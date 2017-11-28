package com.myzhihu.dao;

import com.myzhihu.pojo.Question;
import org.apache.ibatis.annotations.*;

@Mapper
public interface QuestionDAO {

    @Insert({"insert into question (title,content,created_date,user_id,comment_count)values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select * from question where id = #{id}"})
    Question getById(int id);

    @Update({"update question set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id,
                           @Param("commentCount") int commentCount);
}
