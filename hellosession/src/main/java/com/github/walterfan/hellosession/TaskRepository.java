package com.github.walterfan.hellosession;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface TaskRepository extends CrudRepository<Task, Long> {

}
