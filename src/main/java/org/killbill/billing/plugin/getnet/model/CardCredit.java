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
 * Conjunto de dados do cartão.
 */
public class CardCredit {
	@SerializedName("number_token")
	private String numberToken = null;

	@SerializedName("cardholder_name")
	private String cardholderName = null;

	@SerializedName("security_code")
	private String securityCode = null;

	@SerializedName("brand")
	private String brand = null;

	@SerializedName("expiration_month")
	private String expirationMonth = null;

	@SerializedName("expiration_year")
	private String expirationYear = null;

	public CardCredit numberToken(String numberToken) {
		this.numberToken = numberToken;
		return this;
	}

	/**
	 * Número do cartão tokenizado.
	 * 
	 * @return numberToken
	 **/
	public String getNumberToken() {
		return numberToken;
	}

	public void setNumberToken(String numberToken) {
		this.numberToken = numberToken;
	}

	public CardCredit cardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
		return this;
	}

	/**
	 * Nome do comprador impresso no cartão.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo
	 * obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return cardholderName
	 **/
	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public CardCredit securityCode(String securityCode) {
		this.securityCode = securityCode;
		return this;
	}

	/**
	 * Código de segurança. CVV ou CVC.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo
	 * obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return securityCode
	 **/
	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public CardCredit brand(String brand) {
		this.brand = brand;
		return this;
	}

	/**
	 * Bandeira do cartão. Preenchido automaticamente pela API caso não seja
	 * informado.&lt;/br&gt;&lt;b&gt;&lt;i&gt;Campo obrigatório para o &lt;a
	 * href&#x3D;\&quot;https://developers.getnet.com.br/api#section/Antifraude/Dados-obrigatorios-para-analise-do-Antifraude\&quot;&gt;Antifraude&lt;/a&gt;&lt;/i&gt;&lt;/b&gt;
	 * 
	 * @return brand
	 **/
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public CardCredit expirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
		return this;
	}

	/**
	 * Mês de expiração do cartão com dois dígitos.
	 * 
	 * @return expirationMonth
	 **/
	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public CardCredit expirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
		return this;
	}

	/**
	 * Ano de expiração do cartão com dois dígitos.
	 * 
	 * @return expirationYear
	 **/
	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CardCredit cardCredit = (CardCredit) o;
		return Objects.equals(this.numberToken, cardCredit.numberToken)
				&& Objects.equals(this.cardholderName, cardCredit.cardholderName)
				&& Objects.equals(this.securityCode, cardCredit.securityCode)
				&& Objects.equals(this.brand, cardCredit.brand)
				&& Objects.equals(this.expirationMonth, cardCredit.expirationMonth)
				&& Objects.equals(this.expirationYear, cardCredit.expirationYear);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberToken, cardholderName, securityCode, brand, expirationMonth, expirationYear);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CardCredit {\n");

		sb.append("    numberToken: ").append(toIndentedString(numberToken)).append("\n");
		sb.append("    cardholderName: ").append(toIndentedString(cardholderName)).append("\n");
		sb.append("    securityCode: ").append(toIndentedString(securityCode)).append("\n");
		sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
		sb.append("    expirationMonth: ").append(toIndentedString(expirationMonth)).append("\n");
		sb.append("    expirationYear: ").append(toIndentedString(expirationYear)).append("\n");
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
