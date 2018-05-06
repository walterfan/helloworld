package com.github.walterfan.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yafan on 22/4/2018.
 */
@RestController
@RequestMapping("/api/v1")
public class PotatoApiController implements ICRUDS<Potato, UUID> {

    @Autowired
    private PotatoService potatoService;



    @RequestMapping(value = "/potatoes", method = RequestMethod.POST)
    @Override
    public Potato create(Potato entity) {
        return potatoService.create(entity);
    }

    @RequestMapping(value = "/potatoes/{id}", method = RequestMethod.GET)
    @Override
    public Potato retrieve(@PathVariable UUID id) {
        return potatoService.retrieve(id);
    }

    @RequestMapping(value = "/potatoes/{id}", method = RequestMethod.PUT)
    @Override
    public Potato update(@RequestBody Potato entity) {
        return potatoService.update(entity);
    }

    @RequestMapping(value = "/potatoes/{id}", method = RequestMethod.DELETE)
    @Override
    public void delete(@PathVariable UUID id) {
        potatoService.delete(id);
    }

    @RequestMapping(value = "/potatoes/{searchType}", method = RequestMethod.GET)
    @Override
    public List<Potato> search(@MatrixVariable List<String> keyValues) {
        return potatoService.search(keyValues);
    }
}
