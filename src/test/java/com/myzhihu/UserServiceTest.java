package com.myzhihu;

import com.myzhihu.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyzhihuApplication.class)
@Sql("/init-schema.sql")
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Test
    public void regiseterTest(){
        userService.regiseter("123","123");
    }
}
