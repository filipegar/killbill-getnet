create table getnet_payments (
  record_id serial
, kb_account_id char(36) not null
, kb_payment_id char(36) not null
, kb_payment_transaction_id char(36) not null
, transaction_type varchar(32) not null
, amount numeric(15,9)
, currency char(3)
, getnet_payment_id char(36) not null
, seller_id char(36) not NULL
, order_id VARCHAR(36) not NULL
, getnet_status VARCHAR(20) NOT NULL
, received_at DATETIME NOT NULL
, authorization_code VARCHAR(20)
, authorized_at DATETIME
, reason_code VARCHAR(5)
, reason_message VARCHAR(250)
, soft_descriptor VARCHAR(22)
, brand VARCHAR(50)
, terminal_nsu VARCHAR(100)
, acquirer_transaction_id VARCHAR(20)
, transaction_id VARCHAR(250)
, created_date datetime not null
, kb_tenant_id char(36) not null
, primary key(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;


create index getnet_payments_kb_payment_id on getnet_payments(kb_payment_id);
create index getnet_payments_kb_payment_transaction_id on getnet_payments(kb_payment_transaction_id);
create index getnet_payments_getnet_payment_id on getnet_payments(getnet_payment_id);

create table getnet_payment_methods (
  record_id serial
, kb_account_id char(36) not null
, kb_payment_method_id char(36) not null
, getnet_card_id varchar(255) not null
, is_default smallint not null default 0
, is_deleted smallint not null default 0
, created_date datetime not null
, updated_date datetime not null
, kb_tenant_id char(36) not null
, primary key(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index getnet_payment_methods_kb_payment_id on getnet_payment_methods(kb_payment_method_id);
create index getnet_payment_methods_getnet_card_id on getnet_payment_methods(getnet_card_id);