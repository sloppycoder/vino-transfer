curl -X POST http://localhost:8080/submit,process,result -H "Content-Type: application/json" -d @transfer.json 
curl -X POST http://localhost:8080/submit,queue -H "Content-Type: application/json" -d @transfer.json 
