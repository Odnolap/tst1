-- not actual for in-memory db
-- DROP TABLE transactions IF EXISTS;
-- DROP TABLE exchange_rates IF EXISTS;
-- DROP TABLE accounts IF EXISTS;
-- DROP TABLE customers IF EXISTS;

CREATE TABLE customers
(
  id BIGINT AUTO_INCREMENT(4) PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  patronymic_name VARCHAR(255),
  email VARCHAR(255) NOT NULL UNIQUE,
  country VARCHAR(2) NOT NULL,
  id_card_number VARCHAR(255) NOT NULL UNIQUE,
  address VARCHAR(255) NOT NULL,
  registered TIMESTAMP DEFAULT now()
)
AS SELECT * FROM CSVREAD('classpath:db/populate_customers.csv');

CREATE TABLE accounts
(
  id BIGINT AUTO_INCREMENT(16) PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  currency VARCHAR(3) NOT NULL,
  balance DECIMAL(23,10),
  FOREIGN KEY ( customer_id ) REFERENCES customers ( id )
)
AS SELECT * FROM CSVREAD('classpath:db/populate_accounts.csv');
CREATE UNIQUE INDEX acct_uniq_cust_and_cur_idx ON accounts(customer_id, currency);

CREATE TABLE exchange_rates
  (
  id BIGINT AUTO_INCREMENT(113) PRIMARY KEY,
  currency_from VARCHAR(3) NOT NULL,
  currency_to VARCHAR(3) NOT NULL,
  rate DECIMAL(23,10) NOT NULL,
  valid_from TIMESTAMP NOT NULL,
  valid_to TIMESTAMP
)
AS SELECT * FROM CSVREAD('classpath:db/populate_rates.csv');

CREATE TABLE transactions
(
  id BIGINT AUTO_INCREMENT(1007) PRIMARY KEY,
  account_from_id BIGINT NOT NULL,
  account_to_id BIGINT NOT NULL,
  currency_from VARCHAR(3) NOT NULL,
  amount_from DECIMAL(23,10) NOT NULL,
  currency_to VARCHAR(3) NOT NULL,
  amount_to DECIMAL(23,10) NOT NULL,
  exchange_rate_id BIGINT NOT NULL,
  created TIMESTAMP DEFAULT now(),
  finalized TIMESTAMP,
  status VARCHAR(10) NOT NULL,
  description VARCHAR(100),
  FOREIGN KEY ( account_from_id ) REFERENCES accounts ( id ),
  FOREIGN KEY ( account_to_id ) REFERENCES accounts ( id ),
  FOREIGN KEY ( exchange_rate_id ) REFERENCES exchange_rates ( id )
)
AS SELECT * FROM CSVREAD('classpath:db/populate_transactoins.csv');