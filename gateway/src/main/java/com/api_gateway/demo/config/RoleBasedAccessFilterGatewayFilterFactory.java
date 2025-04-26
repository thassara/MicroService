package com.api_gateway.demo.config;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RoleBasedAccessFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleBasedAccessFilterGatewayFilterFactory.Config> {

    public RoleBasedAccessFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String role = request.getHeaders().getFirst("X-User-Role");
            String path = request.getURI().getPath();
            System.out.print("Role is ssssss:  "+role);

            if (role == null) {
                return exchange.getResponse().setComplete(); // reject request if no role
            }

            // Match role with path
           // if (role.equals("RESTURENTADMIN") && path.startsWith("/restaurants")) {
                return chain.filter(exchange);
            //}
//            if (role.equals("CUSTOMER") && path.startsWith("/order")) {
//                return chain.filter(exchange);
//            } else if (role.equals("DELIVERY") && path.startsWith("/payment")) {
//                return chain.filter(exchange);
//            }

            //return exchange.getResponse().setComplete(); // reject unauthorized access
        };
    }

    public static class Config {
        // Required config class
    }
}
