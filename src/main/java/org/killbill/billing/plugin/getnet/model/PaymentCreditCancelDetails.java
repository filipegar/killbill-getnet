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

import com.google.gson.annotations.SerializedName;

/**
 * Conjunto de informações adicionais do serviço de cancelamento.
 */
public class PaymentCreditCancelDetails {
	@SerializedName("canceled_at")
	private String canceledAt = null;

	@SerializedName("message")
	private String message = null;

	public PaymentCreditCancelDetails canceledAt(String canceledAt) {
		this.canceledAt = canceledAt;
		return this;
	}

	/**
	 * Data/hora do recebimento da transação.
	 * 
	 * @return canceledAt
	 **/
	public String getCanceledAt() {
		return canceledAt;
	}

	public void setCanceledAt(String canceledAt) {
		this.canceledAt = canceledAt;
	}

	public PaymentCreditCancelDetails message(String message) {
		this.message = message;
		return this;
	}

	/**
	 * Aviso de recebimento e finalização pendente a comfirmação.
	 * 
	 * @return message
	 **/
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PaymentCreditCancelDetails paymentCreditCancelDetails = (PaymentCreditCancelDetails) o;
		return Objects.equals(this.canceledAt, paymentCreditCancelDetails.canceledAt)
				&& Objects.equals(this.message, paymentCreditCancelDetails.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(canceledAt, message);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PaymentCreditCancelDetails {\n");

		sb.append("    canceledAt: ").append(toIndentedString(canceledAt)).append("\n");
		sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
