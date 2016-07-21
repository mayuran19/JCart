--Role for admin
CREATE ROLE GRPEXPADMIN LOGIN
  PASSWORD 'GRPEXPADMIN';

--Role for application user
CREATE ROLE GRPEXPAPP LOGIN
  PASSWORD 'GRPEXPAPP';

--Create table space
CREATE TABLESPACE GRPEXP
  OWNER GRPEXPADMIN
  LOCATION '/opt/postgres/data/GRPEXP';

--Create database
CREATE DATABASE GRPEXP
  WITH OWNER = GRPEXPADMIN
       ENCODING = 'UTF8'
       TABLESPACE = GRPEXP
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;

GRANT CONNECT ON DATABASE GRPEXP TO GRPEXPAPP;

--Revoke all permission for public
REVOKE ALL ON DATABASE GRPEXP FROM public;

--##############################################
--Connect using the GRPEXPADMIN and execute
--psql -h localhost -p 5432 -d grpexp -U grpexpadmin
--##############################################

CREATE SCHEMA GRPEXP AUTHORIZATION GRPEXPADMIN;

GRANT ALL ON SCHEMA GRPEXP TO GRPEXPADMIN;

--Grant permission to schema to grpexpapp
GRANT USAGE ON SCHEMA GRPEXP TO GRPEXPAPP;

--Grant select,update,delete,insert to all tables in schema grpexp
GRANT SELECT,INSERT,UPDATE,DELETE ON ALL TABLES IN SCHEMA GRPEXP TO GRPEXPAPP;

--Grant usage,select,update to all sequences in schema grpexp
GRANT USAGE,SELECT,UPDATE ON ALL SEQUENCES IN SCHEMA GRPEXP TO GRPEXPAPP;

--Create users table
CREATE TABLE GRPEXP.USERS(
	id serial not null,
	username varchar(50) not null unique,
	email varchar(100) not null,
	firstname varchar(50) not null,
	lastname varchar(50),
	password_hash varchar(100) not null,
	password_salt varchar(100) not null,
	created_at timestamp not null,
	updated_at timestamp not null
)
TABLESPACE GRPEXP;

alter table GRPEXP.USERS add constraint pk_users primary key(id) using index TABLESPACE GRPEXP;

--Create GROUPS table
CREATE TABLE GRPEXP.GROUPS(
	id serial not null,
	groupname varchar(50) not null unique,
	created_at timestamp not null,
	updated_at timestamp not null
)
TABLESPACE GRPEXP;

alter table GRPEXP.GROUPS add constraint pk_groups primary key(id) using index TABLESPACE GRPEXP;

--Create GROUP_SETTINGS table
CREATE TABLE GRPEXP.GROUP_SETTINGS(
	id serial not null,
	group_id integer,
	setting_type varchar(100),
	setting_name varchar(100),
	setting_value varchar(100),
	created_at timestamp not null,
	updated_at timestamp not null
)
TABLESPACE GRPEXP;

alter table GRPEXP.GROUP_SETTINGS add constraint pk_group_settings primary key(id) using index TABLESPACE GRPEXP;
alter table GRPEXP.GROUP_SETTINGS add constraint unq_type_name unique(setting_type, setting_name, group_id) using index TABLESPACE GRPEXP;
alter table GRPEXP.GROUP_SETTINGS add constraint fk_group_id FOREIGN key(group_id) references GRPEXP.GROUPS(id);

--Create EXPENSE_CYCLES table
CREATE table GRPEXP.EXPENSE_CYCLES(
	id serial not null,
	group_id integer,
	from_date date,
	to_date date,
	created_at timestamp not null,
	updated_at timestamp not null
)
TABLESPACE GRPEXP;

alter table GRPEXP.EXPENSE_CYCLES add constraint pk_expense_cycles primary key(id) using index TABLESPACE GRPEXP;
alter table GRPEXP.EXPENSE_CYCLES add constraint fk_group_id FOREIGN key(group_id) references GRPEXP.GROUPS(id);

--
CREATE table GRPEXP.FIXED_EXPENSES(
	id serial not null,
	name varchar(100),
	group_id integer,
	is_mandatory boolean,
	default_amount numeric,
	default_user_id integer,
	created_at timestamp not null,
	updated_at timestamp not null
)
TABLESPACE GRPEXP;

alter table GRPEXP.FIXED_EXPENSES add constraint pk_fixed_expenses primary key(id) using index TABLESPACE GRPEXP;
alter table GRPEXP.FIXED_EXPENSES add constraint fk_group_id FOREIGN key(group_id) references GRPEXP.GROUPS(id);
alter table GRPEXP.FIXED_EXPENSES add constraint fk_user_id FOREIGN key(default_user_id) references GRPEXP.USERS(id);

--
CREATE TABLE GRPEXP.EXPENSES(
	id serial not null,
	group_id integer,
	user_id integer,
	fixed_expense_id integer,
	expense_description varchar(200),
	amount numeric not null,
	expense_date date,
	expense_cycle_id integer,
	created_at timestamp not null,
	updated_at timestamp not null	
)
TABLESPACE GRPEXP;

