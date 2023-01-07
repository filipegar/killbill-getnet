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

import org.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;

public class PaymentCreditVoidReponse extends PaymentResponseBase implements PaymentOperation {

	@SerializedName("credit_cancel")
	private PaymentCreditCancelDetails creditCancel = null;

	public PaymentCreditVoidReponse creditCancel(PaymentCreditCancelDetails creditCancel) {
		this.creditCancel = creditCancel;
		return this;
	}

	/**
	 * Get creditCancel
	 * 
	 * @return creditCancel
	 **/
	public PaymentCreditCancelDetails getCreditCancel() {
		return creditCancel;
	}

	public void setCreditCancel(PaymentCreditCancelDetails creditCancel) {
		this.creditCancel = creditCancel;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PaymentCreditVoidReponse creditDelayedCancel = (PaymentCreditVoidReponse) o;
		return Objects.equals(this.paymentId, creditDelayedCancel.paymentId)
				&& Objects.equals(this.sellerId, creditDelayedCancel.sellerId)
				&& Objects.equals(this.amount, creditDelayedCancel.amount)
				&& Objects.equals(this.currency, creditDelayedCancel.currency)
				&& Objects.equals(this.orderId, creditDelayedCancel.orderId)
				&& Objects.equals(this.status, creditDelayedCancel.status)
				&& Objects.equals(this.creditCancel, creditDelayedCancel.creditCancel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(paymentId, sellerId, amount, currency, orderId, status, creditCancel);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CreditDelayedCancel {\n");

		sb.append("    paymentId: ").append(toIndentedString(paymentId)).append("\n");
		sb.append("    sellerId: ").append(toIndentedString(sellerId)).append("\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
		sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
		sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    creditCancel: ").append(toIndentedString(creditCancel)).append("\n");
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

	@Override
	public String getDateField() {
		return this.getCreditCancel().getCanceledAt() != null && !this.getCreditCancel().getCanceledAt().isEmpty()
				? this.getCreditCancel().getCanceledAt()
				: new DateTime().toString();
	}

	@Override
	public String getMessageField() {
		return this.getCreditCancel().getMessage();
	}

}
