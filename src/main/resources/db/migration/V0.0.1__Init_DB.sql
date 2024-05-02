
create table limits
(
  uuid            uuid PRIMARY KEY,
  "limit"         jsonb not null
);

CREATE UNIQUE INDEX unique_limit_name_idx ON limits (("limit"->>'name'));

CREATE TABLE transactions (
    uuid UUID PRIMARY KEY,
    terminal_id UUID NOT NULL,
    amount INT NOT NULL,
    currency VARCHAR(3) NOT NULL,
    card_hash VARCHAR(255) NOT NULL,
    mcc VARCHAR(10) NOT NULL,
    gateway VARCHAR(255) NOT NULL,
    processed_at TIMESTAMP NOT NULL
);