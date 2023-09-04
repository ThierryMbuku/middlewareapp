-- run as owner
-- v2.5
-------------------------------------------------------------------------
-- cov_job
-------------------------------------------------------------------------
alter table cov_job add verification_period varchar2(7);
update cov_job set verification_period=period;
commit;
alter table cov_job modify verification_period not null;

