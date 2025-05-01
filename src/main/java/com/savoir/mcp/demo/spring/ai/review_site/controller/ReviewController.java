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
package com.savoir.mcp.demo.spring.ai.review_site.controller;

import com.savoir.mcp.demo.spring.ai.review_site.model.Review;
import com.savoir.mcp.demo.spring.ai.review_site.service.ReviewSiteService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class ReviewController {

    private final ReviewSiteService reviewSiteService;

    public ReviewController(ReviewSiteService reviewSiteService) {
        this.reviewSiteService = reviewSiteService;
    }

    @GetMapping("/author/{author}")
    public List<Review> getAllReviewsByAuthor(@PathVariable String author) {
        return reviewSiteService.findAllReviewsByAuthor(author);
    }

    @GetMapping("/title/{title}")
    public Review getReviewByTitle(@PathVariable String title) {
        return reviewSiteService.getReviewByTitle(title);
    }

}
