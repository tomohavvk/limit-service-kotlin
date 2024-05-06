
create table limits
(
  uuid            uuid PRIMARY KEY,
  "limit"         jsonb not null
);

CREATE UNIQUE INDEX unique_limit_name_idx ON limits (("limit"->>'name'));

CREATE TABLE transactions (
    uuid UUID PRIMARY KEY,
    terminalId UUID NOT NULL,
    amount NUMERIC NOT NULL,
    currency VARCHAR(3) NOT NULL,
    cardHash VARCHAR(255) NOT NULL,
    mcc VARCHAR(10) NOT NULL,
    gateway VARCHAR(255) NOT NULL,
    processedAt TIMESTAMP NOT NULL
);

CREATE INDEX transactions_terminal_id_idx ON transactions (terminalId);
CREATE INDEX transactions_amount_idx ON transactions (amount);
CREATE INDEX transactions_currency_idx ON transactions (currency);
CREATE INDEX transactions_card_hash_idx ON transactions (cardHash);
CREATE INDEX transactions_mcc_idx ON transactions (mcc);
CREATE INDEX transactions_gateway_idx ON transactions (gateway);
CREATE INDEX transactions_processed_at_idx ON transactions (processedAt);