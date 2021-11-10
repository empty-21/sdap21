--drop table wallet;
-- 지갑
CREATE TABLE "wallet" (
    id serial PRIMARY KEY,
    -- member의 id필드와 맵핑
    account_id integer,
    consoid numeric,
    -- 월렛 User 아이디
    userid VARCHAR (255) not null,
    -- 월렛 사용자 아이디
    -- account_id VARCHAR (255) not null,
    -- wallet address
    account_addr VARCHAR (255),
    -- 대표 지갑 여부  : member로 옴겨야 할지 부분은 고려
    account_addr_rep boolean default true,
    account_stat account_stat default 'ACTIVATE' :: account_stat,
    -- wallet backup method
    keybackupmethod key_backup_method default 'PERSONAL' :: key_backup_method,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
--ALTER TABLE wallet ADD member_id varchar(255) not null;
--ALTER TABLE wallet ALTER COLUMN account_id TYPE varchar(255) ;

--ALTER TABLE wallet ALTER COLUMN account_id set not null;


--ALTER TABLE "wallet" MODIFY "account_id" varchar(255) not null;


-- insert INTO wallet (consoid,member_id,account_id,account_addr,updatedAt)
-- values (1,'user_01','account_2','0xccc',CURRENT_TIMESTAMP);
-- --insert into member (wallets) values ([1,2])

-- --UPDATE member SET wallets = wallets || 2 WHERE userid = 'user_01';

-- --UPDATE member SET authgrp = 'authGrpNameId1' WHERE userid = 'user_01';

-- -- test_grp
-- select a.userid, b.* from member a  join wallet b
-- on a.userid = b.member_id;
-- --join permissin_group c on a.authgrp = c.authgrpnameid;

-- -- select * from member where userid='user_01';
-- -- UPDATE member SET authgrp = 'test_grp' WHERE userid = 'user_01';



-- SELECT m.defaultuser,
-- m.userid,
-- m.username,
-- m.userStat,
-- m.usercompany,
-- m.userdepart,
-- m.useremail,
-- m.usertel,
-- m.authgrp,
-- m.logincnt,
-- m.loginfailcnt,
-- m.lastloginedip,
-- m.lastlogindt,
-- m.createdAt,
-- g.defaultgrp,
-- g.authgrpnameid,
-- g.authgrpname,
-- g.viewhost,
-- g.mgrhost,
-- g.viewconso,
-- g.mgrconso,
-- g.viewnetwork,
-- g.mgrnetwork,
-- g.menumgrda,
-- g.viewissue,
-- g.mgrissue,
-- g.viewcontract,
-- g.viewtransfer,
-- g.menumgrpolicy,
-- g.viewbacicpolicy,
-- g.mgrbasicpolicy,
-- g.viewauthgrp,
-- g.mgrauthgrp,
-- g.viewmember,
-- g.mgrmember,
-- g.viewkyc,
-- g.mgrkyc,
-- g.viewnotice,
-- g.mgrnotice,
-- g.viewnotify,
-- g.mgrnotify,
-- g.viewauditlog,
-- g.menublockexplorer,COALESCE(wallet.wallets, '[]') AS wallets
-- FROM   member m
-- LEFT   JOIN LATERAL (
--    SELECT json_agg(json_build_object('wallet_id', wallet.account_id
--    ,'account_addr', wallet.account_addr
--    ,'created', wallet.createdAt
--    ,'account_stat', wallet.account_stat)) AS wallets
--    FROM   wallet
--    WHERE  wallet.member_id = m.userid
--    ) wallet ON true
-- join "permissin_group" g on m.authgrp = g.authgrpnameid
-- ORDER  BY m.userid;



-- -- SELECT
-- --     B.VALUE
-- -- ,   A.CATEGORY
-- -- ,   COUNT(*)            TOTAL
-- -- ,   SUM(A.GOOD)         GOOD
-- -- ,   SUM(A.ANGRY)        ANGRY
-- -- ,   SUM(A.COMMENT_NUM)  COMMENTS
-- -- ,   SUM(A.CONTENT_HIT)  HIT
-- -- FROM
-- --     tbl_content_ft A
-- -- JOIN
-- --     TBL_BUCKET  B
-- -- ON
-- --     A.CATEGORY  =   B.KEY
-- -- WHERE
-- --     A.DOMAIN            =   'naver_fin'
-- -- AND DATE_INFO['yyyymm'] =   '201812'
-- -- GROUP BY
-- --     A.CATEGORY
-- -- ,   B.VALUE
-- -- ORDER BY
-- --     TOTAL   DESC    LIMIT   ?
-- -- OFFSET  ?