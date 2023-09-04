-------------------------------------------------------------------------
-- rollback
-------------------------------------------------------------------------
-- update sequences set cur_value = 0 where sub_service = 'one_day';

delete from outcome_staging;
update cov_job set status='failed' where id=384;
commit;

