package com.aaron.springcloud.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.additional.idlist.IdListProvider;
import tk.mybatis.mapper.common.IdsMapper;

import java.util.List;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-07-29
 */
public interface IdSelect<T> extends IdsMapper<T>
{
    @SelectProvider (type = IdListProvider.class, method = "dynamicSQL")
    List<T> selectByIdList(@Param ("idList") List<?> var1);
}
