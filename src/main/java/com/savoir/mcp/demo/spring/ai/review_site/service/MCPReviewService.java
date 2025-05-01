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
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class MCPReviewService {

    private static final Logger log = LoggerFactory.getLogger(MCPReviewService.class);
    private ReviewSiteService reviewSiteService;

    public MCPReviewService(ReviewSiteService reviewSiteService) {
        this.reviewSiteService = reviewSiteService;
    }

    @Tool(name = "review-site_get_all_reviews_by_author", description = "Get all book reviews on review-site by Author.")
    public List<Review> findAllReviewsByAuthor(@ToolParam(description = "Author") String author) {
        log.info("MCP findAllReviewsByAuthor: {}", author);
        return (List<Review>) reviewSiteService.findAllReviewsByAuthor(author);
    }

    @Tool(name = "review-site_get_review_by_title", description = "Get a single review from savoir review-site by book title")
    public Review getReviewByTitle(@ToolParam(description = "BookTitle") String bookTitle) {
        log.info("MCP getReviewByTitle: {}", bookTitle);
        return reviewSiteService.getReviewByTitle(bookTitle);
    }

}
