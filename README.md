# vino-transfer

This project is demostrate how to write business logic using [Spring Cloud Function](https://spring.io/projects/spring-cloud-function) that can be deployed as either API or stream processor without code changes.


## Modules

- `function`: Business logic, consists of 4 composible functions: submit, queue, process and result
- `api`: REST API for transfer operations
- `stream`: Stream processor for transfer events

## Quick start

Install rabbmitmq and redis first. If using homebrew, it's as easy as

```shell
brew install rabbitmq
brew install redis
brew services start rabbitmq
brew services start redis
```

Then start the api and stream processor:
```shell
./gradlew :api:bootRun
./gradlew :stream:bootRun
```

Send request for sync processing
```shell

curl -X POST http://localhost:8080/submit,process,result -H "Content-Type: application/json" -d @scripts/transfer.json

# the log of api should show the request is processed with some amount of
# random delay. the stream log should not show anything.
# output should be a transfer json with status PROCESSED
```

Send request for async processing
```shell
curl -X POST http://localhost:8080/submit,queue -H "Content-Type: application/json" -d @scripts/transfer.json

# output should be a refId.
# the log of api should show the request is submitted. the stream log will show
# the request being processed with random delay.
# after processing is complete

curl http://localhost:8080/result/refId

# should return trasnfer json with status PROCESSED
```
