package com.aaron.springcloud.dao;

import com.aaron.springcloud.base.BaseDao;
import com.aaron.springcloud.model.po.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-07-29
 */
@Mapper
public interface StudentDao extends BaseDao<Student>
{
}
