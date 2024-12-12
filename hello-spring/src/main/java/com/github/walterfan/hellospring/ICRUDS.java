package com.github.walterfan.hellospring;

import java.util.List;

/**
 * Created by yafan on 4/5/2018.
 */
public interface ICRUDS<T, K> {

    T create(T entity);

    T retrieve(K id);

    T update(T entity);

    void delete(K id);

    List<T> search(List<String> keyValues);

}
