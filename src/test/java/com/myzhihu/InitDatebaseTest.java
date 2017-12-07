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
    public void initDataTest() {
        Random random = new Random();
        for (int i = 1; i <= 11; ++i) {
            User user = new User();
            user.setHeadUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4080609108,1550036849&fm=27&gp=0.jpg");
            user.setName(String.format("USER%d", i));
            user.setPassword("123");
            user.setSalt(" "+random.nextInt(10000));
            userDAO.addUser(user);

            user.setPassword("xxx");
            userDAO.updatePassword(user);
        }

        for(int i = 1;i<=11;++i) {
            Question question = new Question();
            question.setCommentCount(0);
            question.setTitle(String.format("题目标题[%d]",i));
            question.setUserId(i);
            question.setContent(String.format("问题内容{%d}",i));
            Date date = new Date();
            date.setTime(date.getTime()+1000);
            question.setCreatedDate(date);
            questionDAO.addQuestion(question);
        }

        System.out.print(questionDAO.selectLatestQuestions(0,0,10));
    }
}
