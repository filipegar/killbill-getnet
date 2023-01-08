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
	public String card_id = null;

	@SerializedName("last_four_digits")
	public String last_four_digits = null;

	public String bin = null;

	@SerializedName("expiration_month")
	public String expiration_month = null;

	@SerializedName("expiration_year")
	public String expiration_year = null;

	@SerializedName("brand")
	public String brand = null;

	@SerializedName("cardholder_name")
	public String cardholder_name = null;

	@SerializedName("customer_id")
	public String customer_id = null;

	@SerializedName("number_token")
	public String number_token = null;

	@SerializedName("used_at")
	public String used_at = null;

	@SerializedName("created_at")
	public String created_at = null;

	@SerializedName("updated_at")
	public String updated_at = null;

	@SerializedName("status")
	public String status = null;

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
		return Objects.equals(this.card_id, vaultCardResponse.card_id)
				&& Objects.equals(this.last_four_digits, vaultCardResponse.last_four_digits)
				&& Objects.equals(this.expiration_month, vaultCardResponse.expiration_month)
				&& Objects.equals(this.expiration_year, vaultCardResponse.expiration_year)
				&& Objects.equals(this.brand, vaultCardResponse.brand)
				&& Objects.equals(this.cardholder_name, vaultCardResponse.cardholder_name)
				&& Objects.equals(this.customer_id, vaultCardResponse.customer_id)
				&& Objects.equals(this.number_token, vaultCardResponse.number_token)
				&& Objects.equals(this.used_at, vaultCardResponse.used_at)
				&& Objects.equals(this.created_at, vaultCardResponse.created_at)
				&& Objects.equals(this.updated_at, vaultCardResponse.updated_at)
				&& Objects.equals(this.status, vaultCardResponse.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(card_id, last_four_digits, expiration_month, expiration_year, brand, cardholder_name,
				customer_id, number_token, used_at, created_at, updated_at, status);
	}

}
