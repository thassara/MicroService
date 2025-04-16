package com.api_gateway.demo.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RoleBasedAccessFilter implements GatewayFilterFactory<RoleBasedAccessFilter.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String role = request.getHeaders().getFirst("X-User-Role");

            if (role == null) {
                throw new RuntimeException("No role information found");
            }

            // Check if the user has access based on their role
            if (role.equals("ADMIN")) {
                // Allow access to everything (example)
                return chain.filter(exchange);
            } else if (role.equals("USER") && request.getURI().getPath().startsWith("/order")) {
                // Allow users to access order routes
                return chain.filter(exchange);
            } else if (role.equals("GUEST") && request.getURI().getPath().startsWith("/payment")) {
                // Allow guests to access payment routes
                return chain.filter(exchange);
            }

            // Deny access if no valid role
            throw new RuntimeException("Unauthorized");
        };
    }

    public static class Config {
        // Config class for future customizations if needed
    }
}
