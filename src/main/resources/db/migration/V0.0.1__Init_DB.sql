
create table limits
(
  uuid            uuid PRIMARY KEY,
  name            varchar(128) not null,
  description     varchar(256) not null,
  limit_on        jsonb not null,
  currencies      jsonb not null,
  dynamic_filter  jsonb not null,
  static_filter   jsonb not null,
  criteria        jsonb not null,
  created_at      TIMESTAMP not null
);

CREATE INDEX limit_criterion_uuid_idx ON limits (("criteria"->>'uuid'));
CREATE UNIQUE INDEX unique_limit_name_idx ON limits (name);

CREATE TABLE transactions (
    uuid UUID PRIMARY KEY,
    terminal UUID NOT NULL,
    amount NUMERIC NOT NULL,
    currency VARCHAR(3) NOT NULL,
    cardHash VARCHAR(255) NOT NULL,
    mcc VARCHAR(10) NOT NULL,
    gateway VARCHAR(255) NOT NULL,
    processedAt TIMESTAMP NOT NULL
);

CREATE INDEX transactions_terminal_idx ON transactions (terminal);
CREATE INDEX transactions_amount_idx ON transactions (amount);
CREATE INDEX transactions_currency_idx ON transactions (currency);
CREATE INDEX transactions_card_hash_idx ON transactions (cardHash);
CREATE INDEX transactions_mcc_idx ON transactions (mcc);
CREATE INDEX transactions_gateway_idx ON transactions (gateway);
CREATE INDEX transactions_processed_at_idx ON transactions (processedAt);