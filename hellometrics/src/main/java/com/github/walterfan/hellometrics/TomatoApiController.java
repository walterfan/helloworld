package com.github.walterfan.hellometrics;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @Author: Walter Fan
 **/
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TomatoApiController {

    @Autowired
    private TomatoService tomatoService;


    @RequestMapping(value = "/tomatoes", method = RequestMethod.POST)
    @MetricsAnnotation(name = "CreateTomato")
    public TomatoDTO create(@RequestBody TomatoDTO potatoRequest) {

        log.info("create {}", potatoRequest);

        return tomatoService.create(potatoRequest);

    }


    @RequestMapping(value = "/tomatoes/{id}", method = RequestMethod.GET)
    @MetricsAnnotation(name = "RetrieveTomato")
    public TomatoDTO retrieve(@PathVariable UUID id) {
        return tomatoService.retrieve(id);
    }


    @RequestMapping(value = "/tomatoes/{id}", method = RequestMethod.PUT)
    @MetricsAnnotation(name = "UpdateTomato")
    public void update(@RequestBody TomatoDTO potatoDto) {
        tomatoService.update(potatoDto);
    }


    @RequestMapping(value = "/tomatoes/{id}", method = RequestMethod.DELETE)
    @MetricsAnnotation(name = "DeleteTomato")
    public void delete(@PathVariable UUID id) {
        tomatoService.delete(id);
    }


    @RequestMapping(value = "/tomatoes", method = RequestMethod.GET)
    @MetricsAnnotation(name = "ListTomatos")
    public List<TomatoDTO> list(@RequestParam(value = "userId", required = false) UUID potatoId,
                                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {

        return tomatoService.list(potatoId, page, size);
    }


}
