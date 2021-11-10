-- delete from MEMBER;
-- where USERID = 'dap_operator'
-- or  USERID = 'dap_observer';
-- 권한 그룹 ID값을 확인하기 위해 sql보다는 프로그램에서 구현하는게 좋음
-- 또는 쿼리를?[2021-08-18 15:12:41]
INSERT
INTO
    MEMBER
(
    DEFAULTUSER
,   USERID
,   SECRET
,   USEREMAIL
,   USERNAME
,   AUTHGRP
,   CREATEDAT
) VALUES

    (TRUE, 'bob',
        crypt('111111',gen_salt('bf')),
        'bob@email.com', '초기관리자', 'DAP_OPERATOR', CURRENT_TIMESTAMP)
,
    (TRUE, 'dap_operator',
        crypt('111111',gen_salt('bf')),
        'operator@email.com', '초기관리자', 'DAP_OPERATOR', CURRENT_TIMESTAMP)
,    (TRUE, 'hancomwith',
        crypt('111111',gen_salt('bf')),
        'hancomwith@email.com', '사용자', 'DAP_OPERATOR', CURRENT_TIMESTAMP)
,   (TRUE, 'dap_observer',
        crypt('111111',gen_salt('bf')),
        'observer@email.com', '관찰담당자', 'DAP_OBSERVER', CURRENT_TIMESTAMP)
,    (TRUE, 'alice',
        crypt('111111',gen_salt('bf')),
        'alice@email.com', '사용자', 'DAP_OPERATOR', CURRENT_TIMESTAMP)
;