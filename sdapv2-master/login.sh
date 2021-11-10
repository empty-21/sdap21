curl --location --request POST 'http://localhost:8088/dap/api/v2/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userid":"dap_operator",
    "secret":"111111"
}'
