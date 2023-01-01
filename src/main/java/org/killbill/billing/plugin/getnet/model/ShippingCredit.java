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

import java.math.BigDecimal;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

/**
 * Conjunto de dados referentes ao endereço de entrega.
 */
public class ShippingCredit {
	@SerializedName("first_name")
	private String firstName = null;

	@SerializedName("name")
	private String name = null;

	@SerializedName("email")
	private String email = null;

	@SerializedName("phone_number")
	private String phoneNumber = null;

	@SerializedName("shipping_amount")
	private BigDecimal shippingAmount = null;

	@SerializedName("address")
	private AddressCredit address = null;

	public ShippingCredit firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * Primeiro nome do comprador.
	 * 
	 * @return firstName
	 **/
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public ShippingCredit name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Nome completo do comprador.
	 * 
	 * @return name
	 **/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShippingCredit email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Email do comprador.
	 * 
	 * @return email
	 **/
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ShippingCredit phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	/**
	 * Telefone do comprador. (sem máscara)&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo
	 * obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return phoneNumber
	 **/
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ShippingCredit shippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
		return this;
	}

	/**
	 * Valor do frete em centavos.
	 * 
	 * @return shippingAmount
	 **/
	public BigDecimal getShippingAmount() {
		return shippingAmount;
	}

	public void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	public ShippingCredit address(AddressCredit address) {
		this.address = address;
		return this;
	}

	/**
	 * Get address
	 * 
	 * @return address
	 **/
	public AddressCredit getAddress() {
		return address;
	}

	public void setAddress(AddressCredit address) {
		this.address = address;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ShippingCredit shippingCredit = (ShippingCredit) o;
		return Objects.equals(this.firstName, shippingCredit.firstName)
				&& Objects.equals(this.name, shippingCredit.name) && Objects.equals(this.email, shippingCredit.email)
				&& Objects.equals(this.phoneNumber, shippingCredit.phoneNumber)
				&& Objects.equals(this.shippingAmount, shippingCredit.shippingAmount)
				&& Objects.equals(this.address, shippingCredit.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, name, email, phoneNumber, shippingAmount, address);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ShippingCredit {\n");

		sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
		sb.append("    shippingAmount: ").append(toIndentedString(shippingAmount)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
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
