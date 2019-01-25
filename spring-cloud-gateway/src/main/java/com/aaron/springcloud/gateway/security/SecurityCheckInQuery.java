package com.aaron.springcloud.gateway.security;

import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-15
 */
class SecurityCheckInQuery extends SecurityCheck
{
    @Override
    boolean support(ServerWebExchange exchange)
    {
        String auth = exchange.getRequest().getQueryParams().getFirst("Auth");

        return !StringUtils.isEmpty(auth);
    }


    boolean doCheck(ServerWebExchange exchange)
    {
        //1.从请求参数中获取
        String auth = exchange.getRequest().getQueryParams().getFirst("Auth");

        //TODO 实现自己的鉴权逻辑
        return "true".equals(auth);
    }
}