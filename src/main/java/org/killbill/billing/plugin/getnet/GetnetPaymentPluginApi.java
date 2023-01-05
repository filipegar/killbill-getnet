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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.currency.api.boilerplate.CurrencyConversionApiImp;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
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
import org.killbill.billing.plugin.getnet.model.BillingAddress;
import org.killbill.billing.plugin.getnet.model.CardCredit;
import org.killbill.billing.plugin.getnet.model.Credit;
import org.killbill.billing.plugin.getnet.model.Credit.TransactionTypeEnum;
import org.killbill.billing.plugin.getnet.model.CustomerCredit;
import org.killbill.billing.plugin.getnet.model.Order;
import org.killbill.billing.plugin.getnet.model.Order.ProductTypeEnum;
import org.killbill.billing.plugin.getnet.model.PaymentCredit;
import org.killbill.billing.plugin.getnet.model.PaymentCreditResponse;
import org.killbill.billing.plugin.getnet.model.VaultCardResponse;
import org.killbill.billing.plugin.util.KillBillMoney;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.entity.Pagination;
import org.killbill.clock.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class GetnetPaymentPluginApi implements PaymentPluginApi {

	private static final Logger logger = LoggerFactory.getLogger(GetnetPaymentPluginApi.class);
	private OSGIKillbillAPI killbillAPI;
	private Clock clock;
	private GetnetHttpClient client;

	public GetnetPaymentPluginApi(final OSGIKillbillAPI killbillAPI, final Clock clock, Properties configProperties) {
		this.killbillAPI = killbillAPI;
		this.clock = clock;

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
		logger.info("authorizePayment, kbAccountId=" + kbAccountId);
		PaymentTransactionInfoPlugin paymentTransactionInfoPlugin;

		logger.info("authorizePayment, about to request card number token");
		try {
			final PaymentMethod paymentMethod = killbillAPI.getPaymentApi().getPaymentMethodById(kbPaymentMethodId,
					false, true, properties, context);
			VaultCardResponse cardRes = client.exchangeTokenForNumberToken(paymentMethod.getExternalKey().toString());
			logger.info("authorizePayment, successfully retrieved card from vault.");

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
			creditTransaction.setDelayed(false);
			creditTransaction.setPreAuthorization(true);
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

			logger.info("authorizePayment, about to send payment request.");
			String res = client.sendPaymentRequest(getnetPayment);
			logger.info(res);
			Gson gson = new Gson();
			PaymentCreditResponse response = gson.fromJson(res, PaymentCreditResponse.class);
			
			List<PluginProperty> outputProperties = new ArrayList<PluginProperty>();
			outputProperties.add(new PluginProperty("paymentId", response.getPaymentId(), false));
			outputProperties.add(new PluginProperty("sellerId", response.getSellerId(), false));
			outputProperties.add(new PluginProperty("authorizationCode", response.getCredit().getAuthorizationCode(), false));
			outputProperties.add(new PluginProperty("terminalNsu", response.getCredit().getTerminalNsu(), false));
			outputProperties.add(new PluginProperty("acquirerTransactionId", response.getCredit().getAcquirerTransactionId(), false));
			outputProperties.add(new PluginProperty("transactionId", response.getCredit().getTransactionId(), false));
		
			paymentTransactionInfoPlugin = new GetnetPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
					TransactionType.AUTHORIZE, KillBillMoney.fromMinorUnits(response.getCurrency(), response.getAmount()), Currency.fromCode(response.getCurrency()), 
					PaymentPluginStatus.PROCESSED, response.getStatus(), response.getCredit().getReasonCode(), String.valueOf(response.getPaymentId()),
					null, new DateTime(), null, outputProperties);
			
		} catch (PaymentApiException e) {
			// TODO Auto-generated catch block
			logger.error("Failed to retrieve context. " + e.getMessage());
			paymentTransactionInfoPlugin = new GetnetPaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
					TransactionType.AUTHORIZE, amount, currency, PaymentPluginStatus.ERROR, "Error", "100", null, null,
					new DateTime(), null, null);
		}

		logger.info("Returning paymentTransactionInfoPlugin={}", paymentTransactionInfoPlugin);
		return paymentTransactionInfoPlugin;
	}

	@Override
	public PaymentTransactionInfoPlugin capturePayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentTransactionInfoPlugin purchasePayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentTransactionInfoPlugin voidPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, Iterable<PluginProperty> properties, CallContext context)
			throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentTransactionInfoPlugin creditPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
			UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagination<PaymentTransactionInfoPlugin> searchPayments(String searchKey, Long offset, Long limit,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public PaymentMethodPlugin getPaymentMethodDetail(UUID kbAccountId, UUID kbPaymentMethodId,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultPaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<PaymentMethodInfoPlugin> getPaymentMethods(UUID kbAccountId, boolean refreshFromGateway,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagination<PaymentMethodPlugin> searchPaymentMethods(String searchKey, Long offset, Long limit,
			Iterable<PluginProperty> properties, TenantContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetPaymentMethods(UUID kbAccountId, List<PaymentMethodInfoPlugin> paymentMethods,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub

	}

	@Override
	public HostedPaymentPageFormDescriptor buildFormDescriptor(UUID kbAccountId, Iterable<PluginProperty> customFields,
			Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayNotification processNotification(String notification, Iterable<PluginProperty> properties,
			CallContext context) throws PaymentPluginApiException {
		// TODO Auto-generated method stub
		return null;
	}

}
