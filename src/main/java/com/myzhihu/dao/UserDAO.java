package com.myzhihu.dao;

import com.myzhihu.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDAO {

    //注册用户
    @Insert({"insert into user(name, password,salt,head_url) values (#{name},#{password},#{salt},#{headUrl})"})
    void addUser(User user);

    @Select({"select * from user where id = #{id}"})
    User selectById(int id);

    @Select({"select * from user where name = #{name}"})
    User selectByName(String name);

    @Update({"update user set password = #{password} where id =#{id}"})
    void updatePassword(User user);

    @Delete({"delete from user where id = #{id}"})
    void deleteById(int id);
}
