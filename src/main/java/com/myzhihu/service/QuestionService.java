package com.myzhihu.service;

import com.myzhihu.dao.QuestionDAO;
import com.myzhihu.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    //显示最新的问题，userId不为0就显示用户关注的问题，为0就由降序显示数据库offset --> limit 的问题内容
    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        if( userId == 0 ) {
            return questionDAO.selectLatestQuestions(userId, offset, limit);
        }else {
            return questionDAO.selectUserLatestQuestions(userId,offset,limit);
        }
    }
}
