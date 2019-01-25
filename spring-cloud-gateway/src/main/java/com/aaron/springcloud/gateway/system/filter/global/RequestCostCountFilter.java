package com.aaron.springcloud.gateway.system.filter.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 接口请求时间统计
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-11-27
 */
@Component
public class RequestCostCountFilter implements GlobalFilter, Ordered
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestCostCountFilter.class);

    private static final String START_TIME = "START_TIME";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            Long startTime = exchange.getAttribute(START_TIME);

            if (startTime != null)
            {
                long cost = System.currentTimeMillis() - startTime;

                if (cost > 500)
                {
                    LOGGER.warn("满请求，接口耗时：{}毫秒", cost);
                    //这里记录慢请求
                }
                else
                {
                    LOGGER.trace("接口耗时：{}毫秒", cost);
                }
            }
        }));
    }


    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}