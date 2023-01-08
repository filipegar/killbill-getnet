/*
 * Copyright 2021 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.killbill.billing.plugin.getnet;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.catalog.api.Currency;
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
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentsRecord;
import org.killbill.billing.plugin.getnet.model.BillingAddress;
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
import org.killbill.billing.plugin.getnet.model.VaultCardResponse;
import org.killbill.billing.plugin.util.KillBillMoney;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.entity.Pagination;
import org.killbill.clock.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetnetPaymentPluginApi implements PaymentPluginApi {

	private static final Logger logger = LoggerFactory.getLogger(GetnetPaymentPluginApi.class);
	private OSGIKillbillAPI killbillAPI;
	private Clock clock;
	private GetnetHttpClient client;
	private GetnetDao getnetDao;

	public GetnetPaymentPluginApi(final OSGIKillbillAPI killbillAPI, final Clock clock, Properties configProperties,
			GetnetDao getnetDao) {
		this.killbillAPI = killbillAPI;
		this.clock = clock;
		this.getnetDao = getnetDao;

		try {
			this.client = new GetnetHttpClient(configProperties);
		} catch (GeneralSecurityException e) {
			logger.error("[Getnet] Failed to create http client.");
		}
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void deletePaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		PaymentMethod paymentMethod = null;
		try {
			paymentMethod = killbillAPI.getPaymentApi().getPaymentMethodById(kbPaymentMethodId, false, false,
					properties, context);
			client.deleteCardFromVault(paymentMethod.getExternalKey().toString());
		} catch (PaymentApiException e) {
			throw new PaymentPluginApiException("#deletePaymentMethod failed with internal API.", e);
		}
	}

	@Override
	public PaymentMethodPlugin getPaymentMethodDetail(UUID kbAccountId, UUID kbPaymentMethodId,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		PaymentMethod paymentMethod = null;
		List<PluginProperty> outputProperties = new ArrayList<PluginProperty>();
		try {
			paymentMethod = killbillAPI.getPaymentApi().getPaymentMethodById(kbPaymentMethodId, false, false,
					properties, context);
			VaultCardResponse cardRes = client.exchangeTokenForNumberToken(paymentMethod.getExternalKey().toString());

			outputProperties.add(new PluginProperty("getnetCardId", cardRes.card_id, false));
			outputProperties.add(new PluginProperty("brand", cardRes.brand, false));
			outputProperties.add(new PluginProperty("lastFourDigits", cardRes.last_four_digits, false));
			outputProperties.add(new PluginProperty("expirationMonth", cardRes.expiration_month, false));
			outputProperties.add(new PluginProperty("expirationYear", cardRes.expiration_year, false));
			outputProperties.add(new PluginProperty("customerId", cardRes.customer_id, false));
			outputProperties.add(new PluginProperty("cardholderName", cardRes.cardholder_name, false));
			outputProperties.add(new PluginProperty("usedAt", cardRes.used_at, false));
			outputProperties.add(new PluginProperty("status", cardRes.status, false));

			return new PluginPaymentMethodPlugin(kbPaymentMethodId, paymentMethod.getExternalKey(), false,
					outputProperties);
		} catch (PaymentPluginApiException e) {
			// guarantees that Killbill will be able to render this payment method anyway,
			// even if it does not exist on Getnet. Maybe it's a problem.
			return new PluginPaymentMethodPlugin(kbPaymentMethodId,
					paymentMethod != null ? paymentMethod.getExternalKey() : kbPaymentMethodId.toString(), false,
					outputProperties);
		} catch (PaymentApiException e) {
			throw new PaymentPluginApiException("#getPaymentMethodDetail failed with internal API.", e);
		}
	}

	@Override
	public List<PaymentMethodInfoPlugin> getPaymentMethods(UUID kbAccountId, boolean refreshFromGateway,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		List<PaymentMethodInfoPlugin> returnList = new ArrayList<PaymentMethodInfoPlugin>();
		if (!refreshFromGateway) {
			return returnList;
		}

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
			return returnList;
		}

		return returnList;
	}

	@Override
	public void resetPaymentMethods(UUID kbAccountId, List<PaymentMethodInfoPlugin> paymentMethods,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		// not supported.
	}

	@Override
	public void setDefaultPaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		// not supported.
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
		throw new PaymentPluginApiException("INTERNAL",
				"#processNotification not yet implemented, please contact support@killbill.io");
	}

	private PaymentTransactionInfoPlugin buildPaymentTransactionInfoPlugin(GetnetPaymentsRecord record) {
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

		try {
			final PaymentMethod paymentMethod = killbillAPI.getPaymentApi().getPaymentMethodById(kbPaymentMethodId,
					false, false, properties, context);
			VaultCardResponse cardRes = client.exchangeTokenForNumberToken(paymentMethod.getExternalKey().toString());

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
			card.setNumberToken(cardRes.number_token);
			card.setCardholderName(cardRes.cardholder_name);
			card.setExpirationMonth(cardRes.expiration_month);
			card.setExpirationYear(cardRes.expiration_year);
			card.setBrand(cardRes.brand);
			creditTransaction.setCard(card);
			getnetPayment.setCredit(creditTransaction);

			String res = client.sendPaymentRequest(getnetPayment);
			logger.debug("[GETNET] PAYMNET RESPONSE" + res);
			Gson gson = new Gson();
			PaymentCreditResponse response = gson.fromJson(res, PaymentCreditResponse.class);

			try {
				GetnetPaymentsRecord record = getnetDao.addResponse(kbAccountId, kbPaymentId, kbTransactionId,
						transactionType, amount, currency, response, context.getTenantId());

				return buildPaymentTransactionInfoPlugin(record);
			} catch (SQLException e) {
				logger.error("Getnet DAO failed to save data. " + e.getMessage());
			}
		} catch (PaymentApiException | PaymentPluginApiException e) {
			logger.error("Failed to retrieve context. " + e.getMessage());
			paymentTransactionInfoPlugin = new PluginPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
					TransactionType.PURCHASE, amount, currency, PaymentPluginStatus.ERROR, "Error", "100", null, null,
					new DateTime(), null, null);

			logger.info("Returning paymentTransactionInfoPlugin={}", paymentTransactionInfoPlugin);
			return paymentTransactionInfoPlugin;
		}

		return paymentTransactionInfoPlugin;
	}

}
