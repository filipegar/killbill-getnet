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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Conjunto de dados para identificação da compra
 */
public class Order {
	@SerializedName("order_id")
	private String orderId = null;

	@SerializedName("sales_tax")
	private BigDecimal salesTax = null;

	/**
	 * Identificador do tipo de produto vendido dentre as opções
	 */
	@JsonAdapter(ProductTypeEnum.Adapter.class)
	public enum ProductTypeEnum {
		CASH_CARRY("cash_carry"),

		DIGITAL_CONTENT("digital_content"),

		DIGITAL_GOODS("digital_goods"),

		DIGITAL_PHYSICAL("digital_physical"),

		GIFT_CARD("gift_card"),

		PHYSICAL_GOODS("physical_goods"),

		RENEW_SUBS("renew_subs"),

		SHAREWARE("shareware"),

		SERVICE("service");

		private String value;

		ProductTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		public static ProductTypeEnum fromValue(String text) {
			for (ProductTypeEnum b : ProductTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}

		public static class Adapter extends TypeAdapter<ProductTypeEnum> {
			@Override
			public void write(final JsonWriter jsonWriter, final ProductTypeEnum enumeration) throws IOException {
				jsonWriter.value(enumeration.getValue());
			}

			@Override
			public ProductTypeEnum read(final JsonReader jsonReader) throws IOException {
				String value = jsonReader.nextString();
				return ProductTypeEnum.fromValue(String.valueOf(value));
			}
		}
	}

	@SerializedName("product_type")
	private ProductTypeEnum productType = null;

	public Order orderId(String orderId) {
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

	public Order salesTax(BigDecimal salesTax) {
		this.salesTax = salesTax;
		return this;
	}

	/**
	 * Valor de impostos
	 * 
	 * @return salesTax
	 **/
	public BigDecimal getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(BigDecimal salesTax) {
		this.salesTax = salesTax;
	}

	public Order productType(ProductTypeEnum productType) {
		this.productType = productType;
		return this;
	}

	/**
	 * Identificador do tipo de produto vendido dentre as opções
	 * 
	 * @return productType
	 **/
	public ProductTypeEnum getProductType() {
		return productType;
	}

	public void setProductType(ProductTypeEnum productType) {
		this.productType = productType;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Order order = (Order) o;
		return Objects.equals(this.orderId, order.orderId) && Objects.equals(this.salesTax, order.salesTax)
				&& Objects.equals(this.productType, order.productType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, salesTax, productType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Order {\n");

		sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
		sb.append("    salesTax: ").append(toIndentedString(salesTax)).append("\n");
		sb.append("    productType: ").append(toIndentedString(productType)).append("\n");
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
