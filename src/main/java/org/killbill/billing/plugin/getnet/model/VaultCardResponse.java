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
/*
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
package org.killbill.billing.plugin.getnet.model;

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Conjunto de dados do cartão salvo.
 */
public class VaultCardResponse {
	@SerializedName("card_id")
	private String cardId = null;

	@SerializedName("last_four_digits")
	private String lastFourDigits = null;

	private String bin = null;

	@SerializedName("expiration_month")
	private String expirationMonth = null;

	@SerializedName("expiration_year")
	private String expirationYear = null;

	@SerializedName("brand")
	private String brand = null;

	@SerializedName("cardholder_name")
	private String cardholderName = null;

	@SerializedName("customer_id")
	private String customerId = null;

	@SerializedName("number_token")
	private String numberToken = null;

	@SerializedName("used_at")
	private String usedAt = null;

	@SerializedName("created_at")
	private String createdAt = null;

	@SerializedName("updated_at")
	private String updatedAt = null;

	@SerializedName("status")
	private String status = null;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getNumberToken() {
		return numberToken;
	}

	public void setNumberToken(String numberToken) {
		this.numberToken = numberToken;
	}

	public String getUsedAt() {
		return usedAt;
	}

	public void setUsedAt(String usedAt) {
		this.usedAt = usedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		VaultCardResponse vaultCardResponse = (VaultCardResponse) o;
		return Objects.equals(this.cardId, vaultCardResponse.cardId)
				&& Objects.equals(this.lastFourDigits, vaultCardResponse.lastFourDigits)
				&& Objects.equals(this.expirationMonth, vaultCardResponse.expirationMonth)
				&& Objects.equals(this.expirationYear, vaultCardResponse.expirationYear)
				&& Objects.equals(this.brand, vaultCardResponse.brand)
				&& Objects.equals(this.cardholderName, vaultCardResponse.cardholderName)
				&& Objects.equals(this.customerId, vaultCardResponse.customerId)
				&& Objects.equals(this.numberToken, vaultCardResponse.numberToken)
				&& Objects.equals(this.usedAt, vaultCardResponse.usedAt)
				&& Objects.equals(this.createdAt, vaultCardResponse.createdAt)
				&& Objects.equals(this.updatedAt, vaultCardResponse.updatedAt)
				&& Objects.equals(this.status, vaultCardResponse.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cardId, lastFourDigits, expirationMonth, expirationYear, brand, cardholderName, customerId,
				numberToken, usedAt, createdAt, updatedAt, status);
	}

}
