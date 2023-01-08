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
package org.killbill.billing.plugin.getnet.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class CancelRequestResponse implements PaymentOperation {
	@SerializedName("seller_id")
	private String sellerId = null;

	@SerializedName("payment_id")
	private String paymentId = null;

	@SerializedName("cancel_request_at")
	private String cancelRequestAt = null;

	@SerializedName("cancel_request_id")
	private String cancelRequestId = null;

	@SerializedName("cancel_custom_key")
	private String cancelCustomKey = null;

	@SerializedName("status")
	private String status = null;

	public CancelRequestResponse sellerId(String sellerId) {
		this.sellerId = sellerId;
		return this;
	}

	/**
	 * Código de identificação do e-commerce.
	 * 
	 * @return sellerId
	 **/
	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public CancelRequestResponse paymentId(String paymentId) {
		this.paymentId = paymentId;
		return this;
	}

	/**
	 * Código de identificação do pagamento da transação original.
	 * 
	 * @return paymentId
	 **/
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public CancelRequestResponse cancelRequestAt(String cancelRequestAt) {
		this.cancelRequestAt = cancelRequestAt;
		return this;
	}

	/**
	 * Data da solicitação de cancelamento.
	 * 
	 * @return cancelRequestAt
	 **/
	public String getCancelRequestAt() {
		return cancelRequestAt;
	}

	public void setCancelRequestAt(String cancelRequestAt) {
		this.cancelRequestAt = cancelRequestAt;
	}

	public CancelRequestResponse cancelRequestId(String cancelRequestId) {
		this.cancelRequestId = cancelRequestId;
		return this;
	}

	/**
	 * Identificador do request de cancelamento.
	 * 
	 * @return cancelRequestId
	 **/
	public String getCancelRequestId() {
		return cancelRequestId;
	}

	public void setCancelRequestId(String cancelRequestId) {
		this.cancelRequestId = cancelRequestId;
	}

	public CancelRequestResponse cancelCustomKey(String cancelCustomKey) {
		this.cancelCustomKey = cancelCustomKey;
		return this;
	}

	/**
	 * Chave do cliente utilizada para identificar uma solicitação de cancelamento.
	 * (Chave deve ser única por transação).
	 * 
	 * @return cancelCustomKey
	 **/
	public String getCancelCustomKey() {
		return cancelCustomKey;
	}

	public void setCancelCustomKey(String cancelCustomKey) {
		this.cancelCustomKey = cancelCustomKey;
	}

	public CancelRequestResponse status(String status) {
		this.status = status;
		return this;
	}

	/**
	 * Status da transação aceita ou negada. Para mais informações sobre os status
	 * de retorno, veja em &lt;a
	 * href&#x3D;\&quot;api#tag/Notificacoes-1.0\&quot;&gt;Notificações
	 * 1.0&lt;/a&gt; ou &lt;a
	 * href&#x3D;\&quot;api#tag/Notificacoes-1.1\&quot;&gt;Notificações
	 * 1.1&lt;/a&gt;
	 * 
	 * @return status
	 **/
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CancelRequestResponse cancelRequestResponse = (CancelRequestResponse) o;
		return Objects.equals(this.sellerId, cancelRequestResponse.sellerId)
				&& Objects.equals(this.paymentId, cancelRequestResponse.paymentId)
				&& Objects.equals(this.cancelRequestAt, cancelRequestResponse.cancelRequestAt)
				&& Objects.equals(this.cancelRequestId, cancelRequestResponse.cancelRequestId)
				&& Objects.equals(this.cancelCustomKey, cancelRequestResponse.cancelCustomKey)
				&& Objects.equals(this.status, cancelRequestResponse.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sellerId, paymentId, cancelRequestAt, cancelRequestId, cancelCustomKey, status);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CancelRequestResponse {\n");

		sb.append("    sellerId: ").append(toIndentedString(sellerId)).append("\n");
		sb.append("    paymentId: ").append(toIndentedString(paymentId)).append("\n");
		sb.append("    cancelRequestAt: ").append(toIndentedString(cancelRequestAt)).append("\n");
		sb.append("    cancelRequestId: ").append(toIndentedString(cancelRequestId)).append("\n");
		sb.append("    cancelCustomKey: ").append(toIndentedString(cancelCustomKey)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
		return this.getCancelRequestAt();
	}

	@Override
	public String getMessageField() {
		return this.getCancelRequestId();
	}

	@Override
	public String getOrderId() {
		return this.getCancelRequestId();
	}
}
