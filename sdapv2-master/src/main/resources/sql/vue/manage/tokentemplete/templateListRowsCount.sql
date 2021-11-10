-- 수신된 알람 메세지 목록 조회
SELECT  COUNT(*) AS TOTAL
FROM token_template
WHERE template_name = :#${headers[name]}