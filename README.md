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
