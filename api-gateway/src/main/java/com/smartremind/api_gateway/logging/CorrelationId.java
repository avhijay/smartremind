package com.smartremind.api_gateway.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Component
public class CorrelationId implements GlobalFilter , Ordered {

    private static Logger log = LoggerFactory.getLogger(CorrelationId.class);

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();



//extracting correlation id from http request if present
String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);


// generation of correlation id if not present in http request header
if (correlationId==null|| correlationId.isBlank()){

    correlationId= UUID.randomUUID().toString();
}

ServerHttpRequest editRequest = request.mutate()
        .header (CORRELATION_ID_HEADER, correlationId).build();


exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER,correlationId);

        log.info("CorrelationId = {}",correlationId);
        return chain.filter(exchange.mutate().request(editRequest).build());


    }

    @Override
    public int getOrder() {
        return -1;
    }
}
