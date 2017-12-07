package com.myzhihu.dao;

import com.myzhihu.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface MessageDAO {
    @Insert({"insert into message (from_user_id,to_user_id,content,created_date,has_read,conversation_id) values" +
            "(#{fromUserId},#{toUserId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select * from message where id = #{id}"})
    Message selectMessageById(int id);

    @Select({"select * from message where conversation_id = #{conversationId}order by created_date desc limit #{offset},#{limit}"})
    List<Message> getConversationIdDetail(@Param("conversationId")String conversationId,
                                          @Param("offset") int offset,
                                          @Param("limit")int limit);

    @Select({"select * ,count(id) as id from ( select * from message where from_user_id=#{userId} or to_user_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"select count(id) from message where to_user_id = #{userId} and has_read = 0 and conversation_id=#{conversationId}"})
    int getConvesationUnreadCount(@Param("userId")int userId,
                                  @Param("conversationId")String conversationId);
}

