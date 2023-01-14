/*******************************************************************************
 * Copyright (c) 2023 Filipe Garcia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: Apache-2.0
 *
 * Contributors:
 *     Filipe Garcia - initial API and implementation
 *******************************************************************************/
package org.killbill.billing.plugin.getnet;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.osgi.libs.killbill.OSGIConfigPropertiesService;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.payment.api.Payment;
import org.killbill.billing.payment.api.PaymentApiException;
import org.killbill.billing.payment.api.PaymentMethod;
import org.killbill.billing.payment.api.PaymentMethodPlugin;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.payment.plugin.api.GatewayNotification;
import org.killbill.billing.payment.plugin.api.HostedPaymentPageFormDescriptor;
import org.killbill.billing.payment.plugin.api.PaymentMethodInfoPlugin;
import org.killbill.billing.payment.plugin.api.PaymentPluginApi;
import org.killbill.billing.payment.plugin.api.PaymentPluginApiException;
import org.killbill.billing.payment.plugin.api.PaymentPluginStatus;
import org.killbill.billing.payment.plugin.api.PaymentTransactionInfoPlugin;
import org.killbill.billing.plugin.api.payment.PluginPaymentMethodInfoPlugin;
import org.killbill.billing.plugin.api.payment.PluginPaymentMethodPlugin;
import org.killbill.billing.plugin.api.payment.PluginPaymentTransactionInfoPlugin;
import org.killbill.billing.plugin.getnet.dao.GetnetDao;
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentMethodsRecord;
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentsRecord;
import org.killbill.billing.plugin.getnet.model.BillingAddress;
import org.killbill.billing.plugin.getnet.model.CancelRequestResponse;
import org.killbill.billing.plugin.getnet.model.CardCredit;
import org.killbill.billing.plugin.getnet.model.Credit;
import org.killbill.billing.plugin.getnet.model.Credit.TransactionTypeEnum;
import org.killbill.billing.plugin.getnet.model.CustomerCredit;
import org.killbill.billing.plugin.getnet.model.Order;
import org.killbill.billing.plugin.getnet.model.Order.ProductTypeEnum;
import org.killbill.billing.plugin.getnet.model.PaymentCredit;
import org.killbill.billing.plugin.getnet.model.PaymentCreditDelayedConfirmResponse;
import org.killbill.billing.plugin.getnet.model.PaymentCreditResponse;
import org.killbill.billing.plugin.getnet.model.PaymentCreditVoidReponse;
import org.killbill.billing.plugin.getnet.model.VaultCard;
import org.killbill.billing.plugin.getnet.model.VaultCardResponse;
import org.killbill.billing.plugin.util.KillBillMoney;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.entity.Pagination;
import org.killbill.clock.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetnetPaymentPluginApi implements PaymentPluginApi {

	private static final Logger logger = LoggerFactory.getLogger(GetnetPaymentPluginApi.class);
	private OSGIKillbillAPI killbillAPI;
	private Clock clock;
	private GetnetHttpClient client;
	private GetnetDao getnetDao;
	private final GetnetConfigurationHandler getnetConfigurationHandler;

	public GetnetPaymentPluginApi(final OSGIKillbillAPI killbillAPI, final Clock clock,
			OSGIConfigPropertiesService configProperties, GetnetDao getnetDao,
			GetnetConfigurationHandler getnetConfigurationHandler) {
		this.killbillAPI = killbillAPI;
		this.clock = clock;
		this.getnetDao = getnetDao;
		this.getnetConfigurationHandler = getnetConfigurationHandler;
		this.client = null;
	}

	@Override
	public PaymentTransactionInfoPlugin authorizePayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {

		return executePaymentTransaction(TransactionType.AUTHORIZE, kbAccountId, kbPaymentId, kbTransactionId,
				kbPaymentMethodId, amount, currency, properties, context);
	}

	@Override
	public PaymentTransactionInfoPlugin capturePayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = null;

		this.ensureClient(context.getTenantId());

		try {
			GetnetPaymentsRecord record = getnetDao.getSuccessfulAuthorizationResponse(kbPaymentId,
					context.getTenantId());

			String res = client.captureTransactionRequest(record.getGetnetPaymentId(),
					Math.toIntExact(KillBillMoney.toMinorUnits(currency.toString(), amount)));
			logger.debug("[GETNET] PAYMNET RESPONSE" + res);
			Gson gson = new Gson();
			PaymentCreditDelayedConfirmResponse response = gson.fromJson(res,
					PaymentCreditDelayedConfirmResponse.class);

			if (response.getStatus().equalsIgnoreCase("CONFIRMED")) {
				try {
					record = getnetDao.addResponseGeneric(kbAccountId, kbPaymentId, kbTransactionId,
							TransactionType.CAPTURE, amount, currency, response, context.getTenantId(), record);

					return buildPaymentTransactionInfoPlugin(record);
				} catch (SQLException e) {
					logger.error("Getnet DAO failed to save data. " + e.getMessage());
				}
			} else {
				throw new PaymentPluginApiException("Transaction could not be captured on Getnet.",
						response.getCreditConfirm().getMessage());
			}
		} catch (SQLException e) {
			throw new PaymentPluginApiException("Failed to retrieve last transaction to capture.", e);
		}

		return paymentTransactionInfoPlugin;
	}

	@Override
	public PaymentTransactionInfoPlugin purchasePayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		return executePaymentTransaction(TransactionType.PURCHASE, kbAccountId, kbPaymentId, kbTransactionId,
				kbPaymentMethodId, amount, currency, properties, context);
	}

	@Override
	public PaymentTransactionInfoPlugin voidPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, Iterable<PluginProperty> properties, CallContext context)
			throws PaymentPluginApiException {
		this.ensureClient(context.getTenantId());
		try {
			Payment originalPayment = killbillAPI.getPaymentApi().getPayment(kbPaymentId, true, false, properties,
					context);
			long diffInMillies = Math.abs(clock.getUTCNow().getMillis() - originalPayment.getCreatedDate().getMillis());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			if (diff >= 1 && originalPayment.getCapturedAmount().compareTo(BigDecimal.ZERO) > 0) {
				throw new PaymentPluginApiException("Failed.",
						"Cannot void a captured transaction older than a day. Retry with Refund.");
			} else if (diff >= 1 && diff < 7 && originalPayment.getCapturedAmount().compareTo(BigDecimal.ZERO) == 0) {
				throw new PaymentPluginApiException("Failed.",
						"Preauthorization expired as is older than 7 days. Check Getnet directly.");
			}

			try {
				GetnetPaymentsRecord record = null;
				String getnetPaymentId = "";
				List<GetnetPaymentsRecord> responses = getnetDao.getResponses(kbPaymentId, context.getTenantId());
				for (int i = 0; i < responses.size(); i++) {
					record = responses.get(i);
					if (!record.getGetnetPaymentId().isEmpty()) {
						getnetPaymentId = record.getGetnetPaymentId();
						break;
					}
				}

				if (getnetPaymentId.isEmpty() || record == null) {
					throw new PaymentPluginApiException("Failed.", "Failed to find the original Getnet payment id.");
				}

				String res = client.voidTransactionRequest(getnetPaymentId);
				logger.debug("[GETNET] VOID RESPONSE" + res);
				Gson gson = new Gson();
				PaymentCreditVoidReponse response = gson.fromJson(res, PaymentCreditVoidReponse.class);

				getnetDao.addResponseGeneric(kbAccountId, kbPaymentId, kbTransactionId, TransactionType.VOID,
						record.getAmount(), Currency.fromCode(record.getCurrency()), response, context.getTenantId(),
						record);

				if (response.getStatus().equalsIgnoreCase("CANCELED")) {
					return new PluginPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId, TransactionType.VOID,
							originalPayment.getAuthAmount(), originalPayment.getCurrency(),
							PaymentPluginStatus.PROCESSED, response.getMessageField(), response.getStatus(),
							response.getPaymentId(), response.getOrderId(), clock.getUTCNow(), clock.getUTCNow(),
							new ArrayList<PluginProperty>());
				}

				return new PluginPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId, TransactionType.VOID,
						originalPayment.getAuthAmount(), originalPayment.getCurrency(), PaymentPluginStatus.ERROR,
						response.getMessageField(), response.getStatus(), response.getPaymentId(),
						response.getOrderId(), clock.getUTCNow(), clock.getUTCNow(), new ArrayList<PluginProperty>());
			} catch (SQLException e) {
				throw new PaymentPluginApiException("#voidPayment, SQL Exception", e);
			}
		} catch (PaymentApiException e) {
			throw new PaymentPluginApiException("Failed.", "#voidPayment, Failed to retrieve related transaction.");
		}
	}

	@Override
	public PaymentTransactionInfoPlugin creditPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		throw new PaymentPluginApiException("INTERNAL", "#creditPayment not supported.");
	}

	@Override
	public PaymentTransactionInfoPlugin refundPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = null;

		this.ensureClient(context.getTenantId());

		try {
			GetnetPaymentsRecord record = null;
			String getnetPaymentId = "";
			List<GetnetPaymentsRecord> responses = getnetDao.getResponses(kbPaymentId, context.getTenantId());
			for (int i = 0; i < responses.size(); i++) {
				record = responses.get(i);
				if (!record.getGetnetPaymentId().isEmpty()) {
					getnetPaymentId = record.getGetnetPaymentId();
					break;
				}
			}

			String res = client.refundTransaction(getnetPaymentId,
					Math.toIntExact(KillBillMoney.toMinorUnits(currency.toString(), amount)),
					kbTransactionId.toString());
			Gson gson = new Gson();
			CancelRequestResponse response = gson.fromJson(res, CancelRequestResponse.class);

			getnetDao.addResponseGeneric(kbAccountId, kbPaymentId, kbTransactionId, TransactionType.REFUND, amount,
					currency, response, context.getTenantId(), record);

			PaymentPluginStatus status;
			if (response.getStatus().equalsIgnoreCase("DENIED")) {
				status = PaymentPluginStatus.ERROR;
			} else {
				status = PaymentPluginStatus.PENDING;
			}

			paymentTransactionInfoPlugin = new PluginPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
					TransactionType.REFUND, amount, currency, status, response.getStatus(),
					status.equals(PaymentPluginStatus.PENDING) ? "00" : "01", response.getCancelRequestId(), null,
					clock.getUTCNow(), clock.getUTCNow(), new ArrayList<PluginProperty>());
		} catch (SQLException | PaymentPluginApiException e) {
			paymentTransactionInfoPlugin = new PluginPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
					TransactionType.REFUND, amount, currency, PaymentPluginStatus.ERROR,
					"Failed to find related Getnet transaction. " + e.getMessage(), "E100", null, null,
					clock.getUTCNow(), clock.getUTCNow(), new ArrayList<PluginProperty>());
		}

		return paymentTransactionInfoPlugin;
	}

	@Override
	public List<PaymentTransactionInfoPlugin> getPaymentInfo(UUID kbAccountId, UUID kbPaymentId,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		List<PaymentTransactionInfoPlugin> returnList = new ArrayList<PaymentTransactionInfoPlugin>();

		try {
			GetnetPaymentsRecord record = getnetDao.getSuccessfulAuthorizationResponse(kbPaymentId,
					context.getTenantId());

			if (record != null) {
				returnList.add(buildPaymentTransactionInfoPlugin(record));
			}
		} catch (SQLException e) {
			logger.error("GetnetDAO failed to retrieve more information on payment. " + e.getMessage());
		}
		return returnList;
	}

	@Override
	public void addPaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, PaymentMethodPlugin paymentMethodProps,
			boolean setDefault, Iterable<PluginProperty> properties, CallContext context)
			throws PaymentPluginApiException {
		List<PluginProperty> result = ImmutableList.copyOf(properties);
		PluginProperty externalProp = null;
		Boolean shouldRecordCard = false;

		if (result.size() >= 1) {
			for (int i = 0; i < result.size(); i++) {
				externalProp = result.get(i);
				if (externalProp.getKey().equals("cardId")) {
					shouldRecordCard = true;
					break;
				}
			}
		}

		if (shouldRecordCard == true && externalProp != null) {
			this.addPaymentMethodLocal(kbAccountId, kbPaymentMethodId, paymentMethodProps, setDefault, context,
					externalProp.getValue().toString());
		} else {
			this.addPaymentMethodGetnet(kbAccountId, kbPaymentMethodId, paymentMethodProps, setDefault, properties,
					context);
		}
	}

	@Override
	public void deletePaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		GetnetPaymentMethodsRecord record;
		this.ensureClient(context.getTenantId());

		try {
			record = getnetDao.getPaymentMethod(kbPaymentMethodId, context.getTenantId());
			try {
				client.deleteCardFromVault(record.getGetnetCardId().toString());
			} catch (PaymentPluginApiException e) {
				logger.error("[GETNET] Failed to remove card on Getnet end. " + e.getMessage());
			}

			Map<String, Object> props = ImmutableMap.of("isDeleted", true, "isDefault", false);
			getnetDao.updatePaymentMethod(kbAccountId, kbPaymentMethodId, props, clock.getUTCNow(),
					context.getTenantId(), record.getGetnetCardId().toString());
		} catch (SQLException e) {
			throw new PaymentPluginApiException("#deletePaymentMethod failed to find/update Getnet table records.", e);
		}
	}

	@Override
	public PaymentMethodPlugin getPaymentMethodDetail(UUID kbAccountId, UUID kbPaymentMethodId,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		List<PluginProperty> outputProperties = new ArrayList<PluginProperty>();
		Gson gson = new Gson();
		PaymentMethod paymentMethod = null;
		this.ensureClient(context.getTenantId());

		try {
			paymentMethod = killbillAPI.getPaymentApi().getPaymentMethodById(kbPaymentMethodId, false, false,
					properties, context);
			GetnetPaymentMethodsRecord record = getnetDao.getPaymentMethod(kbPaymentMethodId, context.getTenantId());
			String res = client.exchangeTokenForNumberToken(record.getGetnetCardId().toString());
			VaultCardResponse cardRes = gson.fromJson(res, VaultCardResponse.class);

			outputProperties.add(new PluginProperty("getnetCardId", cardRes.getCardId(), false));
			outputProperties.add(new PluginProperty("brand", cardRes.getBrand(), false));
			outputProperties.add(new PluginProperty("lastFourDigits", cardRes.getLastFourDigits(), false));
			outputProperties.add(new PluginProperty("expirationMonth", cardRes.getExpirationMonth(), false));
			outputProperties.add(new PluginProperty("expirationYear", cardRes.getExpirationYear(), false));
			outputProperties.add(new PluginProperty("customerId", cardRes.getCustomerId(), false));
			outputProperties.add(new PluginProperty("cardholderName", cardRes.getCardholderName(), false));
			outputProperties.add(new PluginProperty("usedAt", cardRes.getUsedAt(), false));
			outputProperties.add(new PluginProperty("status", cardRes.getStatus(), false));

			return new PluginPaymentMethodPlugin(kbPaymentMethodId, paymentMethod.getExternalKey(), false,
					outputProperties);
		} catch (PaymentPluginApiException e) {
			// guarantees that Killbill will be able to render this payment method anyway,
			// even if it does not exist on Getnet. Maybe it's a problem.
			return new PluginPaymentMethodPlugin(kbPaymentMethodId,
					paymentMethod != null ? paymentMethod.getExternalKey() : kbPaymentMethodId.toString(), false,
					outputProperties);
		} catch (SQLException e) {
			throw new PaymentPluginApiException("Requested card does not exist in Getnet tables. Plugin wont work.", e);
		} catch (PaymentApiException e) {
			throw new PaymentPluginApiException("Payment API failed.", e);
		}
	}

	@Override
	public List<PaymentMethodInfoPlugin> getPaymentMethods(UUID kbAccountId, boolean refreshFromGateway,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		List<PaymentMethodInfoPlugin> returnList = new ArrayList<PaymentMethodInfoPlugin>();
		if (!refreshFromGateway) {
			return returnList;
		}

		this.ensureClient(context.getTenantId());
		try {
			Account account = killbillAPI.getAccountUserApi().getAccountById(kbAccountId, context);
			List<PaymentMethod> payments = killbillAPI.getPaymentApi().getAccountPaymentMethods(kbAccountId, false,
					false, new ArrayList<PluginProperty>(), context);
			for (int j = 0; j < payments.size(); j++) {
				PaymentMethod pay = payments.get(j);
				killbillAPI.getPaymentApi().deletePaymentMethod(account, pay.getId(), false, true,
						new ArrayList<PluginProperty>(), context);
			}

			String res = client.getCardsByCustomerId(account.getExternalKey());
			logger.debug("[GETNET] CARDS RESPONSE" + res);
			Gson gson = new Gson();
			JsonObject response = gson.fromJson(res, JsonObject.class);

			if (!response.has("cards")) {
				throw new PaymentPluginApiException("Failed to get the card list from Getnet.", "Gateway failed.");
			}

			JsonArray cards = response.getAsJsonArray("cards");
			for (int i = 0; i < cards.size(); i++) {
				JsonObject card = cards.get(i).getAsJsonObject();
				returnList.add(new PluginPaymentMethodInfoPlugin(kbAccountId,
						UUID.fromString(card.get("card_id").getAsString()), i == 0, card.get("card_id").getAsString()));
			}
		} catch (AccountApiException | PaymentApiException e) {
			logger.error("[GETNET] getPaymentMethods failed - " + e.getMessage());
			return returnList;
		}

		return returnList;
	}

	@Override
	public void resetPaymentMethods(UUID kbAccountId, List<PaymentMethodInfoPlugin> paymentMethods,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		throw new PaymentPluginApiException("INTERNAL", "#resetPaymentMethods not supported.");
	}

	@Override
	public void setDefaultPaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		try {
			GetnetPaymentMethodsRecord record = getnetDao.getPaymentMethod(kbPaymentMethodId, context.getTenantId());
			getnetDao.markAllNotDefaultCards(kbAccountId, context.getTenantId(), clock.getUTCNow());

			Map<String, Object> props = ImmutableMap.of("isDeleted", false, "isDefault", true);
			getnetDao.updatePaymentMethod(kbAccountId, kbPaymentMethodId, props, clock.getUTCNow(),
					context.getTenantId(), record.getGetnetCardId().toString());
		} catch (SQLException e) {
			throw new PaymentPluginApiException("Failed to update default records on Getnet table", e);
		}
	}

	@Override
	public Pagination<PaymentMethodPlugin> searchPaymentMethods(String searchKey, Long offset, Long limit,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		throw new PaymentPluginApiException("INTERNAL", "#searchPaymentMethods not supported.");
	}

	@Override
	public HostedPaymentPageFormDescriptor buildFormDescriptor(UUID kbAccountId, Iterable<PluginProperty> customFields,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		throw new PaymentPluginApiException("INTERNAL", "#buildFormDescriptor not supported.");
	}

	@Override
	public Pagination<PaymentTransactionInfoPlugin> searchPayments(String searchKey, Long offset, Long limit,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		throw new PaymentPluginApiException("INTERNAL", "#searchPayments not supported.");
	}

	@Override
	public GatewayNotification processNotification(String notification, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		throw new PaymentPluginApiException("INTERNAL", "#processNotification not used.");
	}

	public PaymentTransactionInfoPlugin buildPaymentTransactionInfoPlugin(GetnetPaymentsRecord record) {
		List<PluginProperty> outputProperties = new ArrayList<PluginProperty>();

		outputProperties.add(new PluginProperty("paymentId", record.getGetnetPaymentId(), false));
		outputProperties.add(new PluginProperty("sellerId", record.getSellerId(), false));
		outputProperties.add(new PluginProperty("authorizationCode", record.getAuthorizationCode(), false));
		outputProperties.add(new PluginProperty("terminalNsu", record.getTerminalNsu(), false));
		outputProperties.add(new PluginProperty("acquirerTransactionId", record.getAcquirerTransactionId(), false));
		outputProperties.add(new PluginProperty("transactionId", record.getTransactionId(), false));

		return new PluginPaymentTransactionInfoPlugin(UUID.fromString(record.getKbPaymentId()),
				UUID.fromString(record.getKbPaymentTransactionId()),
				TransactionType.valueOf(record.getTransactionType()), record.getAmount(),
				Currency.fromCode(record.getCurrency()),
				record.getReasonCode().equals("00") ? PaymentPluginStatus.PROCESSED : PaymentPluginStatus.ERROR,
				record.getReasonMessage(), record.getReasonCode(), record.getGetnetPaymentId(), record.getTerminalNsu(),
				DateTime.parse(record.getCreatedDate().toString()), DateTime.parse(record.getAuthorizedAt().toString()),
				outputProperties);
	}

	private PaymentTransactionInfoPlugin executePaymentTransaction(final TransactionType transactionType,
			final UUID kbAccountId, final UUID kbPaymentId, final UUID kbTransactionId, final UUID kbPaymentMethodId,
			final BigDecimal amount, final Currency currency, final Iterable<PluginProperty> properties,
			final CallContext context) throws PaymentPluginApiException {
		PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = null;
		Gson gson = new Gson();
		String res = null;

		this.ensureClient(context.getTenantId());

		try {
			GetnetPaymentMethodsRecord cardRecord = getnetDao.getPaymentMethod(kbPaymentMethodId,
					context.getTenantId());
			res = client.exchangeTokenForNumberToken(cardRecord.getGetnetCardId().toString());
			VaultCardResponse cardRes = gson.fromJson(res, VaultCardResponse.class);

			PaymentCredit getnetPayment = new PaymentCredit();
			getnetPayment.setCurrency(currency.toString());
			getnetPayment.setAmount(Math.toIntExact(KillBillMoney.toMinorUnits(currency.toString(), amount)));
			Order order = new Order();
			order.setOrderId(kbTransactionId.toString());
			order.setProductType(ProductTypeEnum.SERVICE);
			getnetPayment.setOrder(order);
			CustomerCredit customer = new CustomerCredit();
			customer.setCustomerId(kbAccountId.toString());
			BillingAddress billing = new BillingAddress();
			customer.setBillingAddress(billing);
			getnetPayment.setCustomer(customer);
			Credit creditTransaction = new Credit();
			creditTransaction.setPreAuthorization(transactionType.equals(TransactionType.AUTHORIZE));
			creditTransaction.setDelayed(false);
			creditTransaction.setSaveCardData(false);
			creditTransaction.setTransactionType(TransactionTypeEnum.FULL);
			creditTransaction.setNumberInstallments(BigDecimal.valueOf(1));
			creditTransaction.setSoftDescriptor(("COB " + kbTransactionId.toString()).substring(0, 20));
			CardCredit card = new CardCredit();
			card.setNumberToken(cardRes.getNumberToken());
			card.setCardholderName(cardRes.getCardholderName());
			card.setExpirationMonth(cardRes.getExpirationMonth());
			card.setExpirationYear(cardRes.getExpirationYear());
			card.setBrand(cardRes.getBrand());
			creditTransaction.setCard(card);
			getnetPayment.setCredit(creditTransaction);

			res = client.sendPaymentRequest(getnetPayment);
			logger.debug("[GETNET] PAYMENT RESPONSE" + res);
			PaymentCreditResponse response = gson.fromJson(res, PaymentCreditResponse.class);

			try {
				GetnetPaymentsRecord record = getnetDao.addResponse(kbAccountId, kbPaymentId, kbTransactionId,
						transactionType, amount, currency, response, context.getTenantId());

				return buildPaymentTransactionInfoPlugin(record);
			} catch (SQLException e) {
				logger.error("[GETNET] Getnet DAO failed to save data. " + e.getMessage());
				throw new PaymentPluginApiException("Failed to store transaction on local tables.", e);
			}
		} catch (PaymentPluginApiException e) {
			logger.error("[GETNET] Transaction may have failed. " + e.getMessage());
			paymentTransactionInfoPlugin = new PluginPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
					transactionType.equals(TransactionType.AUTHORIZE) ? TransactionType.AUTHORIZE
							: TransactionType.PURCHASE,
					amount, currency, PaymentPluginStatus.UNDEFINED, e.getMessage(), "E1000", null, null,
					new DateTime(), null, null);

			logger.debug("[GETNET] Returning paymentTransactionInfoPlugin={}", paymentTransactionInfoPlugin);
			return paymentTransactionInfoPlugin;
		} catch (SQLException e) {
			throw new PaymentPluginApiException("Failed to retrieve card from local tables.", e);
		}
	}

	private void addPaymentMethodGetnet(UUID kbAccountId, UUID kbPaymentMethodId,
			PaymentMethodPlugin paymentMethodProps, boolean setDefault, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		VaultCard vaultCard = new VaultCard();
		List<PluginProperty> props = paymentMethodProps.getProperties();
		Gson gson = new Gson();
		this.ensureClient(context.getTenantId());
		for (int i = 0; i < props.size(); i++) {
			switch (props.get(i).getKey()) {
			case "ccFirstName":
				vaultCard.setCardholderName(props.get(i).getValue().toString());
				break;
			case "ccExpirationMonth":
				vaultCard.setExpirationMonth(props.get(i).getValue().toString().substring(0, 2));
				break;
			case "ccExpirationYear":
				vaultCard.setExpirationYear(props.get(i).getValue().toString().substring(0, 2));
				break;
			case "ccNumber":
				String res = client.tokenCard(kbAccountId.toString(), props.get(i).getValue().toString());
				JsonObject response = gson.fromJson(res, JsonObject.class);
				vaultCard.setNumberToken(response.get("number_token").getAsString());
				break;
			default:
				break;
			}
		}

		vaultCard.setVerifyCard(Boolean.valueOf(getnetConfigurationHandler.getConfigurable(context.getTenantId())
				.getProperty(GetnetActivator.PROPERTY_PREFIX + "verify_card", "true")));

		try {
			Account account = killbillAPI.getAccountUserApi().getAccountById(kbAccountId, context);
			vaultCard.setCustomerId(account.getExternalKey());
			String res = client.saveCardToVault(vaultCard);
			VaultCardResponse response = gson.fromJson(res, VaultCardResponse.class);

			Map<String, String> daoProperties = ImmutableMap.of("token", response.getCardId());
			getnetDao.addPaymentMethod(kbAccountId, kbPaymentMethodId, setDefault, daoProperties, clock.getUTCNow(),
					context.getTenantId());
		} catch (AccountApiException e) {
			throw new PaymentPluginApiException("#addPaymentMethod failed with internal API.", e);
		} catch (SQLException e) {
			throw new PaymentPluginApiException("#addPaymentMethod failed to store on internal payments table.",
					e.getMessage());
		}
	}

	private void addPaymentMethodLocal(UUID kbAccountId, UUID kbPaymentMethodId, PaymentMethodPlugin paymentMethodProps,
			boolean setDefault, CallContext context, String cardToken) throws PaymentPluginApiException {

		Map<String, String> daoProperties = ImmutableMap.of("token", cardToken);

		try {
			getnetDao.addPaymentMethod(kbAccountId, kbPaymentMethodId, setDefault, daoProperties, clock.getUTCNow(),
					context.getTenantId());
		} catch (SQLException e) {
			throw new PaymentPluginApiException("#addPaymentMethodLocal failed to store on internal payments table.",
					e.getMessage());
		}
	}

	private void ensureClient(UUID tenantId) throws PaymentPluginApiException {
		if (this.client == null || !this.client.getTenantId().equals(tenantId)) {
			try {
				this.client = new GetnetHttpClient(getnetConfigurationHandler.getConfigurable(tenantId), tenantId);
			} catch (GeneralSecurityException e) {
				logger.error("[Getnet] Failed to initialize http client.");
				throw new PaymentPluginApiException("#ensureClient, failed to initialize http client.", e.getMessage());
			}
		}
	}
}
