package com.myzhihu.dao;

import com.myzhihu.pojo.Question;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface QuestionDAO {

    //添加问题
    @Insert({"insert into question (title,content,created_date,user_id,comment_count)values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    //根据ID查找问题
    @Select({"select * from question where id = #{id}"})
    Question getById(int id);

    //更新问题评论数
    @Update({"update question set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id,
                           @Param("commentCount") int commentCount);

    //显示最新的问题，userId不为0就显示用户关注的问题，为0就由降序显示数据库offset --> limit 的问题内容
    @Select({"Select * from question ORDER BY id DESC LIMIT #{offset},#{limit}"})
    List<Question> selectLatestQuestions(@Param ("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Select({"Select * from question where user_id =#{userId} ORDER BY id DESC LIMIT #{offset},#{limit}"})
    List<Question> selectUserLatestQuestions(@Param ("userId") int userId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);
}
