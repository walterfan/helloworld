CREATE TABLE inventory (
user_id uuid,
inventory_id uuid,
inventory_name text,
name text,
tags text,
create_time timestamp,
last_modified_time timestamp,
PRIMARY KEY(user_id,inventory_id));


create index on inventory(name);

CREATE TABLE person (
id text PRIMARY KEY,
name text,
age int);