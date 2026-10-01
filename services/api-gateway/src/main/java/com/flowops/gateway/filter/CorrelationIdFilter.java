package com.flowops.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter
        implements WebFilter, Ordered {

    public static final String HEADER =
            "X-Correlation-Id";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {

        String correlationId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HEADER);

        if (correlationId == null
                || correlationId.isBlank()) {

            correlationId =
                    UUID.randomUUID().toString();
        }

        ServerHttpRequest request =
                exchange.getRequest()
                        .mutate()
                        .header(
                                HEADER,
                                correlationId
                        )
                        .build();

        ServerWebExchange mutatedExchange =
                exchange
                        .mutate()
                        .request(request)
                        .build();

        mutatedExchange
                .getResponse()
                .getHeaders()
                .add(
                        HEADER,
                        correlationId
                );

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}