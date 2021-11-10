UPDATE
    CONTRACT
SET 
    STATUS          =   :#${headers.dsl.status}
,   CONTRACTITEM    =   :#${headers.dsl.contractItem}::json
,   UPDATEDAT       =   CURRENT_TIMESTAMP
WHERE
    CONTRACTID  =   :#${headers.dsl.contractId}
AND STATUS      =   1
AND TEMPLATEID  =   :#${headers.dsl.templateId}
    RETURNING       CONTRACTID
,   PARENTID
,   SIGNATORIES
,   OBSERVERS
,   TEMPLATEID
,   EXECISERCHOICE
;