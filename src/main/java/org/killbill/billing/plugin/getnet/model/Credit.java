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
 * Conjunto de dados referentes a transação de crédito.
 */
public class Credit {
	@SerializedName("delayed")
	private Boolean delayed = null;

	@SerializedName("pre_authorization")
	private Boolean preAuthorization = null;

	@SerializedName("save_card_data")
	private Boolean saveCardData = null;

	/**
	 * Tipo de transação. Pagamento completo à vista, parcelado sem juros, parcelado
	 * com juros.
	 */
	@JsonAdapter(TransactionTypeEnum.Adapter.class)
	public enum TransactionTypeEnum {
		FULL("FULL"),

		INSTALL_NO_INTEREST("INSTALL_NO_INTEREST"),

		INSTALL_WITH_INTEREST("INSTALL_WITH_INTEREST");

		private String value;

		TransactionTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		public static TransactionTypeEnum fromValue(String text) {
			for (TransactionTypeEnum b : TransactionTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}

		public static class Adapter extends TypeAdapter<TransactionTypeEnum> {
			@Override
			public void write(final JsonWriter jsonWriter, final TransactionTypeEnum enumeration) throws IOException {
				jsonWriter.value(enumeration.getValue());
			}

			@Override
			public TransactionTypeEnum read(final JsonReader jsonReader) throws IOException {
				String value = jsonReader.nextString();
				return TransactionTypeEnum.fromValue(String.valueOf(value));
			}
		}
	}

	@SerializedName("transaction_type")
	private TransactionTypeEnum transactionType = null;

	@SerializedName("number_installments")
	private BigDecimal numberInstallments = null;

	@SerializedName("soft_descriptor")
	private String softDescriptor = null;

	@SerializedName("dynamic_mcc")
	private BigDecimal dynamicMcc = null;

	@SerializedName("card")
	private CardCredit card = null;

	public Credit delayed(Boolean delayed) {
		this.delayed = delayed;
		return this;
	}

	/**
	 * Identifica se o crédito será feito com confirmação tardia.
	 * 
	 * @return delayed
	 **/
	public Boolean isDelayed() {
		return delayed;
	}

	public void setDelayed(Boolean delayed) {
		this.delayed = delayed;
	}

	public Credit preAuthorization(Boolean preAuthorization) {
		this.preAuthorization = preAuthorization;
		return this;
	}

	/**
	 * Indicativo se a transação é uma pré autorização de crédito.
	 * 
	 * @return preAuthorization
	 **/
	public Boolean isPreAuthorization() {
		return preAuthorization;
	}

	public void setPreAuthorization(Boolean preAuthorization) {
		this.preAuthorization = preAuthorization;
	}

	public Credit saveCardData(Boolean saveCardData) {
		this.saveCardData = saveCardData;
		return this;
	}

	/**
	 * Identifica se o cartão deve ser salvo para futuras compras.
	 * 
	 * @return saveCardData
	 **/

	public Boolean isSaveCardData() {
		return saveCardData;
	}

	public void setSaveCardData(Boolean saveCardData) {
		this.saveCardData = saveCardData;
	}

	public Credit transactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
		return this;
	}

	/**
	 * Tipo de transação. Pagamento completo à vista, parcelado sem juros, parcelado
	 * com juros.
	 * 
	 * @return transactionType
	 **/
	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}

	public Credit numberInstallments(BigDecimal numberInstallments) {
		this.numberInstallments = numberInstallments;
		return this;
	}

	/**
	 * Número de parcelas para uma transação de crédito parcelado.
	 * 
	 * @return numberInstallments
	 **/
	public BigDecimal getNumberInstallments() {
		return numberInstallments;
	}

	public void setNumberInstallments(BigDecimal numberInstallments) {
		this.numberInstallments = numberInstallments;
	}

	public Credit softDescriptor(String softDescriptor) {
		this.softDescriptor = softDescriptor;
		return this;
	}

	/**
	 * Get softDescriptor
	 * 
	 * @return softDescriptor
	 **/
	public String getSoftDescriptor() {
		return softDescriptor;
	}

	public void setSoftDescriptor(String softDescriptor) {
		this.softDescriptor = softDescriptor;
	}

	public Credit dynamicMcc(BigDecimal dynamicMcc) {
		this.dynamicMcc = dynamicMcc;
		return this;
	}

	/**
	 * Get dynamicMcc
	 * 
	 * @return dynamicMcc
	 **/
	public BigDecimal getDynamicMcc() {
		return dynamicMcc;
	}

	public void setDynamicMcc(BigDecimal dynamicMcc) {
		this.dynamicMcc = dynamicMcc;
	}

	public Credit card(CardCredit card) {
		this.card = card;
		return this;
	}

	/**
	 * Get card
	 * 
	 * @return card
	 **/
	public CardCredit getCard() {
		return card;
	}

	public void setCard(CardCredit card) {
		this.card = card;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Credit credit = (Credit) o;
		return Objects.equals(this.delayed, credit.delayed)
				&& Objects.equals(this.preAuthorization, credit.preAuthorization)
				&& Objects.equals(this.saveCardData, credit.saveCardData)
				&& Objects.equals(this.transactionType, credit.transactionType)
				&& Objects.equals(this.numberInstallments, credit.numberInstallments)
				&& Objects.equals(this.softDescriptor, credit.softDescriptor)
				&& Objects.equals(this.dynamicMcc, credit.dynamicMcc) && Objects.equals(this.card, credit.card);
	}

	@Override
	public int hashCode() {
		return Objects.hash(delayed, preAuthorization, saveCardData, transactionType, numberInstallments,
				softDescriptor, dynamicMcc, card);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Credit {\n");

		sb.append("    delayed: ").append(toIndentedString(delayed)).append("\n");
		sb.append("    preAuthorization: ").append(toIndentedString(preAuthorization)).append("\n");
		sb.append("    saveCardData: ").append(toIndentedString(saveCardData)).append("\n");
		sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
		sb.append("    numberInstallments: ").append(toIndentedString(numberInstallments)).append("\n");
		sb.append("    softDescriptor: ").append(toIndentedString(softDescriptor)).append("\n");
		sb.append("    dynamicMcc: ").append(toIndentedString(dynamicMcc)).append("\n");
		sb.append("    card: ").append(toIndentedString(card)).append("\n");
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
