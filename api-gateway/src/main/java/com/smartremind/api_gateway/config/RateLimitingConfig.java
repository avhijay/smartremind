package com.smartremind.api_gateway.config;


import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RateLimitingConfig {

    @Bean(name = "ipKeyResolver")
    public KeyResolver ipKeyResolver(){
        return  exchange -> {


            var remoteAddress = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();



            return Mono.just(remoteAddress);
        };

    }

    @Bean(name = "userKeyResolver")
    public KeyResolver userKeyResolver(){
        return exchange -> {

            String username = exchange.getRequest().getHeaders().getFirst("X-User-Name");

            if (username==null){
                return Mono.just("anonymous");

            }
            return Mono.just(username);
        };

    }

}
