package com.myzhihu;

import com.myzhihu.dao.QuestionDAO;
import com.myzhihu.dao.UserDAO;
import com.myzhihu.pojo.Question;
import com.myzhihu.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyzhihuApplication.class)
@Sql("/init-schema.sql")
public class InitDatebaseTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Test
    public void userTest() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("123");
            user.setSalt(" "+random.nextInt(10000));
            userDAO.addUser(user);

            user.setPassword("xxx");
            userDAO.updatePassword(user);
            System.out.println(userDAO.selectById(1));
        }
    }


    @Test
    public void questionTest(){
        for(int i = 0;i<=10;++i) {
            Question question = new Question();
            question.setCommentCount(i);
            question.setTitle(String.format("题目标题[%d]",i));
            question.setUserId(i*i);
            question.setContent(String.format("问题内容{%d}",i));
            Date date = new Date();
            date.setTime(date.getTime()+1000);
            question.setCreatedDate(date);
            questionDAO.addQuestion(question);
        }
    }

}
