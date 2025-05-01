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

```text
jgoodyear@Mac spring-ai-mcp-server-demo % sh test_circuitBreaker.sh
Sending 100 concurrent requests to http://localhost:3001/api/books/author/Heath%20Kesler
---------------------------------------------
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 500]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 429]
 [HTTP 429]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 429]
 [HTTP 429]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]
 [HTTP 200]

```

Sample MCP Client Side:
```text
jgoodyear@Mac bin % ./karaf
        __ __                  ____      
       / //_/____ __________ _/ __/      
      / ,<  / __ `/ ___/ __ `/ /_        
     / /| |/ /_/ / /  / /_/ / __/        
    /_/ |_|\__,_/_/   \__,_/_/         

  Apache Karaf (4.4.7)

Hit '<tab>' for a list of available commands
and '[cmd] --help' for help on a specific command.
Hit '<ctrl-d>' or type 'system:shutdown' or 'logout' to shutdown Karaf.

karaf@root()> ai:ask "What is the review for the book OSGi Starter by Jamie Goodyear?"
 I apologize for the inconvenience, but it seems there is an issue with the service required to fetch the reviews. Here's a brief review of OSGi Starter by Jamie Goodyear based on other sources:

OSGi Starter is a great book for developers who are interested in learning about the OSGi framework and its capabilities. The author, Jamie Goodyear, does an excellent job of explaining complex concepts in a straightforward and easy-to-understand manner.

The book covers essential topics such as module definition, service registration, dependency management, and versioning. It also provides practical examples that help readers get hands-on experience with OSGi.

One of the strengths of this book is its focus on real-world scenarios and use cases, making it highly relevant for professionals working in software development. The book also includes a comprehensive introduction to various tools and frameworks commonly used in conjunction with OSGi, such as Bndtools, Maven, and Karaf.

Overall, OSGi Starter is an excellent resource for developers looking to expand their skills and learn about the powerful and versatile OSGi framework. Whether you're new to OSGi or have some experience but want to deepen your understanding, this book will be a valuable addition to your library.

Again, I apologize for any inconvenience caused by the service issue. If possible, please try again later or consult other sources for more reviews on OSGi Starter by Jamie Goodyear.
karaf@root()>
```
