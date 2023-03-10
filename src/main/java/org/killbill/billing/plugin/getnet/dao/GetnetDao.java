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
package org.killbill.billing.plugin.getnet.dao;

import static org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPaymentMethods.GETNET_PAYMENT_METHODS;
import static org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPayments.GETNET_PAYMENTS;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.plugin.dao.payment.PluginPaymentDao;
import org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPaymentMethods;
import org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPayments;
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentMethodsRecord;
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentsRecord;
import org.killbill.billing.plugin.getnet.model.PaymentCreditResponse;
import org.killbill.billing.plugin.getnet.model.PaymentOperation;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class GetnetDao extends
		PluginPaymentDao<GetnetPaymentsRecord, GetnetPayments, GetnetPaymentMethodsRecord, GetnetPaymentMethods> {

	public GetnetDao(final DataSource dataSource) throws SQLException {
		super(GETNET_PAYMENTS, GETNET_PAYMENT_METHODS, dataSource);
		// Save space in the database
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	// Responses
	public GetnetPaymentsRecord addResponse(final UUID kbAccountId, final UUID kbPaymentId,
			final UUID kbPaymentTransactionId, final TransactionType transactionType, final BigDecimal amount,
			final Currency currency, final PaymentCreditResponse getnetPayment, final UUID kbTenantId)
			throws SQLException {

		return execute(dataSource.getConnection(),
				conn -> DSL.using(conn, dialect, settings).transactionResult(configuration -> {
					final DSLContext dslContext = DSL.using(configuration);
					dslContext.insertInto(GETNET_PAYMENTS, GETNET_PAYMENTS.KB_ACCOUNT_ID, GETNET_PAYMENTS.KB_PAYMENT_ID,
							GETNET_PAYMENTS.KB_PAYMENT_TRANSACTION_ID, GETNET_PAYMENTS.TRANSACTION_TYPE,
							GETNET_PAYMENTS.AMOUNT, GETNET_PAYMENTS.CURRENCY, GETNET_PAYMENTS.GETNET_PAYMENT_ID,
							GETNET_PAYMENTS.SELLER_ID, GETNET_PAYMENTS.ORDER_ID, GETNET_PAYMENTS.GETNET_STATUS,
							GETNET_PAYMENTS.RECEIVED_AT, GETNET_PAYMENTS.AUTHORIZATION_CODE,
							GETNET_PAYMENTS.AUTHORIZED_AT, GETNET_PAYMENTS.REASON_CODE, GETNET_PAYMENTS.REASON_MESSAGE,
							GETNET_PAYMENTS.SOFT_DESCRIPTOR, GETNET_PAYMENTS.BRAND, GETNET_PAYMENTS.TERMINAL_NSU,
							GETNET_PAYMENTS.ACQUIRER_TRANSACTION_ID, GETNET_PAYMENTS.TRANSACTION_ID,
							GETNET_PAYMENTS.CREATED_DATE, GETNET_PAYMENTS.KB_TENANT_ID)
							.values(kbAccountId.toString(), kbPaymentId.toString(), kbPaymentTransactionId.toString(),
									transactionType.toString(), amount, currency == null ? null : currency.name(),
									getnetPayment.getPaymentId(), getnetPayment.getSellerId(),
									getnetPayment.getOrderId(), getnetPayment.getStatus(),
									toLocalDateTime(DateTime.parse(getnetPayment.getReceivedAt())),
									getnetPayment.getCredit().getAuthorizationCode(),
									toLocalDateTime(DateTime.parse(getnetPayment.getCredit().getAuthorizedAt())),
									getnetPayment.getCredit().getReasonCode(),
									getnetPayment.getCredit().getReasonMessage(),
									getnetPayment.getCredit().getSoftDescriptor(), getnetPayment.getCredit().getBrand(),
									getnetPayment.getCredit().getTerminalNsu(),
									getnetPayment.getCredit().getAcquirerTransactionId(),
									getnetPayment.getCredit().getTransactionId(), toLocalDateTime(new DateTime()),
									kbTenantId.toString())
							.execute();
					return dslContext.fetchOne(GETNET_PAYMENTS, GETNET_PAYMENTS.RECORD_ID
							.eq(GETNET_PAYMENTS.RECORD_ID.getDataType().convert(dslContext.lastID())));
				}));
	}

	public GetnetPaymentsRecord addResponseGeneric(final UUID kbAccountId, final UUID kbPaymentId,
			final UUID kbPaymentTransactionId, final TransactionType transactionType, final BigDecimal amount,
			final Currency currency, final PaymentOperation getnetPayment, final UUID kbTenantId,
			GetnetPaymentsRecord originalTransaction) throws SQLException {

		return execute(dataSource.getConnection(),
				conn -> DSL.using(conn, dialect, settings).transactionResult(configuration -> {
					final DSLContext dslContext = DSL.using(configuration);
					dslContext.insertInto(GETNET_PAYMENTS, GETNET_PAYMENTS.KB_ACCOUNT_ID, GETNET_PAYMENTS.KB_PAYMENT_ID,
							GETNET_PAYMENTS.KB_PAYMENT_TRANSACTION_ID, GETNET_PAYMENTS.TRANSACTION_TYPE,
							GETNET_PAYMENTS.AMOUNT, GETNET_PAYMENTS.CURRENCY, GETNET_PAYMENTS.GETNET_PAYMENT_ID,
							GETNET_PAYMENTS.SELLER_ID, GETNET_PAYMENTS.ORDER_ID, GETNET_PAYMENTS.GETNET_STATUS,
							GETNET_PAYMENTS.RECEIVED_AT, GETNET_PAYMENTS.AUTHORIZATION_CODE,
							GETNET_PAYMENTS.AUTHORIZED_AT, GETNET_PAYMENTS.REASON_CODE, GETNET_PAYMENTS.REASON_MESSAGE,
							GETNET_PAYMENTS.SOFT_DESCRIPTOR, GETNET_PAYMENTS.BRAND, GETNET_PAYMENTS.TERMINAL_NSU,
							GETNET_PAYMENTS.ACQUIRER_TRANSACTION_ID, GETNET_PAYMENTS.TRANSACTION_ID,
							GETNET_PAYMENTS.CREATED_DATE, GETNET_PAYMENTS.KB_TENANT_ID)
							.values(kbAccountId.toString(), kbPaymentId.toString(), kbPaymentTransactionId.toString(),
									transactionType.toString(), amount, currency == null ? null : currency.name(),
									getnetPayment.getPaymentId(), getnetPayment.getSellerId(),
									getnetPayment.getOrderId(), getnetPayment.getStatus(),
									toLocalDateTime(DateTime.parse(getnetPayment.getDateField())),
									originalTransaction.getAuthorizationCode(), originalTransaction.getAuthorizedAt(),
									originalTransaction.getReasonCode(), getnetPayment.getMessageField(),
									originalTransaction.getSoftDescriptor(), originalTransaction.getBrand(),
									originalTransaction.getTerminalNsu(),
									originalTransaction.getAcquirerTransactionId(),
									originalTransaction.getTransactionId(), toLocalDateTime(new DateTime()),
									kbTenantId.toString())
							.execute();
					return dslContext.fetchOne(GETNET_PAYMENTS, GETNET_PAYMENTS.RECORD_ID
							.eq(GETNET_PAYMENTS.RECORD_ID.getDataType().convert(dslContext.lastID())));
				}));
	}

	@Override
	public GetnetPaymentsRecord getSuccessfulAuthorizationResponse(final UUID kbPaymentId, final UUID kbTenantId)
			throws SQLException {
		return execute(dataSource.getConnection(), new WithConnectionCallback<GetnetPaymentsRecord>() {
			@Override
			public GetnetPaymentsRecord withConnection(final Connection conn) throws SQLException {
				return DSL.using(conn, dialect, settings).selectFrom(responsesTable)
						.where(DSL.field(responsesTable.getName() + "." + KB_PAYMENT_ID).equal(kbPaymentId.toString()))
						.and(DSL.field(responsesTable.getName() + "." + KB_TENANT_ID).equal(kbTenantId.toString()))
						.orderBy(DSL.field(responsesTable.getName() + "." + RECORD_ID).desc()).limit(1).fetchOne();
			}
		});
	}

	@Override
	public void addPaymentMethod(final UUID kbAccountId, final UUID kbPaymentMethodId, final boolean isDefault,
			final Map<String, String> properties, final DateTime utcNow, final UUID kbTenantId) throws SQLException {

		execute(dataSource.getConnection(), new WithConnectionCallback<GetnetPaymentMethodsRecord>() {
			@Override
			public GetnetPaymentMethodsRecord withConnection(final Connection conn) throws SQLException {
				DSL.using(conn, dialect, settings)
						.insertInto(GETNET_PAYMENT_METHODS, GETNET_PAYMENT_METHODS.KB_ACCOUNT_ID,
								GETNET_PAYMENT_METHODS.KB_PAYMENT_METHOD_ID, GETNET_PAYMENT_METHODS.GETNET_CARD_ID,
								GETNET_PAYMENT_METHODS.IS_DEFAULT, GETNET_PAYMENT_METHODS.IS_DELETED,
								GETNET_PAYMENT_METHODS.CREATED_DATE, GETNET_PAYMENT_METHODS.UPDATED_DATE,
								GETNET_PAYMENT_METHODS.KB_TENANT_ID)
						.values(kbAccountId.toString(), kbPaymentMethodId.toString(), (String) properties.get("token"),
								(short) (isDefault ? TRUE : FALSE), (short) FALSE, toLocalDateTime(utcNow),
								toLocalDateTime(utcNow), kbTenantId.toString())
						.execute();

				return null;
			}
		});
	}

	public void updatePaymentMethod(final UUID kbAccountId, final UUID kbPaymentMethodId,
			final Map<String, Object> properties, final DateTime utcNow, final UUID kbTenantId, String cardId)
			throws SQLException {
		execute(dataSource.getConnection(), new WithConnectionCallback<GetnetPaymentMethodsRecord>() {
			@Override
			public GetnetPaymentMethodsRecord withConnection(final Connection conn) throws SQLException {
				DSL.using(conn, dialect, settings).update(GETNET_PAYMENT_METHODS)
						.set(GETNET_PAYMENT_METHODS.IS_DEFAULT,
								(Boolean) properties.get("isDefault") ? (short) TRUE : (short) FALSE)
						.set(GETNET_PAYMENT_METHODS.IS_DELETED,
								(Boolean) properties.get("isDeleted") ? (short) TRUE : (short) FALSE)
						.set(GETNET_PAYMENT_METHODS.UPDATED_DATE, toLocalDateTime(utcNow))
						.where(GETNET_PAYMENT_METHODS.KB_PAYMENT_METHOD_ID.equal(kbPaymentMethodId.toString()))
						.and(GETNET_PAYMENT_METHODS.GETNET_CARD_ID.equal(cardId))
						.and(GETNET_PAYMENT_METHODS.KB_TENANT_ID.equal(kbTenantId.toString()))
						.and(GETNET_PAYMENT_METHODS.KB_ACCOUNT_ID.equal(kbAccountId.toString())).execute();
				return null;
			}
		});
	}

	public void markAllNotDefaultCards(final UUID kbAccountId, final UUID kbTenantId, final DateTime utcNow)
			throws SQLException {
		execute(dataSource.getConnection(), new WithConnectionCallback<GetnetPaymentMethodsRecord>() {
			@Override
			public GetnetPaymentMethodsRecord withConnection(final Connection conn) throws SQLException {
				DSL.using(conn, dialect, settings).update(GETNET_PAYMENT_METHODS)
						.set(GETNET_PAYMENT_METHODS.IS_DEFAULT, (short) FALSE)
						.set(GETNET_PAYMENT_METHODS.UPDATED_DATE, toLocalDateTime(utcNow))
						.where(GETNET_PAYMENT_METHODS.KB_TENANT_ID.equal(kbTenantId.toString()))
						.and(GETNET_PAYMENT_METHODS.KB_ACCOUNT_ID.equal(kbAccountId.toString())).execute();
				return null;
			}
		});
	}
}