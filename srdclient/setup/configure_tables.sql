-- run as owner
-------------------------------------------------------------------------
-- mqevent
-------------------------------------------------------------------------
drop table mqevent cascade constraints;
drop sequence mqevent_seq;

create table mqevent(
	id              number (19) not null,
	outcome_id      number (19),
	sequence      	number (19),
	message_id      varchar2(60),
	correlation     varchar2(60),
	type      		varchar2(10),
	effected_by     varchar2(20),
	effected_on     varchar2(50),
	occurred        timestamp,
	context       	varchar2 (20),
	creator       	varchar2 (50) not null,
	created         timestamp not null,
    primary key (id)
);
create sequence mqevent_seq start with 1;

grant select on mqevent to srdclient_ro_role;
grant select, insert, update, delete on mqevent to srdclient_rw_role;
grant select on mqevent_seq to srdclient_rw_role;
