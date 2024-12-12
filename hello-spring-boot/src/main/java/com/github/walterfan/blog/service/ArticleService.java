package com.github.walterfan.blog.service;

import com.github.walterfan.blog.entity.Article;
import com.github.walterfan.blog.repository.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Article> readArticle(String id) {
        return articleRepository.findById(id);
    }

    public List<Article> readArticles(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<Article> articles = articleRepository.findAll(pageRequest);
        return articles.getContent();
    }

    public void deleteArticle(String id) {
        articleRepository.deleteById(id);
    }
}
