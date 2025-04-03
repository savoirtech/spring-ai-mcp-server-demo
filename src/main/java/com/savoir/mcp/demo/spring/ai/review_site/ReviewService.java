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
package com.savoir.mcp.demo.spring.ai.review_site;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private List<Review> reviews = new ArrayList<>();

    @Tool(name = "review-site_get_all_reviews", description = "Get all book reviews on review-site.")
    public List<Review> getReviews() {
        return reviews;
    }

    @Tool(name = "review-site_get_review", description = "Get a single review from savoir review-site by title")
    public Review getReviewByTitle(String title) {
        return reviews.stream().filter(review -> review.bookTitle().equals(title)).findFirst().orElse(null);
    }

    @PostConstruct
    public void init() {
        reviews.addAll(List.of(
                new Review("OSGi Starter", "Jamie Goodyear", "A good starter book.", 5),
                new Review("Apache Karaf Cookbook", "Heath Kesler", "Thrilling.", 9),
                new Review("Learning Apache Karaf", "Jamie Goodyear", "An excellent learning resource.", 9)
        ));
    }

}
