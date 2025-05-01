# spring-ai-mcp-server-demo

MCP Server demo providing Book Reviews

## Build

```bash
mvn clean package -DskipTests
```

## Run

```bash
mvn spring-boot:run
```

alt

```text
Click run application on ReviewSiteApplication class in IDEA
```

## Rest Endpoints

```text
curl -X GET http://localhost:3001/api/books/author/Jamie%20Goodyear
curl -X GET http://localhost:3001/api/books/title/OSGi%20Starter
```

## Testing Bulkhead

```text
sh test_bulkhead.sh
```

Expected output:
```text
Sending 10 concurrent requests to http://localhost:3001/api/books/title/OSGi%20Starter
---------------------------------------------
 [HTTP 429]
 [HTTP 429]
 [HTTP 429]
 [HTTP 429]
 [HTTP 429]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
All requests completed.
```
This represents 5 concurrent calls successfully processing, with the remainder getting 429 error message (TOO_MANY_REQUESTS).

Server Side Error Message:
```text
2025-05-01T14:36:56.873-02:30 ERROR 96113 --- [review-site] [tor-http-nio-10] c.s.m.d.s.a.r.e.GlobalExceptionHandler   : Bulkhead 'reviewBulkhead' is full and does not permit further calls

io.github.resilience4j.bulkhead.BulkheadFullException: Bulkhead 'reviewBulkhead' is full and does not permit further calls
```

## Testing Circuit Breaker

```text
sh test_circuitBreaker.sh
```

The provided shell script will launch 100 requests to the find review by author endpoint. 

When its configured to search Jamie Goodyear the endpoint will always fail. When its set to Heath Kesler the endpoint will always succeed. Using this property we can explore the nature of circuit breakers.

When we run the script for Heath Kesler, we'll observe 200 and 429 HTTP codes. These represent successful calls (200), and Bulkhead (429). When we run the script with Jamie Goodyear we'll observe 500 error codes.

Checking the server side logs, we'll find:
```text
2025-05-01T15:55:49.884-02:30 ERROR 16366 --- [review-site] [ctor-http-nio-2] c.s.m.d.s.a.r.e.GlobalExceptionHandler   : CircuitBreaker 'reviewCircuit' is OPEN and does not permit further calls
```

Running a single request after will get:
```text
jgoodyear@Mac spring-ai-mcp-server-demo % curl -X GET http://localhost:3001/api/books/author/Jamie%20Goodyear
Circuit Breaker is OPEN. Please try again later.% 
```

When we run the script with Heath Kesler, we'll observe a return to 200 and or 429 codes. The circuit is restored.

In the logs we'll see:
```text
2025-05-01T15:56:38.854-02:30 ERROR 16366 --- [review-site] [tor-http-nio-11] c.s.m.d.s.a.r.e.GlobalExceptionHandler   : CircuitBreaker 'reviewCircuit' is HALF_OPEN and does not permit further calls
```

Then calls will begin to succeed as per usual.

