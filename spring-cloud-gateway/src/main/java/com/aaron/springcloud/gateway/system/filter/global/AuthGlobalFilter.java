package com.aaron.springcloud.gateway.system.filter.global;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

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

    private SecurityCheck securityCheck = new SecurityCheck();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        if (!securityCheck.check(exchange))
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
        return 0;
    }


    /**
     * 接口权限校验类
     * 1. 先从请求参数中获取权限信息
     * 2. 再从header
     * 3. 最后从cookie
     */
    private class SecurityCheck
    {

        private boolean check(ServerWebExchange exchange)
        {
            //1.从请求参数中获取
            String auth = exchange.getRequest().getQueryParams().getFirst("Auth");

            //2.从header中获取
            if (StringUtils.isEmpty(auth))
            {

                auth = exchange.getRequest().getHeaders().getFirst("Auth");
            }

            //3.从cookie中获取
            if (StringUtils.isEmpty(auth))
            {
                exchange.getRequest().getCookies().getFirst("Auth");
            }

            Objects.requireNonNull(auth, "缺失鉴权参数");

            return check(auth);
        }


        private boolean check(String auth)
        {
            return "true".equals(auth);
        }
    }
}
