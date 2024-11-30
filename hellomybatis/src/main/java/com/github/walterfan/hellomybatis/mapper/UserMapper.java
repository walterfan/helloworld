package com.github.walterfan.hellomybatis.mapper;

import com.github.walterfan.hellomybatis.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //define the sql in UserMapper.xml
    //@Select("SELECT * FROM users WHERE id = #{id}")
    User findById(int id);

    @Insert("INSERT INTO users(name, email) VALUES(#{name}, #{email})")
    void insert(User user);

    @Update("UPDATE users SET name = #{name}, email = #{email} WHERE id = #{id}")
    void update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(int id);

    @Select("SELECT * FROM users")
    List<User> findAll();
}