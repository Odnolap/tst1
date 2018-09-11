DROP TABLE transactions IF EXISTS;
DROP TABLE exchange_rates IF EXISTS;
DROP TABLE accounts IF EXISTS;
DROP TABLE customer IF EXISTS;

CREATE TABLE customers
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  patronymic_name VARCHAR(255),
  email VARCHAR(255) NOT NULL UNIQUE,
  id_card_number VARCHAR(255) NOT NULL UNIQUE,
  registered TIMESTAMP DEFAULT now(),
  is_active BOOLEAN DEFAULT true
)
AS SELECT * FROM CSVREAD('classpath:db/customers.csv');

CREATE TABLE accounts
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  currency VARCHAR(3) NOT NULL,
  balance FLOAT,
  is_active BOOLEAN DEFAULT true,
  FOREIGN KEY ( customer_id ) REFERENCES customers ( id )
)
AS SELECT * FROM CSVREAD('classpath:db/accounts.csv');
CREATE UNIQUE INDEX acct_uniq_cust_and_cur_idx ON accounts(customer_id, currency);

CREATE TABLE exchange_rates
  (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  currency_from VARCHAR(3) NOT NULL,
  currency_to VARCHAR(3) NOT NULL,
  rate FLOAT NOT NULL,
  valid_from TIMESTAMP,
  valid_to TIMESTAMP
)
AS SELECT * FROM CSVREAD('classpath:db/rates.csv');

CREATE TABLE transactions
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_from_id BIGINT NOT NULL,
  account_to_id BIGINT NOT NULL,
  currency_from VARCHAR(3) NOT NULL,
  amount_from FLOAT NOT NULL,
  currency_to VARCHAR(3) NOT NULL,
  amount_to FLOAT NOT NULL,
  exchange_rate_id BIGINT NOT NULL,
  created TIMESTAMP DEFAULT now(),
  finalized TIMESTAMP,
  status VARCHAR(10) NOT NULL,
  description VARCHAR(100),
  FOREIGN KEY ( account_from_id ) REFERENCES accounts ( id ),
  FOREIGN KEY ( account_to_id ) REFERENCES accounts ( id ),
  FOREIGN KEY ( exchange_rate_id ) REFERENCES exchange_rates ( id )
)
AS SELECT * FROM CSVREAD('classpath:db/transactoins.csv');