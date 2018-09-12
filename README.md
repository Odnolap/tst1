How to buld: `mvn clean package`  
How to run: `java -jar ./target/tst1-1.0-SNAPSHOT.jar `  
How to shutdown: `curl -XGET localhost:8080/v1/quit` (for ease of use during testing)

It's just a test server.  
Try to get `localhost:8080/v1/transactions?accountId=11&page=0&offset=10`  
or `localhost:8080/v1/transactions?cusTOmeRId=1&page=1&offset=2`  
or `localhost:8080/v1/transactions/1003`  
or `localhost:8080/v1/transactions?page=0&offset=20` for example.  
Available endpoints:  
- /v1/transactions (GET, POST)  
- /v1/transactions/{id} (GET)  
- /v1/customers (GET)
- /v1/customers/{id} (GET)
- /v1/accounts (GET)
- /v1/accounts/{id} (GET)
- /v1/rates (GET)
- /v1/rates/{id} (GET)

Body content example for POST /v1/transactions request:
```json
{
  "accountFromId": 11,
  "customerToId": 3,
  "amountFrom": 100000,
  "currencyTo": "USD"
}
```