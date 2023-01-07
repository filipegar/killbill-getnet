/*
 * Copyright 2020-2020 Equinix, Inc
 * Copyright 2014-2020 The Billing Project, LLC
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

package org.killbill.billing.plugin.getnet.dao;

import static org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPayments.GETNET_PAYMENTS;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.plugin.dao.payment.PluginPaymentDao;
import org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPayments;
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentsRecord;
import org.killbill.billing.plugin.getnet.model.PaymentCreditDelayedConfirmResponse;
import org.killbill.billing.plugin.getnet.model.PaymentCreditResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class GetnetDao
		extends PluginPaymentDao<GetnetPaymentsRecord, GetnetPayments, GetnetPaymentsRecord, GetnetPayments> {

	public GetnetDao(final DataSource dataSource) throws SQLException {
		super(GETNET_PAYMENTS, GETNET_PAYMENTS, dataSource);
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

	public GetnetPaymentsRecord addResponseCapture(final UUID kbAccountId, final UUID kbPaymentId,
			final UUID kbPaymentTransactionId, final TransactionType transactionType, final BigDecimal amount,
			final Currency currency, final PaymentCreditDelayedConfirmResponse getnetPayment, final UUID kbTenantId,
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
									toLocalDateTime(DateTime.parse(getnetPayment.getCreditConfirm().getConfirmDate())),
									originalTransaction.getAuthorizationCode(), originalTransaction.getAuthorizedAt(),
									originalTransaction.getReasonCode(), getnetPayment.getCreditConfirm().getMessage(),
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
}