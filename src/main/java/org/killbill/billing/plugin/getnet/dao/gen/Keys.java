/*
 * This file is generated by jOOQ.
 */
package org.killbill.billing.plugin.getnet.dao.gen;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPaymentMethods;
import org.killbill.billing.plugin.getnet.dao.gen.tables.GetnetPayments;
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentMethodsRecord;
import org.killbill.billing.plugin.getnet.dao.gen.tables.records.GetnetPaymentsRecord;

/**
 * A class modelling foreign key relationships and constraints of tables in
 * killbill.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<GetnetPaymentMethodsRecord> KEY_GETNET_PAYMENT_METHODS_GETNET_PAYMENT_METHODS_KB_PAYMENT_ID = Internal
			.createUniqueKey(GetnetPaymentMethods.GETNET_PAYMENT_METHODS,
					DSL.name("KEY_getnet_payment_methods_getnet_payment_methods_kb_payment_id"),
					new TableField[] { GetnetPaymentMethods.GETNET_PAYMENT_METHODS.KB_PAYMENT_METHOD_ID }, true);
	public static final UniqueKey<GetnetPaymentMethodsRecord> KEY_GETNET_PAYMENT_METHODS_PRIMARY = Internal
			.createUniqueKey(GetnetPaymentMethods.GETNET_PAYMENT_METHODS,
					DSL.name("KEY_getnet_payment_methods_PRIMARY"),
					new TableField[] { GetnetPaymentMethods.GETNET_PAYMENT_METHODS.RECORD_ID }, true);
	public static final UniqueKey<GetnetPaymentMethodsRecord> KEY_GETNET_PAYMENT_METHODS_RECORD_ID = Internal
			.createUniqueKey(GetnetPaymentMethods.GETNET_PAYMENT_METHODS,
					DSL.name("KEY_getnet_payment_methods_record_id"),
					new TableField[] { GetnetPaymentMethods.GETNET_PAYMENT_METHODS.RECORD_ID }, true);
	public static final UniqueKey<GetnetPaymentsRecord> KEY_GETNET_PAYMENTS_PRIMARY = Internal.createUniqueKey(
			GetnetPayments.GETNET_PAYMENTS, DSL.name("KEY_getnet_payments_PRIMARY"),
			new TableField[] { GetnetPayments.GETNET_PAYMENTS.RECORD_ID }, true);
	public static final UniqueKey<GetnetPaymentsRecord> KEY_GETNET_PAYMENTS_RECORD_ID = Internal.createUniqueKey(
			GetnetPayments.GETNET_PAYMENTS, DSL.name("KEY_getnet_payments_record_id"),
			new TableField[] { GetnetPayments.GETNET_PAYMENTS.RECORD_ID }, true);
}
