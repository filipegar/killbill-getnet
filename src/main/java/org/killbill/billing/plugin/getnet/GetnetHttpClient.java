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
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
import com.google.gson.JsonObject;

public class GetnetHttpClient extends HttpClient {

	private String accessToken;
	private String sellerId;
	private String clientId;
	private String clientSecret;
	private static final Logger logger = LoggerFactory.getLogger(GetnetHttpClient.class);

	public GetnetHttpClient(Properties configProperties) throws GeneralSecurityException {
		super(configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "url", "https://api.getnet.com.br"), null,
				null, null, null, true);

		this.sellerId = configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "seller_id");
		this.clientId = configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "client_id");
		this.clientSecret = configProperties.getProperty(GetnetActivator.PROPERTY_PREFIX + "client_secret");
		this.accessToken = "";
	}

	public void doLogin() {
		String res = this.doLogin(clientId, clientSecret, sellerId);
		JsonObject response = new Gson().fromJson(res, JsonObject.class);
		accessToken = response.get("token_type").getAsString() + " " + response.get("access_token").getAsString();
	}

	public String doLogin(String client_id, String client_secret, String seller_id) {
		String auth = client_id + ":" + client_secret;
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/x-www-form-urlencoded",
				"Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));
		Map<String, String> query = ImmutableMap.of();

		try {
			return doCall(POST, url + "/auth/oauth/v2/token", "scope=oob&grant_type=client_credentials", query, headers,
					String.class, ResponseFormat.TEXT);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("[GETNET]" + e.getMessage());
		} catch (ExecutionException e) {
			logger.error("[GETNET]" + e.getMessage());
		} catch (TimeoutException e) {
			logger.error("[GETNET]" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			logger.error("[GETNET]" + e.getMessage());
		} catch (InvalidRequest e) {
			logger.error("[GETNET]" + e.getMessage());
		}

		throw new Error("Failed");
	}

	public String exchangeTokenForNumberToken(String token) throws PaymentPluginApiException {
		Map<String, String> headers = ImmutableMap.of("Content-Type", "application/json", "Authorization",
				this.getAccessToken(), "seller_id", sellerId);
		Map<String, String> query = ImmutableMap.of();

		try {
			return doCall(GET, url + "/v1/cards/" + token, "", query, headers, String.class, ResponseFormat.TEXT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException
				| InvalidRequest e) {
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
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
		} catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException
				| InvalidRequest e) {
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
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
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
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
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
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
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
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
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
		} catch (InvalidRequest e) {
			if (e.getResponse().getStatusCode() == 400 && e.getResponse().hasResponseBody()) {
				Gson gson = new Gson();
				JsonObject res = gson.fromJson(e.getResponse().getResponseBody(), JsonObject.class);
				throw new PaymentPluginApiException(res.get("message").getAsString(), e);
			}

			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
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
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
		} catch (InvalidRequest e) {
			if (e.getResponse().getStatusCode() == 400 && e.getResponse().hasResponseBody()) {
				Gson gson = new Gson();
				JsonObject res = gson.fromJson(e.getResponse().getResponseBody(), JsonObject.class);
				throw new PaymentPluginApiException(res.get("message").getAsString(), e);
			}

			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
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
			throw new PaymentPluginApiException("Failed to process GETNET paymnet.", e.getMessage());
		} catch (InvalidRequest e) {
			if (e.getResponse().getStatusCode() == 400 && e.getResponse().hasResponseBody()) {
				Gson gson = new Gson();
				JsonObject res = gson.fromJson(e.getResponse().getResponseBody(), JsonObject.class);
				throw new PaymentPluginApiException(res.get("message").getAsString(), e);
			}

			throw new PaymentPluginApiException("Failed to process GETNET paymnet.",
					"Failed to process GETNET paymnet.");
		}
	}

	public String getAccessToken() {
		if (accessToken.isEmpty()) {
			doLogin();
		}

		return accessToken;
	}

}
