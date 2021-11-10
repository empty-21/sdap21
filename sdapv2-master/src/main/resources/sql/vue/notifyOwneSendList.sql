-- 수신된 알람 메세지 목록 조회
select
  fromid,
  toid,
  notifytype,
  notimsg,
  readmark,
  updatedAt
from
  notify
where fromid = :#${headers[userid]}
order by
  updatedAt desc
limit :#${headers[rows]}::bigint
offset :#${headers[page]}::bigint;