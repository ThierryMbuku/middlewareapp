-- run as owner
-- v2.1
-------------------------------------------------------------------------
-- sequences
-------------------------------------------------------------------------
alter table sequences modify id number(3);

insert into sequences values (94, 'cashbookfilename', 'western_cape', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (95, 'cashbookfilename', 'eastern_cape', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (96, 'cashbookfilename', 'northern_cape', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (97, 'cashbookfilename', 'freestate', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (98, 'cashbookfilename', 'kwazulu_natal', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (99, 'cashbookfilename', 'gauteng', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (100, 'cashbookfilename', 'north_west', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (101, 'cashbookfilename', 'mpumalanga', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (102, 'cashbookfilename', 'limpopo', 'daily', 'EJSZ_FNAME', null, 1, 99, 0, 'srdeft_owner', sysdate, null, null);
commit;
-------------------------------------------------------------------------
-- patch_outcome_error
-------------------------------------------------------------------------
alter table patch_outcome_error modify error varchar2(500);

