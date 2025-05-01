/*
 * Copyright (c) 2012-2025 Savoir Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.savoir.mcp.demo.spring.ai.review_site.service;

import com.savoir.mcp.demo.spring.ai.review_site.model.Review;
import com.savoir.mcp.demo.spring.ai.review_site.repository.ReviewRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReviewSiteService {

    private static final Logger log = LoggerFactory.getLogger(ReviewSiteService.class);
    private ReviewRepository reviewRepository;

    public ReviewSiteService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @CircuitBreaker(name = "reviewCircuit")
    @Bulkhead(name = "reviewBulkhead", type = Bulkhead.Type.SEMAPHORE)
    public List<Review> findAllReviewsByAuthor(String author) {
        log.info("CoreService: findAllReviewsByAuthor: {}", author);
        try {
            Thread.sleep(500); // Simulate a delay
        } catch (InterruptedException e) {
            //ignore
        }
        // Simulating failure
        if (author.equals("Jamie Goodyear")) {
            throw new RuntimeException("Service unavailable");
        }
        return (List<Review>) reviewRepository.findAllReviewsByAuthor(author);
    }

    //fallback method for reviewCircuit
    public ResponseEntity<String> fallbackFindAllReviewsByAuthor(Throwable t) {
        log.info("CoreService: fallbackFindAllReviewsByAuthor: graceful failure");
        return new ResponseEntity<>("Service Unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Bulkhead(name = "reviewBulkhead", type = Bulkhead.Type.SEMAPHORE)
    public Review getReviewByTitle(String bookTitle) {
        log.info("CoreService: getReviewByTitle: {}", bookTitle);
        try {
            Thread.sleep(3000); // Simulate a delay
        } catch (InterruptedException e) {
            //ignore
        }
        return reviewRepository.findReviewByBookTitle(bookTitle);
    }

}
