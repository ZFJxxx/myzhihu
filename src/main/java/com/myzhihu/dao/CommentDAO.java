package com.myzhihu.dao;

import com.myzhihu.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    @Select({"Select * from comment where id = #{id}"})
    Comment SelectCommentById(int id);

    @Insert({"insert into comment(user_id,entity_id,entity_type,created_date,content,status)values" +
                              "(#{userId},#{entityId},#{entityType},#{createdDate},#{content},#{status}) "})
    int addComment(Comment comment);

    @Select({"select * from comment where entity_id =#{entityId} and entity_type = #{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId")int entityId,
                                        @Param("entityType")int entityType);

    @Select({"select count(id) from comment where entity_id =#{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId")int entityId,
                        @Param("entityType")int entityType);

    @Update({"update comment set status = #{status} where id =#{id}"})
    int updateCommentStatus(@Param("id")int id,
                            @Param("status")int status);
}
