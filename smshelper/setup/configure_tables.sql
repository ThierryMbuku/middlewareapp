-- run as owner
-------------------------------------------------------------------------
-- outcome
-------------------------------------------------------------------------
drop table outcome cascade constraints;
drop sequence outcome_seq;

create table outcome(
	id              number (19) not null,
	id_number      	varchar2(20) not null,
	names	    	varchar2(100) not null,
	surname			varchar2(60) not null,
	mobile      	varchar2(20) not null,
	party      		varchar2(30) not null,
	application     varchar2(30) not null,
	file_name     	varchar2(50) not null,
	processed       timestamp,
	creator       	varchar2 (50) not null,
	created         timestamp not null
);
create sequence outcome_seq start with 1;

grant select on outcome to smshelper_ro_role;
grant select, insert, update, delete on outcome to smshelper_rw_role;
grant select on outcome_seq to smshelper_rw_role;
-------------------------------------------------------------------------
-- sent_sms
-------------------------------------------------------------------------
drop table sent_sms cascade constraints;
drop sequence sent_sms_seq;

create table sent_sms(
	id              number (19) not null,
	id_number      	varchar2(20) not null,
	mobile      	varchar2(20) not null,
	period       	varchar2 (20) not null,
	creator       	varchar2 (50) not null,
	created         timestamp not null,
    primary key (id)
);
create sequence sent_sms_seq start with 1;

grant select on sent_sms to smshelper_ro_role;
grant select, insert, update, delete on sent_sms to smshelper_rw_role;
grant select on sent_sms_seq to smshelper_rw_role;
