package com.github.walterfan.blog;

import com.github.walterfan.blog.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yafan on 17/12/2017.
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article readArticle(String id) {
        return articleRepository.findOne(id);
    }

    public List<Article> readArticles(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<Article> articles = articleRepository.findAll(pageRequest);
        return articles.getContent();
    }

    public void deleteArticle(String id) {
        articleRepository.delete(id);
    }
}
