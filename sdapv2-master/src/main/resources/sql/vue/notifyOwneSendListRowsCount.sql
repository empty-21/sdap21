-- 수신된 알람 메세지 목록 조회
select
 count(*) as total
from
  notify
where toid = :#${headers.userid};