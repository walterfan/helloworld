package com.github.walterfan.blog;

import com.github.walterfan.blog.dto.ArticleRequest;
import com.github.walterfan.blog.dto.ArticleResponse;
import com.github.walterfan.blog.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by yafan on 17/12/2017.
 */
@RestController
@RequestMapping("/blog/api/v1")
@Slf4j
public class ArticleApiController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/articles" , method = RequestMethod.POST)
    public Article createArticle(@RequestBody @Valid Article article) {
        log.info("createMeetingSession: " + article);
        return articleService.createArticle(article);
    }

    @RequestMapping(value = "/articles/{id}" , method = RequestMethod.PUT)
    public Article updateArticle(@PathVariable("id") String articleId, @RequestBody @Valid Article article) {
        article.setId(articleId);
        log.info("updateArticle: " + article);
        return articleService.createArticle(article);
    }

    @RequestMapping(value = "/articles/{id}" , method = RequestMethod.GET)
    public Article readArticle(@PathVariable("id") String articleId) {
        log.info("readArticle: " + articleId);
        return articleService.readArticle(articleId);
    }

    @RequestMapping(value = "/articles" , method = RequestMethod.GET)
    public List<Article> readArticle(@RequestParam(value="page", required = false) Integer page, @RequestParam(value="size", required = false) Integer size) {
        if(null == page) {
            page = 0;
        }
        if(null == size) {
            size = 20;
        }
        log.info("readArticles:{}, {} " , page, size);
        return articleService.readArticles(page, size);
    }

    @RequestMapping(value = "/articles/{id}" , method = RequestMethod.DELETE)
    public void deleteArticle(@PathVariable("id") String articleId) {
        log.info("deleteArticle: " + articleId);
        articleService.deleteArticle(articleId);
    }
}
