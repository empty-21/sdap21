SELECT  id
       ,behavior
       ,formula
       ,fungible
       ,nonfungible
FROM token_behavior
order by id
limit 100;