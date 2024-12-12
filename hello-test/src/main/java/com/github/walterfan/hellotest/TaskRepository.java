package com.github.walterfan.hellotest;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by yafan on 19/11/2017.
 */
@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, UUID> {
}
