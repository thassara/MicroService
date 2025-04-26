package com.api_gateway.demo.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RoleBasedAccessFilter extends AbstractGatewayFilterFactory<RoleBasedAccessFilter.Config> {

    public RoleBasedAccessFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String role = request.getHeaders().getFirst("X-User-Role");
            String path = request.getURI().getPath();

            // Log role for debugging
            System.out.println("User role is: " + role);

            // No role found in header, reject request
            if (role == null) {
                return exchange.getResponse().setComplete(); // Unauthorized
            }

            // Role-based access control
            if (role.equals("RESTURENTADMIN") && path.startsWith("/restaurants")) {
                return chain.filter(exchange); // Allow access to restaurant paths
            } else if (role.equals("CUSTOMER") && path.startsWith("/order")) {
                return chain.filter(exchange); // Allow access to order paths
            } else if (role.equals("DELIVERY") && path.startsWith("/payment")) {
                return chain.filter(exchange); // Allow access to payment paths
            }

            // If none of the above conditions match, reject the request
            return exchange.getResponse().setComplete(); // Unauthorized
        };
    }

    public static class Config {
        // Empty config class required by AbstractGatewayFilterFactory
    }
}
