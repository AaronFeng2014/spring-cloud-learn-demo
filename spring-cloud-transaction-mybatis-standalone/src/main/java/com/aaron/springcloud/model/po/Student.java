package com.aaron.springcloud.model.po;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-07-29
 */
@Setter
@Getter
@ToString
public class Student
{
    @Id
    private Long id;

    private Long classId;

    private String name;

    private Integer status;
}
