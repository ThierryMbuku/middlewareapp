-- run as owner
-- v2.2
-------------------------------------------------------------------------
-- sequences
-------------------------------------------------------------------------
update sequences set sub_service = 'same_day' where sub_service = 'SAMEDAY';
update sequences set sub_service = 'one_day' where sub_service = 'ONE DAY';
update sequences set sub_service = 'two_day' where sub_service = 'TWO DAY';
commit;
-------------------------------------------------------------------------
-- outcome_staging
-------------------------------------------------------------------------
drop table outcome_staging cascade constraints;

create table outcome_staging(
	id               	number(10) not null,
	period		      	varchar2(7) not null,
	party               number(10) not null,
	master             	number(10) not null,
	outcome				varchar2(10) not null,
	pay_day		      	number(2) not null,
	filed          		timestamp,
	paid	          	timestamp,
	bank				varchar2(50),
	branch				varchar2(6),
	account				varchar2(30),
	type				varchar2(12),
	holder				varchar2(60),
	province			varchar2(20) not null,
	creator       		varchar2(50) not null,
	created          	timestamp not null,
	primary key (id)
);

grant select on outcome_staging to srdeft_ro_role;
grant select, insert, update, delete on outcome_staging to srdeft_rw_role;

