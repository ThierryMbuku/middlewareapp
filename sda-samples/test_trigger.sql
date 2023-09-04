-- create user sda_samples identified by sda_samples default tablespace system temporary tablespace temp quota unlimited on system;

-- grant connect, create table, create sequence, create trigger to sda_samples;


-- create table party
create table party (
	id              number (19) not null,
	name      		varchar2(60),
	surname      	varchar2(60),
	id_number      	varchar2(60),
	mobile_nr     	varchar2(60),
	created         timestamp not null,
	creator       	varchar2(50) not null,
	updated         timestamp,
	updator       	varchar2(50),
	constraint party_pk primary key (id)
);


-- create table party_audit
create table party_audit (
	id              number (19) not null,
	name      		varchar2(60),
	surname      	varchar2(60),
	id_number      	varchar2(60),
	mobile_nr     	varchar2(60),
	created         timestamp not null,
	creator       	varchar2(50) not null,
	updated         timestamp,
	updator       	varchar2(50),
	triggered    	timestamp not null,
	constraint party_audit_pk primary key (id, triggered)
);


-- create trigger trg_before_update_party
create or replace trigger trg_before_update_party
before update
  on party
  for each row
begin
  insert into party_audit values(:old.id, :old.name, :old.surname, :old.id_number, :old.mobile_nr,
    							 :old.created, :old.creator, :old.updated, :old.updator, sysdate);
end;


-- insert records
insert into party values (1, 'JAN', 'DIJKSTRA', '12345', '27820647447', sysdate, 'someuser', null, null);
insert into party values (2, 'DEEPAK', 'SURI', '54321', '917773131777', sysdate, 'someuser', null, null);
commit;

-- query
-- party - 2 records
select * from party;
-- party_audit - no record
select * from party_audit;



-- update records
update party set mobile_nr='27721231234', updated=sysdate, updator='someuser' where id = 1;
update party set mobile_nr='918883131888', updated=sysdate, updator='someuser' where id = 2;
commit;

-- query
-- party - 2 records
select * from party;
-- party_audit - 2 records
select * from party_audit;



