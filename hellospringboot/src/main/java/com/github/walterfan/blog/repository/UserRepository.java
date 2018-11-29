package com.github.walterfan.blog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.github.walterfan.blog.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

}
