#!/bin/bash

# Number of concurrent requests
CONCURRENT_REQUESTS=100

# The URL to test
#URL="http://localhost:3001/api/books/author/Jamie%20Goodyear"
URL="http://localhost:3001/api/books/author/Heath%20Kesler"

# Function to make a single request
make_request() {
  curl -s -w " [HTTP %{http_code}]\n" -o /dev/null -X GET "$URL"
}

echo "Sending $CONCURRENT_REQUESTS concurrent requests to $URL"
echo "---------------------------------------------"

# Run curl commands concurrently
for i in $(seq 1 $CONCURRENT_REQUESTS); do
  make_request &
done

# Wait for all background processes to complete
wait

echo "All requests completed."
