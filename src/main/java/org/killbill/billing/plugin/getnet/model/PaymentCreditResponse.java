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
 * Conjunto de dados da resposta do pagamento de crédito.
 */
public class PaymentCreditResponse extends PaymentResponseBase {

	@SerializedName("received_at")
	public String receivedAt = null;

	@SerializedName("credit")
	public PaymentCreditDetail credit = null;

	public PaymentCreditResponse paymentId(String paymentId) {
		this.paymentId = paymentId;
		return this;
	}

	public PaymentCreditResponse receivedAt(String receivedAt) {
		this.receivedAt = receivedAt;
		return this;
	}

	/**
	 * Data e hora do registro de pagamento.
	 * 
	 * @return receivedAt
	 **/
	public String getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(String receivedAt) {
		this.receivedAt = receivedAt;
	}

	public PaymentCreditResponse credit(PaymentCreditDetail credit) {
		this.credit = credit;
		return this;
	}

	/**
	 * Get credit
	 * 
	 * @return credit
	 **/
	public PaymentCreditDetail getCredit() {
		return credit;
	}

	public void setCredit(PaymentCreditDetail credit) {
		this.credit = credit;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PaymentCreditResponse paymentCreditResponse = (PaymentCreditResponse) o;
		return Objects.equals(this.paymentId, paymentCreditResponse.paymentId)
				&& Objects.equals(this.sellerId, paymentCreditResponse.sellerId)
				&& Objects.equals(this.amount, paymentCreditResponse.amount)
				&& Objects.equals(this.currency, paymentCreditResponse.currency)
				&& Objects.equals(this.orderId, paymentCreditResponse.orderId)
				&& Objects.equals(this.status, paymentCreditResponse.status)
				&& Objects.equals(this.receivedAt, paymentCreditResponse.receivedAt)
				&& Objects.equals(this.credit, paymentCreditResponse.credit);
	}

	@Override
	public int hashCode() {
		return Objects.hash(paymentId, sellerId, amount, currency, orderId, status, receivedAt, credit);
	}

	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
