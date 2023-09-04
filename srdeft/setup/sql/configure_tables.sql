-- run as owner
-------------------------------------------------------------------------
-- cov_job
-------------------------------------------------------------------------
drop table cov_job cascade constraints;
drop sequence cov_job_seq;

create table cov_job(
	id               	number(10) not null,
	period		      	varchar2(7) not null,
	pay_day		      	number(2,0) not null,
	sub_service			varchar2(20) not null,
	type				varchar2(20) not null,
	by_region			number(1,0) not null,
	test				number(1,0) not null,
	status				varchar2(20) not null,
	sign_status			varchar2(20),
	jstart				timestamp,
	jend				timestamp,
	creator       		varchar2(50) not null,
    created          	timestamp not null,
	updator       		varchar2(50),
    updated          	timestamp,
    primary key (id)
);
create sequence cov_job_seq start with 1;

grant select on cov_job to srdeft_ro_role;
grant select, insert, update, delete on cov_job to srdeft_rw_role;
grant select on cov_job_seq to srdeft_rw_role;
-------------------------------------------------------------------------
-- sequences
-------------------------------------------------------------------------
drop table sequences cascade constraints;

create table sequences(
	id               	number(2) not null,
	name		      	varchar2(50) not null,
	region		      	varchar2(50),
	frequency			varchar2(20) not null,
	type				varchar2(20) not null,
	sub_service			varchar2(20),
	min_value			number(20) not null,
	max_value			number(20) not null,
	cur_value			number(10) not null,
	creator       		varchar2(50) not null,
    created          	timestamp not null,
	updator       		varchar2(50),
    updated          	timestamp,
    primary key (id)
);

grant select on sequences to srdeft_ro_role;
grant select, insert, update, delete on sequences to srdeft_rw_role;

