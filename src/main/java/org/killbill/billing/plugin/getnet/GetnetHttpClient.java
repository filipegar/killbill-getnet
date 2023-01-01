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

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

	public String getAccessToken() {
		if(accessToken.isEmpty()) {
			doLogin();
		}
		
		return accessToken;
	}

}
