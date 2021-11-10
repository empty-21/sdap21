-- 사용자 정보
CREATE TABLE member (
    id serial PRIMARY KEY,
    -- 기본 사용자 유무 ( true일 경우엔 삭제 못하게...)
    defaultuser BOOL DEFAULT true,
    -- 사용자 id
    userid VARCHAR (60) not null,
    -- 비밀번호
    secret TEXT not null,
    -- 사용자 이름
    username VARCHAR (50) not null,
    -- 사용자 상태
    userStat user_stat default 'ACTIVATE' :: user_stat,
    -- 사용자 회사명
    usercompany VARCHAR (50),
    -- 사용자 부서
    userdepart VARCHAR (50),
    -- 사용자 이메일
    useremail VARCHAR (255) UNIQUE NOT NULL,
    -- 사용자 전화번호
    usertel VARCHAR (50),
    fidokey VARCHAR (50),
    -- 사용자 그룹
    authgrp VARCHAR (50),
    -- 로그인횟수
    logincnt Integer default 0,
    -- 로그인실패
    loginfailcnt Integer default 0,
    -- 최근 접속 IP주소
    lastloginedip VARCHAR (50),
    -- object[]
    kyc  integer Array,
    -- 다중 지갑을 소유 할수 있음 (1:N)
    --wallets  integer Array,
    -- 사용사 사이 관계 (1:N)
    addressBook  integer Array,
    -- kyc 인증 상태
    kycvalidyn kyc_stat default 'UNAUTHORIZED' :: kyc_stat,
    -- 최근 접속일
    lastlogindt TIMESTAMP WITH TIME ZONE,
    -- 갱신일
    updatedAt TIMESTAMP WITH TIME ZONE,
    -- 생성일
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