insert into sequences values (1, 'filename', null, 'daily', 'EJSZ_FNAME', 'SAMEDAY', 1, 999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (2, 'filenumber', null, 'daily', 'EJSZ_01', 'SAMEDAY', 1, 999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (3, 'installationgeneration', 'western_cape', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (4, 'installationgeneration', 'eastern_cape', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (5, 'installationgeneration', 'northern_cape', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (6, 'installationgeneration', 'freestate', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (7, 'installationgeneration', 'kwazulu_natal', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (8, 'installationgeneration', 'gauteng', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (9, 'installationgeneration', 'north_west', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (10, 'installationgeneration', 'mpumalanga', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (11, 'installationgeneration', 'limpopo', 'rolling', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (12, 'datafileserial', null, 'daily', 'EJSZ_02', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (13, 'usergeneration', 'western_cape', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (14, 'usergeneration', 'eastern_cape', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (15, 'usergeneration', 'northern_cape', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (16, 'usergeneration', 'freestate', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (17, 'usergeneration', 'kwazulu_natal', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (18, 'usergeneration', 'gauteng', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (19, 'usergeneration', 'north_west', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (20, 'usergeneration', 'mpumalanga', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (21, 'usergeneration', 'limpopo', 'rolling', 'EJSZ_04', 'SAMEDAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (22, 'usersequence', 'western_cape', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (23, 'usersequence', 'eastern_cape', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (24, 'usersequence', 'northern_cape', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (25, 'usersequence', 'freestate', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (26, 'usersequence', 'kwazulu_natal', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (27, 'usersequence', 'gauteng', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (28, 'usersequence', 'north_west', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (29, 'usersequence', 'mpumalanga', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (30, 'usersequence', 'limpopo', 'daily', 'EJSZ_10', 'SAMEDAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (31, 'cashbookserial', null, 'daily', 'EJSZ_02', 'SAMEDAY', 1, 9999999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (32, 'filename', null, 'daily', 'EJSZ_FNAME', 'ONE DAY', 1, 999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (33, 'filenumber', null, 'daily', 'EJSZ_01', 'ONE DAY', 1, 999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (34, 'installationgeneration', 'western_cape', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (35, 'installationgeneration', 'eastern_cape', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (36, 'installationgeneration', 'northern_cape', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (37, 'installationgeneration', 'freestate', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (38, 'installationgeneration', 'kwazulu_natal', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (39, 'installationgeneration', 'gauteng', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (40, 'installationgeneration', 'north_west', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (41, 'installationgeneration', 'mpumalanga', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (42, 'installationgeneration', 'limpopo', 'rolling', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (43, 'datafileserial', null, 'daily', 'EJSZ_02', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (44, 'usergeneration', 'western_cape', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (45, 'usergeneration', 'eastern_cape', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (46, 'usergeneration', 'northern_cape', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (47, 'usergeneration', 'freestate', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (48, 'usergeneration', 'kwazulu_natal', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (49, 'usergeneration', 'gauteng', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (50, 'usergeneration', 'north_west', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (51, 'usergeneration', 'mpumalanga', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (52, 'usergeneration', 'limpopo', 'rolling', 'EJSZ_04', 'ONE DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (53, 'usersequence', 'western_cape', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (54, 'usersequence', 'eastern_cape', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (55, 'usersequence', 'northern_cape', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (56, 'usersequence', 'freestate', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (57, 'usersequence', 'kwazulu_natal', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (58, 'usersequence', 'gauteng', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (59, 'usersequence', 'north_west', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (60, 'usersequence', 'mpumalanga', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (61, 'usersequence', 'limpopo', 'daily', 'EJSZ_10', 'ONE DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (62, 'cashbookserial', null, 'daily', 'EJSZ_02', 'ONE DAY', 1, 9999999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (63, 'filename', null, 'daily', 'EJSZ_FNAME', 'TWO DAY', 1, 999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (64, 'filenumber', null, 'daily', 'EJSZ_01', 'TWO DAY', 1, 999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (65, 'installationgeneration', 'western_cape', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (66, 'installationgeneration', 'eastern_cape', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (67, 'installationgeneration', 'northern_cape', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (68, 'installationgeneration', 'freestate', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (69, 'installationgeneration', 'kwazulu_natal', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (70, 'installationgeneration', 'gauteng', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (71, 'installationgeneration', 'north_west', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (72, 'installationgeneration', 'mpumalanga', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (73, 'installationgeneration', 'limpopo', 'rolling', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (74, 'datafileserial', null, 'daily', 'EJSZ_02', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (75, 'usergeneration', 'western_cape', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (76, 'usergeneration', 'eastern_cape', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (77, 'usergeneration', 'northern_cape', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (78, 'usergeneration', 'freestate', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (79, 'usergeneration', 'kwazulu_natal', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (80, 'usergeneration', 'gauteng', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (81, 'usergeneration', 'north_west', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (82, 'usergeneration', 'mpumalanga', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (83, 'usergeneration', 'limpopo', 'rolling', 'EJSZ_04', 'TWO DAY', 1, 9999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (84, 'usersequence', 'western_cape', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (85, 'usersequence', 'eastern_cape', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (86, 'usersequence', 'northern_cape', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (87, 'usersequence', 'freestate', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (88, 'usersequence', 'kwazulu_natal', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (89, 'usersequence', 'gauteng', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (90, 'usersequence', 'north_west', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (91, 'usersequence', 'mpumalanga', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (92, 'usersequence', 'limpopo', 'daily', 'EJSZ_10', 'TWO DAY', 1, 999999, 0, 'srdeft_owner', sysdate, null, null);
insert into sequences values (93, 'cashbookserial', null, 'daily', 'EJSZ_02', 'TWO DAY', 1, 9999999999, 0, 'srdeft_owner', sysdate, null, null);
commit;
-------------------------------------------------------------------------
-- user_detail
-------------------------------------------------------------------------
drop table user_detail cascade constraints;

create table user_detail(
	id               					number(2) not null,
	user_code		      				varchar2(4) not null,
	user_name		      				varchar2(20) not null,
	installation_code		      		varchar2(4) not null,
	department_code			      		varchar2(5) not null,
	region		      					varchar2(20) not null,
	region_code		      				varchar2(3) not null,
	dated_nom_detailed_check		  	varchar2(1) not null,
	nominated_acc_seq_number			varchar2(20),
	account_number		      			varchar2(20) not null,
	branch_code		      				varchar2(20) not null,
	user_reference		      			varchar2(20) not null,
	account_status_code		      		varchar2(20),
    primary key (id)
);

grant select on user_detail to srdeft_ro_role;
grant select, insert, update, delete on user_detail to srdeft_rw_role;

insert into user_detail values (1, '0928', 'SASSA', '0928', '60272', 'western_cape', 'WCA', 'Y', null, '80333117', '900145', 'SASSA WC', null);
insert into user_detail values (2, '0469', 'SASSA', '0469', '60264', 'eastern_cape', 'ECA', 'Y', null, '80333028', '900145', 'SASSA EC', null);
insert into user_detail values (3, '0927', 'SASSA', '0927', '60270', 'northern_cape', 'NCA', 'Y', null, '80333087', '900145', 'SASSA NC', null);
insert into user_detail values (4, '0934', 'SASSA', '0934', '60265', 'freestate', 'FST', 'Y', null, '80333036', '900145', 'SASSA FS', null);
insert into user_detail values (5, '0932', 'SASSA', '0932', '60267', 'kwazulu_natal', 'KZN', 'Y', null, '80333052', '900145', 'SASSA KZN', null);
insert into user_detail values (6, '0933', 'SASSA', '0933', '60266', 'gauteng', 'GTG', 'Y', null, '80333044', '900145', 'SASSA GP', null);
insert into user_detail values (7, '0929', 'SASSA', '0929', '60271', 'north_west', 'NWE', 'Y', null, '80333109', '900145', 'SASSA NW', null);
insert into user_detail values (8, '0930', 'SASSA', '0930', '60269', 'mpumalanga', 'MPU', 'Y', null, '80333079', '900145', 'SASSA MP', null);
insert into user_detail values (9, '0931', 'SASSA', '0931', '60268', 'limpopo', 'LIM', 'Y', null, '80333095', '900145', 'SASSA LP', null);
commit;
-------------------------------------------------------------------------
-- cash_book
-------------------------------------------------------------------------
drop table cash_book cascade constraints;
drop sequence cash_book_seq;

create table cash_book(
	id               		number(10) not null,
	cov_job               	number(10) not null,
	file_name		      	varchar2(50) not null,
	serial_number			varchar2(10) not null,
	department_code			varchar2(5) not null,
	issue_date		    	varchar2(8) not null,
	payment_amount		    varchar2(16) not null,
	payment_description		varchar2(100) not null,
	creator       			varchar2(50) not null,
    created          		timestamp not null,
    primary key (id)
);
alter table cash_book add constraint cash_book_cov_job_fk foreign key (cov_job) references cov_job (id);
create sequence cash_book_seq start with 1;

grant select on cash_book to srdeft_ro_role;
grant select, insert, update, delete on cash_book to srdeft_rw_role;
grant select on cash_book_seq to srdeft_rw_role;
-------------------------------------------------------------------------
-- data_file
-------------------------------------------------------------------------
drop table data_file cascade constraints;
drop sequence data_file_seq;

create table data_file(
	id               					number(10) not null,
	cash_book               			number(10) not null,
	file_name		      				varchar2(8) not null,
	file_number							varchar2(4) not null,
	installation_generation_number		varchar2(4) not null,
	user_generation_number		    	varchar2(4) not null,
	serial_number						varchar2(8) not null,
	number_of_records					varchar2(6) not null,
	record_count						varchar2(6) not null,
	first_sequence_number				varchar2(6) not null,
	last_sequence_number				varchar2(6) not null,
	total_debit_value					varchar2(12) not null,
	total_credit_value					varchar2(12) not null,
	user_code		      				varchar2(4) not null,
	user_branch		      				varchar2(6) not null,
	user_nominated_account_number		varchar2(11) not null,
	action_date							varchar2(6) not null,
	test_live_indicator					varchar2(4) not null,
	settlement_date						varchar2(8) not null,
	creator       						varchar2(50) not null,
    created          					timestamp not null,
    primary key (id)
);
alter table data_file add constraint data_file_cash_book_fk foreign key (cash_book) references cash_book (id);
create sequence data_file_seq start with 1;

grant select on data_file to srdeft_ro_role;
grant select, insert, update, delete on data_file to srdeft_rw_role;
grant select on data_file_seq to srdeft_rw_role;
-------------------------------------------------------------------------
-- patch_outcome_error
-------------------------------------------------------------------------
drop table patch_outcome_error cascade constraints;
drop sequence patch_outcome_error_seq;

create table patch_outcome_error(
	id               		number(10) not null,
	data_file               number(10) not null,
	outcome               	number(10) not null,
	type		      		varchar2(10) not null,
	error					varchar2(50) not null,
	creator       			varchar2(50) not null,
    created          		timestamp not null,
    primary key (id)
);
alter table patch_outcome_error add constraint patch_outcome_err_data_file_fk foreign key (data_file) references data_file (id);
create sequence patch_outcome_error_seq start with 1;

grant select on patch_outcome_error to srdeft_ro_role;
grant select, insert, update, delete on patch_outcome_error to srdeft_rw_role;
grant select on patch_outcome_error_seq to srdeft_rw_role;

