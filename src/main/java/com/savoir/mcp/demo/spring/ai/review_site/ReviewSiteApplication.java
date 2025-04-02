package com.savoir.mcp.demo.spring.ai.review_site;

import java.util.List;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReviewSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewSiteApplication.class, args);
	}

	@Bean
	public List<ToolCallback> reviewTools(ReviewService reviewService) {
		return List.of(ToolCallbacks.from(reviewService));
	}

	@Bean
	public ToolCallbackProvider reviewSiteTools(ReviewService reviewService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(reviewService)
				.build();
	}
}
