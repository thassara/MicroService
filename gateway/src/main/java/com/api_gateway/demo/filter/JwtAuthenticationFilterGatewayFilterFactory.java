package com.api_gateway.demo.filter;

import com.api_gateway.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationFilterGatewayFilterFactory.Config> {
    // Your existing code here...



    @Autowired
    private JwtService jwtService;

    public JwtAuthenticationFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // Skip token check for auth endpoints
            if (path.startsWith("/api/auth")) {
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.error(new RuntimeException("Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7);

            try {
                jwtService.validateToken(token);
                String role = jwtService.extractRole(token);

                if (role == null) {
                    return Mono.error(new RuntimeException("No roles found in token"));
                }

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Role", role)
                        .build();

                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();

                return chain.filter(mutatedExchange);

            } catch (Exception e) {
                return Mono.error(new RuntimeException("Invalid token: " + e.getMessage()));
            }
        };
    }

    public static class Config {
        // Required config class
    }
}
