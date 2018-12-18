package com.aaron.springcloud.gateway.system.filter.global;

import java.nio.ByteBuffer;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局权限验证
 * <p>
 * 优先，从header读取
 * <p>
 * 其次，从url参数中获取认证信息
 * <p>
 * 最后，从cookie中获取认证信息
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-11-28
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered
{

    /**
     * 你
     */
    private static final List<String> ALLOWED_PATH = new ArrayList<>();

    private static final Map<String, String> UNAUTHORIZED_RESULT = new HashMap<>();


    static
    {
        ALLOWED_PATH.add("consumer/student");
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        List<String> auth = exchange.getRequest().getHeaders().get("");
        if (exchange.getRequest().getURI().getPath().contains(ALLOWED_PATH.get(0)))
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            ByteBuffer byteBuffer = null;
            Flux<DataBuffer> in = Flux.just(new DefaultDataBufferFactory().wrap(byteBuffer));
            Flux<Flux<DataBuffer>> just = Flux.just(in);

            return exchange.getResponse().writeAndFlushWith(just);
        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder()
    {
        return 0;
    }
}
