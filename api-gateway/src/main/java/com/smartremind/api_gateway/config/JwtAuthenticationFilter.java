package com.smartremind.api_gateway.config;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
public class JwtAuthenticationFilter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

// we have Mono<SecurityContext>
        ServerHttpRequest request = exchange.getRequest();

        //to get - Mono<Authentication> | extracting  Authentication object
       return ReactiveSecurityContextHolder.getContext().map(auth->

               // flat map used as output itself is reactive
               //flatMap is used when NEXT operation itself returns another Mono/reactive pipeline.
            auth.getAuthentication()).flatMap(authenticated ->{
                JwtAuthenticationToken jwtAuthenticationToken= (JwtAuthenticationToken) authenticated;

                var jwt = jwtAuthenticationToken.getToken();

                String username = jwt.getClaimAsString("sub");

           Collection<String> roles = jwt.getClaimAsStringList("roles");

           String rolesHeader = String.join(",",roles);

           ServerHttpRequest request1 = request.mutate().header("X-User-Name",username)
                   .header("X-User-Roles",rolesHeader).build();

           ServerWebExchange newWebExchange = exchange.mutate().request(request1).build();

           return chain.filter(newWebExchange);





        }).switchIfEmpty(chain.filter(exchange));



    }

    @Override
    public int getOrder() {
        return 1;
    }
}
