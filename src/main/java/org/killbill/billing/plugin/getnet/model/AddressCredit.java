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
 * Conjunto de dados referentes ao endereço do comprador
 */
public class AddressCredit {
	@SerializedName("street")
	private String street = null;

	@SerializedName("number")
	private String number = null;

	@SerializedName("complement")
	private String complement = null;

	@SerializedName("district")
	private String district = null;

	@SerializedName("city")
	private String city = null;

	@SerializedName("state")
	private String state = null;

	@SerializedName("country")
	private String country = null;

	@SerializedName("postal_code")
	private String postalCode = null;

	public AddressCredit street(String street) {
		this.street = street;
		return this;
	}

	/**
	 * Logradouro.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return street
	 **/
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public AddressCredit number(String number) {
		this.number = number;
		return this;
	}

	/**
	 * Número do logradouro.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o
	 * &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return number
	 **/
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public AddressCredit complement(String complement) {
		this.complement = complement;
		return this;
	}

	/**
	 * Complemento do logradouro.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para
	 * o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return complement
	 **/
	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public AddressCredit district(String district) {
		this.district = district;
		return this;
	}

	/**
	 * Bairro.
	 * 
	 * @return district
	 **/
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public AddressCredit city(String city) {
		this.city = city;
		return this;
	}

	/**
	 * Cidade.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return city
	 **/
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public AddressCredit state(String state) {
		this.state = state;
		return this;
	}

	/**
	 * Estado (UF).&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return state
	 **/
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public AddressCredit country(String country) {
		this.country = country;
		return this;
	}

	/**
	 * País.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return country
	 **/
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public AddressCredit postalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	/**
	 * Código Postal, CEP no Brasil ou ZIP nos Estados Unidos. (sem
	 * máscara)&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return postalCode
	 **/
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AddressCredit addressCredit = (AddressCredit) o;
		return Objects.equals(this.street, addressCredit.street) && Objects.equals(this.number, addressCredit.number)
				&& Objects.equals(this.complement, addressCredit.complement)
				&& Objects.equals(this.district, addressCredit.district)
				&& Objects.equals(this.city, addressCredit.city) && Objects.equals(this.state, addressCredit.state)
				&& Objects.equals(this.country, addressCredit.country)
				&& Objects.equals(this.postalCode, addressCredit.postalCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(street, number, complement, district, city, state, country, postalCode);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AddressCredit {\n");

		sb.append("    street: ").append(toIndentedString(street)).append("\n");
		sb.append("    number: ").append(toIndentedString(number)).append("\n");
		sb.append("    complement: ").append(toIndentedString(complement)).append("\n");
		sb.append("    district: ").append(toIndentedString(district)).append("\n");
		sb.append("    city: ").append(toIndentedString(city)).append("\n");
		sb.append("    state: ").append(toIndentedString(state)).append("\n");
		sb.append("    country: ").append(toIndentedString(country)).append("\n");
		sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
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
