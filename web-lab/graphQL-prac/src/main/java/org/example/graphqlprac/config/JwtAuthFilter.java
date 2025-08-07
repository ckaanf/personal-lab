package org.example.graphqlprac.config;

import org.example.graphqlprac.auth.JwtUtil;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebGraphQlInterceptor {
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String token = request.getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String userId = jwtUtil.extractUserId(token.substring(7));
            request.configureExecutionInput(((executionInput, builder) ->
                    builder.graphQLContext(contextBuilder ->
                            contextBuilder.of("userId", userId)).build()));
        }
        return chain.next(request);
    }
}
