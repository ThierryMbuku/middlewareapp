-- run as owner
-- v2.6
-------------------------------------------------------------------------
-- cov_job
-------------------------------------------------------------------------
alter table cov_job modify messages varchar2(4000);
-------------------------------------------------------------------------
-- sequences
-------------------------------------------------------------------------
update sequences set name = 'installationgeneration_del' where name = 'installationgeneration';
update sequences set name = 'usergeneration_del' where name = 'usergeneration';
insert into sequences values (104, 'installationgeneration', 'western_cape', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (105, 'installationgeneration', 'eastern_cape', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (106, 'installationgeneration', 'northern_cape', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 14, 'srdeft_owner', sysdate, null, null);
insert into sequences values (107, 'installationgeneration', 'freestate', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (108, 'installationgeneration', 'kwazulu_natal', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (109, 'installationgeneration', 'gauteng', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 17, 'srdeft_owner', sysdate, null, null);
insert into sequences values (110, 'installationgeneration', 'north_west', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (111, 'installationgeneration', 'mpumalanga', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (112, 'installationgeneration', 'limpopo', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (113, 'usergeneration', 'western_cape', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (114, 'usergeneration', 'eastern_cape', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (115, 'usergeneration', 'northern_cape', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 14, 'srdeft_owner', sysdate, null, null);
insert into sequences values (116, 'usergeneration', 'freestate', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (117, 'usergeneration', 'kwazulu_natal', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (118, 'usergeneration', 'gauteng', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 17, 'srdeft_owner', sysdate, null, null);
insert into sequences values (119, 'usergeneration', 'north_west', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (120, 'usergeneration', 'mpumalanga', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
insert into sequences values (121, 'usergeneration', 'limpopo', 'rolling', 'EJSZ_FNAME', null, 1, 9999, 15, 'srdeft_owner', sysdate, null, null);
commit;

