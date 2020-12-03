


```
curl -v -X POST -H "Content-Type: application/json" -d '{"imsi": "123456789012343", "imei": "123456789012343", "msisdn":"225668841"}' localhost:8899
curl -v -X POST -H "Content-Type: application/json" -d '{"imsi": "123456789012343", "imei": "123456789012345", "msisdn":"225668841"}' localhost:8899
curl -v -X POST -H "Content-Type: application/json" -d '{"imsi": "123456789012345", "imei": "123456789012345"}' localhost:8899
```

```
ote: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:8899...
* Connected to localhost (::1) port 8899 (#0)
> POST / HTTP/1.1
> Host: localhost:8899
> User-Agent: curl/7.72.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 54
> 
* upload completely sent off: 54 out of 54 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< content-type: application/json
< content-length: 87
< 
* Connection #0 to host localhost left intact
{ "result": "{"resultCode":"DIAMETER_SUCCESS","equipmentStatus":"BLACK","error":null}"}
```


```
while true
do 
   curl -X POST -H "Content-Type: application/json" -d '{"imsi": "123456789012343", "imei": "123456789012343", "msisdn":"225668841"}' localhost:8899
   printf "\n";
done
```