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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.joda.time.DateTime;
import org.killbill.billing.payment.plugin.api.PaymentPluginApiException;
import org.killbill.billing.plugin.getnet.model.PaymentCredit;
import org.killbill.billing.plugin.getnet.model.VaultCard;
import org.killbill.billing.plugin.util.http.HttpClient;
import org.killbill.billing.plugin.util.http.InvalidRequest;
import org.killbill.billing.plugin.util.http.ResponseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetnetHttpClient extends HttpClient {

	private String accessToken;
	private String sellerId;
	private String clientId;
	private String clientSecret;
	private static final Logger logger = LoggerFactory.getLogger(GetnetHttpClient.class);
	private UUID tenantId;
	private DateTime tokenExpires;

	public GetnetHttpClient(Properties configProperties, UUID tenantUuid) throws GeneralSecurityException {
		super(configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "url", "https://api.getnet.com.br"), null,
				null, null, null, true);

		this.sellerId = configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "seller_id");
		this.clientId = configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "client_id");
		this.clientSecret = configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "client_secret");
		this.accessToken = "";
		this.tenantId = tenantUuid;

		this.tokenExpires = DateTime.now();
	}

	public void doLogin() throws PaymentPluginApiException {
		String res = this.doLogin(clientId, clientSecret, sellerId);
		JsonObject response = new Gson().fromJson(res, JsonObject.class);
		this.accessToken = response.get("token_type").getAsString() + " " + response.get("access_token").getAsString();
		this.tokenExpires = DateTime.now().plus(response.get("expires_in").getAsLong() * 1000);
	}

	public String doLogin(String client_id, String client_secret, String seller_id) throws PaymentPluginApiException {
		String auth = client_id + ":" + client_secret;
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/x-www-form-urlencoded",
				"Authorization",
				"Basic " + Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("UTF-8"))));
		Map<String, String> query = ImmutableMap.of();

		try {
			return doCall(POST, url + "/auth/oauth/v2/token", "scope=oob&grant_type=client_credentials", query, headers,
					String.class, ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
			logger.error("[GETNET] doLogin error - " + e.getMessage());
			throw new PaymentPluginApiException("Failed communicate with Getnet.", e);
		} catch (InvalidRequest e) {
			logger.error("[GETNET] doLogin failed - " + e.getMessage());
			throw new PaymentPluginApiException("Failed to authenticate on Getnet.", e);
		}
	}

	public String exchangeTokenForNumberToken(String token) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken(), "seller_id", sellerId);
		Map<String, String> query = ImmutableMap.of();

		try {
			return doCall(GET, url + "/v1/cards/" + token, "", query, headers, String.class, ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException
				| InvalidRequest e) {
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		}
	}

	public String sendPaymentRequest(PaymentCredit payment) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken());
		Map<String, String> query = ImmutableMap.of();

		payment.setSellerId(sellerId);

		try {
			return doCall(POST, url + "/v1/payments/credit", payment.toString(), query, headers, String.class,
					ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {			
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		} catch (InvalidRequest e) {
			Gson gson = new Gson();
			JsonObject response = gson.fromJson(e.getResponse().getResponseBody(), JsonObject.class);
			String longError = "";
			
			if(response.has("details")) {
				JsonArray details = response.getAsJsonArray("details");
				if(details.size() >= 1) {
					JsonObject entry = (JsonObject) details.get(0);
					longError = entry.get("description").getAsString() + " - " + entry.get("description_detail").getAsString() + " - " + entry.get("status").getAsString();
					
					throw new PaymentPluginApiException(entry.get("error_code").getAsString(), longError);
				}
			}
			
			throw new PaymentPluginApiException(response.get("name").getAsString() + " " + response.get("message").getAsString(), e.getResponse().getResponseBody());
		}
	}

	public String captureTransactionRequest(String paymentId, Integer amount) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken());
		Map<String, String> query = ImmutableMap.of();

		JsonObject request = new JsonObject();
		request.addProperty("amount", amount);

		try {
			return doCall(POST, url + "/v1/payments/credit/" + paymentId + "/confirm", request.toString(), query,
					headers, String.class, ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException
				| InvalidRequest e) {
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		}
	}

	public String voidTransactionRequest(String paymentId) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken());
		Map<String, String> query = ImmutableMap.of();

		try {
			return doCall(POST, url + "/v1/payments/credit/" + paymentId + "/cancel", "{}", query, headers,
					String.class, ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException
				| InvalidRequest e) {
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		}
	}

	public String getCardsByCustomerId(String customerId) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken(), "seller_id", sellerId);
		Map<String, String> query = ImmutableMap.of("status", "active", "customer_id", customerId);

		try {
			return doCall(GET, url + "/v1/cards", "{}", query, headers, String.class, ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException
				| InvalidRequest e) {
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		}
	}

	public String deleteCardFromVault(String cardId) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken(), "seller_id", sellerId);
		Map<String, String> query = ImmutableMap.of();

		try {
			return doCall(DELETE, url + "/v1/cards/" + cardId, "{}", query, headers, String.class, ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException
				| InvalidRequest e) {
			throw new PaymentPluginApiException("Failed to delete card on Getnet.", e);
		}
	}

	public String refundTransaction(String paymentId, Integer amount, String cancelCustomKey)
			throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken(), "seller_id", sellerId);
		Map<String, String> query = ImmutableMap.of();

		JsonObject request = new JsonObject();
		request.addProperty("cancel_amount", amount);
		request.addProperty("payment_id", paymentId);
		request.addProperty("cancel_custom_key", cancelCustomKey.substring(0, 30));

		try {
			return doCall(POST, url + "/v1/payments/cancel/request", request.toString(), query, headers, String.class,
					ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		} catch (InvalidRequest e) {
			if (e.getResponse().getStatusCode() == 400 && e.getResponse().hasResponseBody()) {
				Gson gson = new Gson();
				JsonObject res = gson.fromJson(e.getResponse().getResponseBody(), JsonObject.class);
				throw new PaymentPluginApiException(res.get("message").getAsString(), e);
			}

			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		}
	}

	public String tokenCard(String accountId, String pan) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken(), "seller_id", sellerId);
		Map<String, String> query = ImmutableMap.of();

		JsonObject request = new JsonObject();
		request.addProperty("card_number", pan);
		request.addProperty("customer_id", accountId);

		try {
			return doCall(POST, url + "/v1/tokens/card", request.toString(), query, headers, String.class,
					ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		} catch (InvalidRequest e) {
			if (e.getResponse().getStatusCode() == 400 && e.getResponse().hasResponseBody()) {
				Gson gson = new Gson();
				JsonObject res = gson.fromJson(e.getResponse().getResponseBody(), JsonObject.class);
				throw new PaymentPluginApiException(res.get("message").getAsString(), e);
			}

			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		}
	}

	public String saveCardToVault(VaultCard vaultCard) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken(), "seller_id", sellerId);
		Map<String, String> query = ImmutableMap.of();

		try {
			return doCall(POST, url + "/v1/cards", vaultCard.toString(), query, headers, String.class,
					ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
			throw new PaymentPluginApiException("Failed to process GETNET payment.", e.getMessage());
		} catch (InvalidRequest e) {
			if (e.getResponse().getStatusCode() == 400 && e.getResponse().hasResponseBody()) {
				Gson gson = new Gson();
				JsonObject res = gson.fromJson(e.getResponse().getResponseBody(), JsonObject.class);
				logger.error("GETNET SAVE CARD ERROR:" + e.getResponse().getResponseBody());
				JsonObject details = res.get("details").getAsJsonArray().get(0).getAsJsonObject();
				throw new PaymentPluginApiException(res.get("message").getAsString() + " - Erro reportado: "
						+ details.get("status").getAsString() + ", " + details.get("description").getAsString() + " - "
						+ details.get("description_detail").getAsString(), e);
			}

			throw new PaymentPluginApiException("Failed to process GETNET payment.",
					"Failed to process GETNET payment.");
		}
	}

	public String getAccessToken() throws PaymentPluginApiException {
		if (this.accessToken.isEmpty() || DateTime.now().isAfter(tokenExpires)) {
			doLogin();
		}

		return this.accessToken;
	}

	public UUID getTenantId() {
		return this.tenantId;
	}

}
