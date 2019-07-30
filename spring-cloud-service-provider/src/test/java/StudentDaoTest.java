import com.aaron.springcloud.SpringCloudServiceProviderApplication;
import com.aaron.springcloud.dao.StudentDao;
import com.aaron.springcloud.model.po.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-07-29
 */
@RunWith (SpringRunner.class)
@SpringBootTest (classes = SpringCloudServiceProviderApplication.class)
public class StudentDaoTest
{
    @Autowired
    private StudentDao studentDao;


    @Test
    public void insert()
    {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 200; i++)
        {
            Student student = new Student();

            student.setCityId((long)i);
            student.setName("name" + i);
            student.setId((long)i);
            list.add(student);

        }
        studentDao.insertList(list);
    }


    @Test
    public void select()
    {
        Example example = new Example(Student.class);
        example.createCriteria().andCondition("name like '%name%'");

        System.out.print(studentDao.selectByExample(example));
    }

}