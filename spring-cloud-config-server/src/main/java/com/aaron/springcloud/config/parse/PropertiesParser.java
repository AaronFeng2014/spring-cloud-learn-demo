package com.aaron.springcloud.config.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义配置文件解析
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-12-01
 */
public class PropertiesParser implements PropertySourceLoader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesParser.class);

    private static final String XML_FILE_EXTENSION = ".xml";


    @Override
    public String[] getFileExtensions()
    {
        return new String[] {"properties", "xml"};
    }


    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException
    {
        Map<String, ?> properties = loadProperties(resource);
        if (properties.isEmpty())
        {
            return Collections.emptyList();
        }
        return Collections.singletonList(new OriginTrackedMapPropertySource(name, properties));
    }


    @SuppressWarnings ({"unchecked", "rawtypes"})
    private Map<String, ?> loadProperties(Resource resource) throws IOException
    {
        String filename = resource.getFilename();
        if (filename != null && filename.endsWith(XML_FILE_EXTENSION))
        {
            return (Map)PropertiesLoaderUtils.loadProperties(resource);
        }
        return (Map)parseProperties(resource);
    }


    private Properties parseProperties(Resource resource)
    {
        Properties properties = new Properties();
        try (InputStream inputStream = resource.getInputStream())
        {
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            LOGGER.error("配置文件解析失败", e);
        }

        return properties;
    }
}
