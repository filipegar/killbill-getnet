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

import com.google.gson.annotations.SerializedName;

public class PaymentCreditDelayedConfirmResponse extends PaymentResponseBase {

	@SerializedName("credit_confirm")
	private PaymentCreditConfirmDetails creditConfirm = null;

	/**
	 * Get creditConfirm
	 * 
	 * @return creditConfirm
	 **/
	public PaymentCreditConfirmDetails getCreditConfirm() {
		return creditConfirm;
	}

	public void setCreditConfirm(PaymentCreditConfirmDetails creditConfirm) {
		this.creditConfirm = creditConfirm;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PaymentCreditDelayedConfirmResponse creditDelayedConfirm = (PaymentCreditDelayedConfirmResponse) o;
		return Objects.equals(this.paymentId, creditDelayedConfirm.paymentId)
				&& Objects.equals(this.sellerId, creditDelayedConfirm.sellerId)
				&& Objects.equals(this.amount, creditDelayedConfirm.amount)
				&& Objects.equals(this.currency, creditDelayedConfirm.currency)
				&& Objects.equals(this.orderId, creditDelayedConfirm.orderId)
				&& Objects.equals(this.status, creditDelayedConfirm.status)
				&& Objects.equals(this.creditConfirm, creditDelayedConfirm.creditConfirm);
	}

	@Override
	public int hashCode() {
		return Objects.hash(paymentId, sellerId, amount, currency, orderId, status, creditConfirm);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CreditDelayedConfirm {\n");

		sb.append("    paymentId: ").append(toIndentedString(paymentId)).append("\n");
		sb.append("    sellerId: ").append(toIndentedString(sellerId)).append("\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
		sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
		sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    creditConfirm: ").append(toIndentedString(creditConfirm)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
