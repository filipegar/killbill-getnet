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

import com.google.gson.annotations.SerializedName;

public class PaymentResponseBase {

	@SerializedName("payment_id")
	public String paymentId = null;

	@SerializedName("seller_id")
	public String sellerId = null;

	@SerializedName("amount")
	public Integer amount = null;

	@SerializedName("currency")
	public String currency = null;

	@SerializedName("order_id")
	public String orderId = null;

	@SerializedName("status")
	public String status = null;

	/**
	 * Código de identificação do pagamento.
	 * 
	 * @return paymentId
	 **/
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public PaymentResponseBase sellerId(String sellerId) {
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

	public PaymentResponseBase amount(Integer amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Valor da compra em centavos.
	 * 
	 * @return amount
	 **/
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public PaymentResponseBase currency(String currency) {
		this.currency = currency;
		return this;
	}

	/**
	 * Identificação da moeda (Consultar código em
	 * \&quot;https://www.currency-iso.org/en/home/tables/table-a1.html\&quot;).
	 * 
	 * @return currency
	 **/
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public PaymentResponseBase orderId(String orderId) {
		this.orderId = orderId;
		return this;
	}

	/**
	 * Código de identificação da compra utilizado pelo e-commerce
	 * 
	 * @return orderId
	 **/
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public PaymentResponseBase status(String status) {
		this.status = status;
		return this;
	}

	/**
	 * Status da transação. Para mais informações sobre os status de retorno, veja
	 * em &lt;a href&#x3D;\&quot;api#tag/Notificacoes-1.0\&quot;&gt;Notificações
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

}
