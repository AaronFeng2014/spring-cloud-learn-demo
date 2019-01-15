package com.aaron.springcloud.gateway.system.filter.global;

import com.aaron.springcloud.gateway.security.SecurityCheckChain;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * 全局过滤器只需要申明即可，会自动注入
 * <p>
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

    @Autowired
    private SecurityCheckChain securityCheckChain;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {

        if (!securityCheckChain.check(exchange))
        {
            //未通过登录认证
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            ByteBuffer byteBuffer = ByteBuffer.wrap("请登录".getBytes(StandardCharsets.UTF_8));
            Flux<DataBuffer> in = Flux.just(new DefaultDataBufferFactory().wrap(byteBuffer));
            Flux<Flux<DataBuffer>> just = Flux.just(in);

            return exchange.getResponse().writeAndFlushWith(just);
        }

        return chain.filter(exchange);
    }


    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}