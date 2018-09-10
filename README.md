How to buld: `mvn clean package`  
How to run: `java -jar ./target/tst1-1.0-SNAPSHOT.jar `  
How to shutdown: `localhost:8080/v1/quit`

It's just a test server.  
Try to get /v1/transactions?accountId=123 or /v1/transactions?customerId=456
or /v1/transactions/555 for example.
Available endpoints:
- /v1/transactions (GET, POST)
- /v1/transactions/{id} (GET)

For GET request you must specify either 'accountId' or 'customerId' query parameter or 'id' in path. 

Body content example for POST request:
```json
{
  "accountFromId": 111,
  "customerToId": 222,
  "currencyFrom": "GBP",
  "amountFrom": 100,
  "currencyTo": "EUR"
}
```