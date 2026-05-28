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

                //After JWT verification by Spring Security,
           // the Authentication object is specifically of type JwtAuthenticationToken.
           // we cast to it to get access to JWT-specific methods
                JwtAuthenticationToken jwtAuthenticationToken= (JwtAuthenticationToken) authenticated;

                //.getToken()
           // gives  the actual Jwt object —
           // the decoded token with all its claims accessible as a map.
                var jwt = jwtAuthenticationToken.getToken();

                //sub is the subject claim — standard JWT claim that
           // Spring Authorization Server sets to the username automatically.
                String username = jwt.getClaimAsString("sub");


//roles is the custom claim  added in OAuth2TokenCustomizer
           Collection<String> roles = jwt.getClaimAsStringList("roles");


           //It was stored as a Set<String>. getClaimAsStringList reads it back as a list.
           // String.join converts the collection to a single comma-separated string —
           // because HTTP headers are strings, not collections.
           String rolesHeader = String.join(",",roles);




           //In reactive, it  cannot modify a request directly — it is immutable.
           // .mutate() creates a builder copy of the request, we add headers to it,
           // .build() produces a new immutable request with those headers added.
           ServerHttpRequest request1 = request.mutate()
                   .header("X-User-Name",username)
                   .header("X-User-Roles",rolesHeader).build();


         //  ServerWebExchange is also immutable.
           //  create a new exchange with the mutated request inside it
           ServerWebExchange newWebExchange = exchange.mutate()
                   .request(request1)
                   .build();



//Pass the mutated exchange forward in the filter chain.
// The downstream service receives the request with
// X-User-Name and X-User-Roles headers already attached.
           return chain.filter(newWebExchange);


           //ReactiveSecurityContextHolder.getContext() returns an empty Mono when there is no authenticated user —
           // public routes like /auth/register, /oauth2/**.
           // If the whole chain produces nothing, switchIfEmpty catches that and just forwards the original unmodified exchange.
           // Without this, public route requests would silently complete without ever being forwarded — they would just disappear



        }).switchIfEmpty(chain.filter(exchange));



    }

    @Override
    public int getOrder() {
        return 1;
    }
}
