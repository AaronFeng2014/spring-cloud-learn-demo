package com.aaron.springcloud.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-07-29
 */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T>, IdSelect<T>
{
}
