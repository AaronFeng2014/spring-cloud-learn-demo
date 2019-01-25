package com.aaron.springcloud.gateway.security;

import org.springframework.http.HttpCookie;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-15
 */
class SecurityCheckInCookie extends SecurityCheck
{
    @Override
    boolean support(ServerWebExchange exchange)
    {
        HttpCookie auth = exchange.getRequest().getCookies().getFirst("Auth");

        return auth != null;
    }


    boolean doCheck(ServerWebExchange exchange)
    {

        String auth = exchange.getRequest().getCookies().getFirst("Auth").getValue();

        //TODO 实现自己的鉴权逻辑
        return "true".equals(auth);
    }
}