alter table GRPEXP.EXPENSES add constraint pk_expenses primary key(id) using index TABLESPACE GRPEXP;
alter table GRPEXP.EXPENSES add constraint fk_group_id FOREIGN key(group_id) references GRPEXP.GROUPS(id);
alter table GRPEXP.EXPENSES add constraint fk_user_id FOREIGN key(user_id) references GRPEXP.USERS(id);
alter table GRPEXP.EXPENSES add constraint fk_fixed_expense_id FOREIGN key(fixed_expense_id) references GRPEXP.FIXED_EXPENSES(id);
alter table GRPEXP.EXPENSES add constraint fk_expense_cycle_id FOREIGN key(expense_cycle_id) references GRPEXP.EXPENSE_CYCLES(id);

--
CREATE TABLE GRPEXP.EXPENSES_PER_USER(
	id serial not null,
	expense_id integer,
	user_id integer,
	group_id integer,
	amount numeric not null,
	division_factor integer,
	division_factor_per_user integer,
	created_at timestamp not null,
	updated_at timestamp not null	
)
TABLESPACE GRPEXP;

alter table GRPEXP.EXPENSES_PER_USER add constraint pk_expenses_per_user primary key(id) using index TABLESPACE GRPEXP;
alter table GRPEXP.EXPENSES_PER_USER add constraint fk_expense_id FOREIGN key(expense_id) references GRPEXP.EXPENSES(id);
alter table GRPEXP.EXPENSES_PER_USER add constraint fk_user_id FOREIGN key(user_id) references GRPEXP.USERS(id);
alter table GRPEXP.EXPENSES_PER_USER add constraint fk_group_id FOREIGN key(group_id) references GRPEXP.GROUPS(id);

--
CREATE TABLE GRPEXP.EXPENSES_SUMMARY_ITEMS(
	id serial not null,
	item_name varchar(200),
	item_description varchar(500),	
	created_at timestamp not null,
	updated_at timestamp not null	
)
TABLESPACE GRPEXP;

alter table GRPEXP.EXPENSES_SUMMARY_ITEMS add constraint pk_EXPENSES_SUMMARY_ITEM primary key(id) using index TABLESPACE GRPEXP;

--
CREATE TABLE GRPEXP.EXPENSES_SUMMARY(
	id serial not null,
	group_id integer,
	expense_cycle_id integer,
	expense_summary_item_id integer,
	amount numeric not null,
	receiver_user_id integer,
	payer_user_id integer,	
	created_at timestamp not null,
	updated_at timestamp not null
)
TABLESPACE GRPEXP;

alter table GRPEXP.EXPENSES_SUMMARY add constraint pk_expenses_summary primary key(id) using index TABLESPACE GRPEXP;
alter table GRPEXP.EXPENSES_SUMMARY add constraint fk_expense_cycle_id FOREIGN key(expense_cycle_id) references GRPEXP.EXPENSE_CYCLES(id);
alter table GRPEXP.EXPENSES_SUMMARY add constraint fk_expense_summary_item_id FOREIGN key(expense_summary_item_id) references GRPEXP.EXPENSES_SUMMARY_ITEMS(id);
alter table GRPEXP.EXPENSES_SUMMARY add constraint fk_group_id FOREIGN key(group_id) references GRPEXP.GROUPS(id);
alter table GRPEXP.EXPENSES_SUMMARY add constraint fk_receiver_user_id FOREIGN key(receiver_user_id) references GRPEXP.USERS(id);
alter table GRPEXP.EXPENSES_SUMMARY add constraint fk_payer_user_id FOREIGN key(payer_user_id) references GRPEXP.USERS(id);

--
CREATE TABLE GRPEXP.USER_GROUPS(
	id serial not null,
	user_id integer,
	group_id integer,
	is_admin boolean,
	created_at timestamp not null,
	updated_at timestamp not null	
)
TABLESPACE GRPEXP;

alter table GRPEXP.USER_GROUPS add constraint pk_user_groups primary key(id) using index TABLESPACE GRPEXP;
alter table GRPEXP.USER_GROUPS add constraint unq_user_group unique(user_id, group_id) using index TABLESPACE GRPEXP;
alter table GRPEXP.USER_GROUPS add constraint fk_group_id FOREIGN key(group_id) references GRPEXP.GROUPS(id);
alter table GRPEXP.USER_GROUPS add constraint fk_user_id FOREIGN key(user_id) references GRPEXP.USERS(id);


INSERT INTO grpexp.expenses_summary_items(
            item_name, item_description, created_at, updated_at)
    VALUES ('TOTAL_PER_EXPENSE_CYCLE', null, current_timestamp, current_timestamp);

INSERT INTO grpexp.expenses_summary_items(
            item_name, item_description, created_at, updated_at)
    VALUES ('TOTAL_PER_EXPENSE_CYCLE_PER_USER', null, current_timestamp, current_timestamp);

INSERT INTO grpexp.expenses_summary_items(
            item_name, item_description, created_at, updated_at)
    VALUES ('TOTAL_SPENDING_PER_EXPENSE_CYCLE_PER_USER', null, current_timestamp, current_timestamp);

INSERT INTO grpexp.expenses_summary_items(
            item_name, item_description, created_at, updated_at)
    VALUES ('TO_BE_PAID_TO', null, current_timestamp, current_timestamp);

INSERT INTO grpexp.expenses_summary_items(
            item_name, item_description, created_at, updated_at)
    VALUES ('TO_BE_RECEIVED_FROM', null, current_timestamp, current_timestamp);
