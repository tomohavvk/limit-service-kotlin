
create table limits
(
  uuid            uuid PRIMARY KEY,
  "limit"         jsonb not null
);

CREATE UNIQUE INDEX unique_limit_name_idx ON limits (("limit"->>'name'));
