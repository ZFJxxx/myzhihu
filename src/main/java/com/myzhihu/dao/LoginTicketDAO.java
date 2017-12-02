package com.myzhihu.dao;

import com.myzhihu.pojo.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDAO {

    @Insert({"insert into login_ticket(id,user_id,ticket,expired,status)values(#{id},#{userId},#{ticket},#{expired},#{status})"})
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"select * from login_ticket where ticket = #{ticket}"})
    LoginTicket selectLoginTicketByTicket(String ticket);

    @Update({"update login_ticket set status = #{status} where ticket =#{ticket}"})
    void updateStatus(@Param("ticket")String ticket,
                      @Param("status")int status);

    @Select({"select user_id from login_ticket where ticket =#{ticket}"})
    int selectUserIdByTicket(String ticket);
}
