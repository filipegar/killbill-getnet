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
 * Conjunto de dados referentes a transação.
 */
public class PaymentCreditDetail {
	@SerializedName("delayed")
	public Boolean delayed = null;

	@SerializedName("authorization_code")
	public String authorizationCode = null;

	@SerializedName("authorized_at")
	public BigDecimal authorizedAt = null;

	@SerializedName("reason_code")
	public String reasonCode = null;

	@SerializedName("reason_message")
	public String reasonMessage = null;

	@SerializedName("acquirer")
	public String acquirer = null;

	@SerializedName("soft_descriptor")
	public String softDescriptor = null;

	@SerializedName("brand")
	public String brand = null;

	@SerializedName("terminal_nsu")
	public String terminalNsu = null;

	@SerializedName("acquirer_transaction_id")
	public String acquirerTransactionId = null;

	@SerializedName("transaction_id")
	public String transactionId = null;

	@SerializedName("first_installment_amount")
	public String firstInstallmentAmount = null;

	@SerializedName("other_installment_amount")
	public String otherInstallmentAmount = null;

	@SerializedName("total_installment_amount")
	public String totalInstallmentAmount = null;

	public PaymentCreditDetail delayed(Boolean delayed) {
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

	public PaymentCreditDetail authorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
		return this;
	}

	/**
	 * Código interno da transação.
	 * 
	 * @return authorizationCode
	 **/
	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public PaymentCreditDetail authorizedAt(BigDecimal authorizedAt) {
		this.authorizedAt = authorizedAt;
		return this;
	}

	/**
	 * Data/hora da autorização de crédito.
	 * 
	 * @return authorizedAt
	 **/
	public BigDecimal getAuthorizedAt() {
		return authorizedAt;
	}

	public void setAuthorizedAt(BigDecimal authorizedAt) {
		this.authorizedAt = authorizedAt;
	}

