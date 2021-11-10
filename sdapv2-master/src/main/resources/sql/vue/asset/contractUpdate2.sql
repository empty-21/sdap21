UPDATE
    CONTRACT
SET
    EXECISERCHOICE  =   :#${body.execiserChoice}::json
,   STATUS          =   :#${body.status}
,   CONTRACTITEM    =   :#${body.contractItem}::json
,   ARCHIVEDID      =   :#${body.archivedId}
,   UPDATEDAT       =   CURRENT_TIMESTAMP
WHERE
    CONTRACTID  =   :#${body.contractId}    
RETURNING *;

-- and templateid = templateid
-- and status = 0