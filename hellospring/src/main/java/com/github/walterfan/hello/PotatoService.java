package com.github.walterfan.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yafan on 4/5/2018.
 */
@Slf4j
@Service
public class PotatoService implements ICRUDS<Potato, UUID> {
    @Override
    public Potato create(Potato entity) {
        return null;
    }

    @Override
    public Potato retrieve(UUID id) {
        return null;
    }

    @Override
    public Potato update(Potato entity) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<Potato> search(List<String> keyValues) {
        Potato potato = new Potato();
        potato.setName("test name");
        potato.setDescription("test description");
        potato.setPriority(1);
        return Arrays.asList(potato);
    }
}