	public PaymentCreditDetail reasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
		return this;
	}

	/**
	 * Código de retorno.
	 * 
	 * @return reasonCode
	 **/
	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public PaymentCreditDetail reasonMessage(String reasonMessage) {
		this.reasonMessage = reasonMessage;
		return this;
	}

	/**
	 * Mensagem de retorno.
	 * 
	 * @return reasonMessage
	 **/
	public String getReasonMessage() {
		return reasonMessage;
	}

	public void setReasonMessage(String reasonMessage) {
		this.reasonMessage = reasonMessage;
	}

	public PaymentCreditDetail acquirer(String acquirer) {
		this.acquirer = acquirer;
		return this;
	}

	/**
	 * Nome do adquirente.
	 * 
	 * @return acquirer
	 **/
	public String getAcquirer() {
		return acquirer;
	}

	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}

	public PaymentCreditDetail softDescriptor(String softDescriptor) {
		this.softDescriptor = softDescriptor;
		return this;
	}

	/**
	 * Descrição para a fatura do cartão.
	 * 
	 * @return softDescriptor
	 **/
	public String getSoftDescriptor() {
		return softDescriptor;
	}

	public void setSoftDescriptor(String softDescriptor) {
		this.softDescriptor = softDescriptor;
	}

	public PaymentCreditDetail brand(String brand) {
		this.brand = brand;
		return this;
	}

	/**
	 * Bandeira resposável pelo o processamento da transação.
	 * 
	 * @return brand
	 **/
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public PaymentCreditDetail terminalNsu(String terminalNsu) {
		this.terminalNsu = terminalNsu;
		return this;
	}

	/**
	 * Código de Autorização gerado pelo Emissor quando a transação é realizada com
	 * sucesso.
	 * 
	 * @return terminalNsu
	 **/
	public String getTerminalNsu() {
		return terminalNsu;
	}

	public void setTerminalNsu(String terminalNsu) {
		this.terminalNsu = terminalNsu;
	}

	public PaymentCreditDetail acquirerTransactionId(String acquirerTransactionId) {
		this.acquirerTransactionId = acquirerTransactionId;
		return this;
	}

	/**
	 * Código de transação do adquirente.
	 * 
	 * @return acquirerTransactionId
	 **/
	public String getAcquirerTransactionId() {
		return acquirerTransactionId;
	}

	public void setAcquirerTransactionId(String acquirerTransactionId) {
		this.acquirerTransactionId = acquirerTransactionId;
	}

	public PaymentCreditDetail transactionId(String transactionId) {
		this.transactionId = transactionId;
		return this;
	}

	/**
	 * Código de transação.
	 * 
	 * @return transactionId
	 **/
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PaymentCreditDetail firstInstallmentAmount(String firstInstallmentAmount) {
		this.firstInstallmentAmount = firstInstallmentAmount;
		return this;
	}

	/**
	 * Valor da primeira parcela.
	 * 
	 * @return firstInstallmentAmount
	 **/
	public String getFirstInstallmentAmount() {
		return firstInstallmentAmount;
	}

	public void setFirstInstallmentAmount(String firstInstallmentAmount) {
		this.firstInstallmentAmount = firstInstallmentAmount;
	}

	public PaymentCreditDetail otherInstallmentAmount(String otherInstallmentAmount) {
		this.otherInstallmentAmount = otherInstallmentAmount;
		return this;
	}

	/**
	 * Outro valor da parcela.
	 * 
	 * @return otherInstallmentAmount
	 **/
	public String getOtherInstallmentAmount() {
		return otherInstallmentAmount;
	}

	public void setOtherInstallmentAmount(String otherInstallmentAmount) {
		this.otherInstallmentAmount = otherInstallmentAmount;
	}

	public PaymentCreditDetail totalInstallmentAmount(String totalInstallmentAmount) {
		this.totalInstallmentAmount = totalInstallmentAmount;
		return this;
	}

	/**
	 * Valor total da parcela.
	 * 
	 * @return totalInstallmentAmount
	 **/
	public String getTotalInstallmentAmount() {
		return totalInstallmentAmount;
	}

	public void setTotalInstallmentAmount(String totalInstallmentAmount) {
		this.totalInstallmentAmount = totalInstallmentAmount;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PaymentCreditDetail paymentCreditDetail = (PaymentCreditDetail) o;
		return Objects.equals(this.delayed, paymentCreditDetail.delayed)
				&& Objects.equals(this.authorizationCode, paymentCreditDetail.authorizationCode)
				&& Objects.equals(this.authorizedAt, paymentCreditDetail.authorizedAt)
				&& Objects.equals(this.reasonCode, paymentCreditDetail.reasonCode)
				&& Objects.equals(this.reasonMessage, paymentCreditDetail.reasonMessage)
				&& Objects.equals(this.acquirer, paymentCreditDetail.acquirer)
				&& Objects.equals(this.softDescriptor, paymentCreditDetail.softDescriptor)
				&& Objects.equals(this.brand, paymentCreditDetail.brand)
				&& Objects.equals(this.terminalNsu, paymentCreditDetail.terminalNsu)
				&& Objects.equals(this.acquirerTransactionId, paymentCreditDetail.acquirerTransactionId)
				&& Objects.equals(this.transactionId, paymentCreditDetail.transactionId)
				&& Objects.equals(this.firstInstallmentAmount, paymentCreditDetail.firstInstallmentAmount)
				&& Objects.equals(this.otherInstallmentAmount, paymentCreditDetail.otherInstallmentAmount)
				&& Objects.equals(this.totalInstallmentAmount, paymentCreditDetail.totalInstallmentAmount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(delayed, authorizationCode, authorizedAt, reasonCode, reasonMessage, acquirer,
				softDescriptor, brand, terminalNsu, acquirerTransactionId, transactionId, firstInstallmentAmount,
				otherInstallmentAmount, totalInstallmentAmount);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PaymentCreditDetail {\n");

		sb.append("    delayed: ").append(toIndentedString(delayed)).append("\n");
		sb.append("    authorizationCode: ").append(toIndentedString(authorizationCode)).append("\n");
		sb.append("    authorizedAt: ").append(toIndentedString(authorizedAt)).append("\n");
		sb.append("    reasonCode: ").append(toIndentedString(reasonCode)).append("\n");
		sb.append("    reasonMessage: ").append(toIndentedString(reasonMessage)).append("\n");
		sb.append("    acquirer: ").append(toIndentedString(acquirer)).append("\n");
		sb.append("    softDescriptor: ").append(toIndentedString(softDescriptor)).append("\n");
		sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
		sb.append("    terminalNsu: ").append(toIndentedString(terminalNsu)).append("\n");
		sb.append("    acquirerTransactionId: ").append(toIndentedString(acquirerTransactionId)).append("\n");
		sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
		sb.append("    firstInstallmentAmount: ").append(toIndentedString(firstInstallmentAmount)).append("\n");
		sb.append("    otherInstallmentAmount: ").append(toIndentedString(otherInstallmentAmount)).append("\n");
		sb.append("    totalInstallmentAmount: ").append(toIndentedString(totalInstallmentAmount)).append("\n");
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
