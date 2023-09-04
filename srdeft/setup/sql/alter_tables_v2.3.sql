-- run as owner
-- v2.3
-------------------------------------------------------------------------
-- sequences
-------------------------------------------------------------------------
insert into sequences values (103, 'ackfilenumber', null, 'daily', 'ERSZ_01', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
commit;
-------------------------------------------------------------------------
-- transmission_file
-------------------------------------------------------------------------
drop table transmission_file cascade constraints;
drop sequence transmission_file_seq;

create table transmission_file(
	id               	number(10) not null,
	file_name		  	varchar2(8) not null,
	service				varchar2(20) not null,
	sub_service			varchar2(20),
	type				varchar2(20) not null,
	ack_status			varchar2(20),
	content				varchar2(180) not null,
	error_code			varchar2(20),
	test				number(1,0) not null,
	creator       		varchar2(50) not null,
    created          	timestamp not null,
    primary key (id)
);
create sequence transmission_file_seq start with 1;

grant select on transmission_file to srdeft_ro_role;
grant select, insert, update, delete on transmission_file to srdeft_rw_role;
grant select on transmission_file_seq to srdeft_rw_role;

