package com.aaron.springcloud.gateway.security;

import org.springframework.web.server.ServerWebExchange;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-15
 */
abstract class SecurityCheck
{
    boolean check(ServerWebExchange exchange)
    {
        if (this.support(exchange))
        {
            return this.doCheck(exchange);
        }

        return false;
    }


    abstract boolean support(ServerWebExchange exchange);


    abstract boolean doCheck(ServerWebExchange exchange);

}
