package com.aaron.springcloud.controller;

import com.aaron.springcloud.model.po.Student;
import com.aaron.springcloud.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019/12/18
 */
@RestController ("student")
public class StudentController
{
    @Autowired
    private StudentService studentService;


    @PostMapping (consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long createStudent(@RequestBody Student student, @RequestParam ("callbackType") Integer callbackType)
    {
        return studentService.update(student, callbackType);
    }


    @GetMapping ()
    public Long getStudent(@RequestParam ("callbackType") Integer callbackType)
    {

        Student student = new Student();
        student.setId(1L);
        student.setClassId(1L);
        student.setName("cc");
        studentService.getUpdate(student, callbackType);
        return student.getId();
    }
}
