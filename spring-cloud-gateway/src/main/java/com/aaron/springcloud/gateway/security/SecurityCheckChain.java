package com.aaron.springcloud.gateway.security;

import com.aaron.springcloud.gateway.SpringCloudGatewayApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;
import java.util.List;

/**
 * 鉴权校验，按顺序查找授权信息，找到即校验
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-15
 */
@Component
public class SecurityCheckChain
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringCloudGatewayApplication.class);

    private static final List<SecurityCheck> SECURITY_CHECK_LIST = new ArrayList<>();


    static
    {
        SECURITY_CHECK_LIST.add(new SecurityCheckInHeader());
        SECURITY_CHECK_LIST.add(new SecurityCheckInCookie());
        SECURITY_CHECK_LIST.add(new SecurityCheckInQuery());
    }


    public boolean check(ServerWebExchange exchange)
    {

        for (SecurityCheck securityCheck : SECURITY_CHECK_LIST)
        {
            if (!securityCheck.support(exchange))
            {
                continue;
            }

            return securityCheck.doCheck(exchange);
        }

        LOGGER.info("无鉴权信息");

        return false;
    }
}