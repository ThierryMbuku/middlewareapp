-- run as owner
-- v2.9
-------------------------------------------------------------------------
-- public_holiday
-------------------------------------------------------------------------
drop table public_holiday cascade constraints;
drop sequence public_holiday_seq;

create table public_holiday(
	id               	number(10) not null,
	ph_date				timestamp not null unique,
	name				varchar2(50) not null,
	description		   	varchar2(100),
	creator       		varchar2(50) not null,
	created          	timestamp not null,
	updator       		varchar2(50),
	updated          	timestamp,
	primary key (id)
);
create sequence public_holiday_seq start with 1;

grant select on public_holiday to srdeft_ro_role;
grant select, insert, update, delete on public_holiday to srdeft_rw_role;
grant select on public_holiday_seq to srdeft_rw_role;

-- 2020
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-01-01', 'YYYY-MM-DD'), 'New Year’s Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-03-21', 'YYYY-MM-DD'), 'Human Rights Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-04-10', 'YYYY-MM-DD'), 'Good Friday', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-04-13', 'YYYY-MM-DD'), 'Family Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-04-27', 'YYYY-MM-DD'), 'Freedom Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-05-01', 'YYYY-MM-DD'), 'Workers'' Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-06-16', 'YYYY-MM-DD'), 'Youth Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-08-09', 'YYYY-MM-DD'), 'National Women’s Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-08-10', 'YYYY-MM-DD'), 'National Women’s Day (in lieu)', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-09-24', 'YYYY-MM-DD'), 'Heritage Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-12-16', 'YYYY-MM-DD'), 'Day of Reconciliation', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-12-25', 'YYYY-MM-DD'), 'Christmas Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2020-12-26', 'YYYY-MM-DD'), 'Day of Goodwill', null, 'srdeft_owner', sysdate, null, null);
-- 2021
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-01-01', 'YYYY-MM-DD'), 'New Year’s Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-03-21', 'YYYY-MM-DD'), 'Human Rights Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-03-22', 'YYYY-MM-DD'), 'Human Rights Day (in lieu)', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-04-02', 'YYYY-MM-DD'), 'Good Friday', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-04-05', 'YYYY-MM-DD'), 'Family Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-04-27', 'YYYY-MM-DD'), 'Freedom Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-05-01', 'YYYY-MM-DD'), 'Workers'' Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-06-16', 'YYYY-MM-DD'), 'Youth Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-08-09', 'YYYY-MM-DD'), 'National Women’s Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-09-24', 'YYYY-MM-DD'), 'Heritage Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-12-16', 'YYYY-MM-DD'), 'Day of Reconciliation', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-12-25', 'YYYY-MM-DD'), 'Christmas Day', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-12-26', 'YYYY-MM-DD'), 'Day of Goodwill', null, 'srdeft_owner', sysdate, null, null);
insert into public_holiday values (public_holiday_seq.nextval, to_date('2021-12-27', 'YYYY-MM-DD'), 'Day of Goodwill (in lieu)', null, 'srdeft_owner', sysdate, null, null);
commit;
