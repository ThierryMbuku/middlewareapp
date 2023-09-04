-- run as owner
-- v2.7
-------------------------------------------------------------------------
-- batch_cov_job
-------------------------------------------------------------------------
drop table batch_cov_job cascade constraints;
drop sequence batch_cov_job_seq;

create table batch_cov_job(
	id               		number(10) not null,
	processing_date			timestamp not null,
	verification_period		varchar2(7) not null,
	period		      		varchar2(7) not null,
	pay_day		      		number(2) not null,
	sub_service				varchar2(20) not null,
	type					varchar2(20) not null,
	by_region				number(1,0) not null,
	test					number(1,0) not null,
	status					varchar2(20) not null,
	sign_status				varchar2(20),
	transactions_per_job	number(10),
	transactions_total		number(10),
	transactions_processed	number(10),
	transactions_rejected	number(10),
	jstart					timestamp,
	jend					timestamp,
	messages				varchar2(4000),
	creator       			varchar2(50) not null,
    created          		timestamp not null,
	updator       			varchar2(50),
    updated          		timestamp,
    primary key (id)
);
create sequence batch_cov_job_seq start with 1;

grant select on batch_cov_job to srdeft_ro_role;
grant select, insert, update, delete on batch_cov_job to srdeft_rw_role;
grant select on batch_cov_job_seq to srdeft_rw_role;
-------------------------------------------------------------------------
-- cov_job
-------------------------------------------------------------------------
alter table cov_job add batch_cov_job number(10);
alter table cov_job add constraint cov_job_batch_cov_job_fk foreign key (batch_cov_job) references batch_cov_job (id);
