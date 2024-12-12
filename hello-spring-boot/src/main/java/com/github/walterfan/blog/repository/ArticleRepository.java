package com.github.walterfan.blog.repository;

import com.github.walterfan.blog.entity.Article;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yafan on 17/12/2017.
 */
@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, String> {
}
