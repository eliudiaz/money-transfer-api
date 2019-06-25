CREATE SEQUENCE money_transfer.s_account_id START WITH 1;

CREATE TABLE money_transfer.account (
  id INT NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  balance DECIMAL(10,2) NOT NULL,
  previous_balance DECIMAL(10,2) NOT NULL,
  enabled BOOLEAN NOT NULL,
  disable_reason VARCHAR(50),
  created_at DATE,
  last_update DATE,

  CONSTRAINT pk_account PRIMARY KEY (ID)
);

CREATE SEQUENCE money_transfer.s_transaction_log_id START WITH 1;

CREATE TABLE money_transfer.transaction_log (
  id INT NOT NULL,
  account_id INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  created_at DATE,
  type VARCHAR(50) NOT NULL,

  CONSTRAINT pk_t_trasaction_log PRIMARY KEY (ID)
);

CREATE SEQUENCE money_transfer.s_wire_transfer_id START WITH 1;

CREATE TABLE money_transfer.wire_transfer (
  id INT NOT NULL,
  origin_account_id INT NOT NULL,
  target_account_id INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  created_at DATE,
  status VARCHAR(50) NOT NULL,
  error VARCHAR(500) NOT NULL,

  CONSTRAINT pk_t_wire_transfer PRIMARY KEY (ID)
);