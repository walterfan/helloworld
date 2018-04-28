package com.github.walterfan.blog;

import com.github.walterfan.blog.entity.Article;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by yafan on 17/12/2017.
 */
public interface ArticleRepository extends PagingAndSortingRepository<Article, String> {
}
