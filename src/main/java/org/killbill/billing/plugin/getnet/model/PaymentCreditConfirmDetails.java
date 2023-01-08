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
 * Conjunto de informações adicionais do serviço de confirmação.
 */
public class PaymentCreditConfirmDetails {
	@SerializedName("confirm_date")
	private String confirmDate = null;

	@SerializedName("message")
	private String message = null;

	public PaymentCreditConfirmDetails confirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
		return this;
	}

	/**
	 * Data/hora do recebimento da transação.
	 * 
	 * @return confirmDate
	 **/
	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public PaymentCreditConfirmDetails message(String message) {
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
		PaymentCreditConfirmDetails paymentCreditConfirmDetails = (PaymentCreditConfirmDetails) o;
		return Objects.equals(this.confirmDate, paymentCreditConfirmDetails.confirmDate)
				&& Objects.equals(this.message, paymentCreditConfirmDetails.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(confirmDate, message);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PaymentCreditConfirmDetails {\n");

		sb.append("    confirmDate: ").append(toIndentedString(confirmDate)).append("\n");
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