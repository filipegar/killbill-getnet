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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Conjunto de dados referentes a transação de pagamento
 */
public class PaymentCredit {
	@SerializedName("seller_id")
	private String sellerId = null;

	@SerializedName("amount")
	private Integer amount = null;

	@SerializedName("currency")
	private String currency = null;

	@SerializedName("order")
	private Order order = null;

	@SerializedName("customer")
	private CustomerCredit customer = null;

	@SerializedName("shippings")
	private List<ShippingCredit> shippings = null;

	@SerializedName("credit")
	private Credit credit = null;

	public PaymentCredit sellerId(String sellerId) {
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

	public PaymentCredit amount(Integer amount) {
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

	public PaymentCredit currency(String currency) {
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

	public PaymentCredit order(Order order) {
		this.order = order;
		return this;
	}

	/**
	 * Get order
	 * 
	 * @return order
	 **/
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public PaymentCredit customer(CustomerCredit customer) {
		this.customer = customer;
		return this;
	}

	/**
	 * Get customer
	 * 
	 * @return customer
	 **/
	public CustomerCredit getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerCredit customer) {
		this.customer = customer;
	}

	public PaymentCredit shippings(List<ShippingCredit> shippings) {
		this.shippings = shippings;
		return this;
	}

	public PaymentCredit addShippingsItem(ShippingCredit shippingsItem) {
		if (this.shippings == null) {
			this.shippings = new ArrayList<ShippingCredit>();
		}
		this.shippings.add(shippingsItem);
		return this;
	}

	/**
	 * Get shippings
	 * 
	 * @return shippings
	 **/
	public List<ShippingCredit> getShippings() {
		return shippings;
	}

	public void setShippings(List<ShippingCredit> shippings) {
		this.shippings = shippings;
	}

	public PaymentCredit credit(Credit credit) {
		this.credit = credit;
		return this;
	}

	/**
	 * Get credit
	 * 
	 * @return credit
	 **/
	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
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
		PaymentCredit paymentCredit = (PaymentCredit) o;
		return Objects.equals(this.sellerId, paymentCredit.sellerId)
				&& Objects.equals(this.amount, paymentCredit.amount)
				&& Objects.equals(this.currency, paymentCredit.currency)
				&& Objects.equals(this.order, paymentCredit.order)
				&& Objects.equals(this.customer, paymentCredit.customer)
				&& Objects.equals(this.shippings, paymentCredit.shippings)
				&& Objects.equals(this.credit, paymentCredit.credit);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sellerId, amount, currency, order, customer, shippings, credit);
	}

	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
