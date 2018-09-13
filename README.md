**How to buld**: `mvn clean package` or with skipping tests `mvn clean package -Dmaven.test.skip=true`  
**How to run**: `java -jar ./target/tst1-1.0-SNAPSHOT.jar` Wait for message in log "Money transfer server started" (about 5 seconds).  
**How to shutdown**: `curl -XGET localhost:8080/v1/quit` (for ease of use during testing)

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
- /v1/rates (GET, POST)
- /v1/rates/{id} (GET)

So at present there is no possibility to update or delete any entity and add customer or accounte.  
You can only 
- get entities  
- add a new transaction using POST request to `/v1/transactions` with body like this: 
```json
{
  "accountFromId": 11,
  "customerToId": 3,
  "amountFrom": 100000,
  "currencyTo": "USD"
}
```
- add a new exchange rate with availableFrom >= NOW() using POST request to `/v1/rates` with body like this:
```json
{
  "currencyFrom": "EUR",
  "currencyTo": "BTC",
  "rate": "0.00018623",
  "validFrom": "2018-09-12T13:00:00",
  "validTo": "2018-09-30T15:00:00"
}
```
Time zone is UTC. If validFrom is null it's recognized = NOW(). ValidFrom must be >= NOW(). If validTo is not null it must be greater than validFrom.

Available from initialization
- customerIds: 1..3
- accountIds: 11..15
- transactionIds: 1001..1006
- currencies: USD, EUR, GBP, BTC
- exchange rates for USD <-> EUR and USD <-> BTC operations

2 unit tests: MoneyTransferServiceTest and MoneyTransferRepositoryTest
1 integration test: Tst1IntegrationTest with logging of requests