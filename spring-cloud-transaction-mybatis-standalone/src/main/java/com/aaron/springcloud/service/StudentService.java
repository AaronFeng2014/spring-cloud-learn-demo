package com.aaron.springcloud.service;

import com.aaron.springcloud.model.po.Student;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019/12/18
 */
public interface StudentService
{
    Long update(Student student, Integer callbackType);


    Long getUpdate(Student student, Integer callbackType);
}
