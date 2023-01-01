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
 * Conjunto de dados referentes ao comprador.
 */
public class CustomerCredit {
	@SerializedName("customer_id")
	private String customerId = null;

	@SerializedName("first_name")
	private String firstName = null;

	@SerializedName("last_name")
	private String lastName = null;

	@SerializedName("name")
	private String name = null;

	@SerializedName("email")
	private String email = null;

	@SerializedName("document_type")
	private String documentType = null;

	@SerializedName("document_number")
	private String documentNumber = null;

	@SerializedName("phone_number")
	private String phoneNumber = null;

	@SerializedName("billing_address")
	private BillingAddress billingAddress = null;

	public CustomerCredit customerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	/**
	 * Identificador do comprador.
	 * 
	 * @return customerId
	 **/
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public CustomerCredit firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * Primeiro nome do comprador.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório
	 * para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return firstName
	 **/
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public CustomerCredit lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * Último nome do comprador.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para
	 * o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return lastName
	 **/
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public CustomerCredit name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Nome completo do comprador. Obrigatório para boleto.
	 * 
	 * @return name
	 **/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomerCredit email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Email do comprador.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o
	 * &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return email
	 **/
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CustomerCredit documentType(String documentType) {
		this.documentType = documentType;
		return this;
	}

	/**
	 * Tipo do documento de identificação do comprador. Obrigatório para
	 * boleto.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return documentType
	 **/
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public CustomerCredit documentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
		return this;
	}

	/**
	 * Número do documento do comprador sem pontuação. (sem
	 * máscara)&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return documentNumber
	 **/
	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public CustomerCredit phoneNumber(String phoneNumber) {
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

	public CustomerCredit billingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
		return this;
	}

	/**
	 * Get billingAddress
	 * 
	 * @return billingAddress
	 **/
	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CustomerCredit customerCredit = (CustomerCredit) o;
		return Objects.equals(this.customerId, customerCredit.customerId)
				&& Objects.equals(this.firstName, customerCredit.firstName)
				&& Objects.equals(this.lastName, customerCredit.lastName)
				&& Objects.equals(this.name, customerCredit.name) && Objects.equals(this.email, customerCredit.email)
				&& Objects.equals(this.documentType, customerCredit.documentType)
				&& Objects.equals(this.documentNumber, customerCredit.documentNumber)
				&& Objects.equals(this.phoneNumber, customerCredit.phoneNumber)
				&& Objects.equals(this.billingAddress, customerCredit.billingAddress);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, firstName, lastName, name, email, documentType, documentNumber, phoneNumber,
				billingAddress);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CustomerCredit {\n");

		sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
		sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
		sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    documentType: ").append(toIndentedString(documentType)).append("\n");
		sb.append("    documentNumber: ").append(toIndentedString(documentNumber)).append("\n");
		sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
		sb.append("    billingAddress: ").append(toIndentedString(billingAddress)).append("\n");
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
