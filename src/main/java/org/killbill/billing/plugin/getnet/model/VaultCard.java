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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class VaultCard {
	@SerializedName("number_token")
	private String numberToken = null;

	@SerializedName("brand")
	private String brand = null;

	@SerializedName("cardholder_name")
	private String cardholderName = null;

	@SerializedName("expiration_month")
	private String expirationMonth = null;

	@SerializedName("expiration_year")
	private String expirationYear = null;

	@SerializedName("customer_id")
	private String customerId = null;

	@SerializedName("cardholder_identification")
	private String cardholderIdentification = null;

	@SerializedName("verify_card")
	private Boolean verifyCard = false;

	@SerializedName("security_code")
	private String securityCode = null;

	public VaultCard numberToken(String numberToken) {
		this.numberToken = numberToken;
		return this;
	}

	/**
	 * Número do cartão tokenizado. Gerado por meio do endpoint /v1/tokens/card.
	 * 
	 * @return numberToken
	 **/
	public String getNumberToken() {
		return numberToken;
	}

	public void setNumberToken(String numberToken) {
		this.numberToken = numberToken;
	}

	public VaultCard brand(String brand) {
		this.brand = brand;
		return this;
	}

	/**
	 * Bandeira do cartão. Preenchido automaticamente pela API caso não seja
	 * informado.
	 * 
	 * @return brand
	 **/
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public VaultCard cardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
		return this;
	}

	/**
	 * Nome do comprador impresso no cartão.
	 * 
	 * @return cardholderName
	 **/
	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public VaultCard expirationMonth(String expirationMonth) {
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

	public VaultCard expirationYear(String expirationYear) {
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

	public VaultCard customerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	/**
	 * Identificador do comprador. Informação definida pela aplicação que consome a
	 * api da Plataforma Digital. É permitido neste campo somente dados
	 * alfanuméricos. Os seguintes caracteres especiais não devem ser
	 * utilizados&amp;#58; % $ , . / &amp; ( ) + &#x3D; &#x3D; &lt; &gt; - *
	 * 
	 * @return customerId
	 **/
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public VaultCard cardholderIdentification(String cardholderIdentification) {
		this.cardholderIdentification = cardholderIdentification;
		return this;
	}

	/**
	 * Identificação do comprador, como CPF ou RG. (sem máscara)
	 * 
	 * @return cardholderIdentification
	 **/
	public String getCardholderIdentification() {
		return cardholderIdentification;
	}

	public void setCardholderIdentification(String cardholderIdentification) {
		this.cardholderIdentification = cardholderIdentification;
	}

	public VaultCard verifyCard(Boolean verifyCard) {
		this.verifyCard = verifyCard;
		return this;
	}

	/**
	 * Realiza uma transação que Verifica se o cartão informado, não está cancelado,
	 * bloqueado ou com restrições.
	 * 
	 * @return verifyCard
	 **/
	public Boolean isVerifyCard() {
		return verifyCard;
	}

	public void setVerifyCard(Boolean verifyCard) {
		this.verifyCard = verifyCard;
	}

	public VaultCard securityCode(String securityCode) {
		this.securityCode = securityCode;
		return this;
	}

	/**
	 * Código de segurança. CVV ou CVC.
	 * 
	 * @return securityCode
	 **/
	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		VaultCard vaultCard = (VaultCard) o;
		return Objects.equals(this.numberToken, vaultCard.numberToken) && Objects.equals(this.brand, vaultCard.brand)
				&& Objects.equals(this.cardholderName, vaultCard.cardholderName)
				&& Objects.equals(this.expirationMonth, vaultCard.expirationMonth)
				&& Objects.equals(this.expirationYear, vaultCard.expirationYear)
				&& Objects.equals(this.customerId, vaultCard.customerId)
				&& Objects.equals(this.cardholderIdentification, vaultCard.cardholderIdentification)
				&& Objects.equals(this.verifyCard, vaultCard.verifyCard)
				&& Objects.equals(this.securityCode, vaultCard.securityCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberToken, brand, cardholderName, expirationMonth, expirationYear, customerId,
				cardholderIdentification, verifyCard, securityCode);
	}

	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
