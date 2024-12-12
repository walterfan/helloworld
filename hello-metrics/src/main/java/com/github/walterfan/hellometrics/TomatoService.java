package com.github.walterfan.hellometrics;

import java.util.List;
import java.util.UUID;

/**
 * @Author: Walter Fan
 **/
public interface TomatoService {

    TomatoDTO create(TomatoDTO TomatoRequest) ;

    TomatoDTO retrieve(UUID id) ;

    TomatoDTO update(TomatoDTO TomatoDto);

    void delete(UUID id);

    List<TomatoDTO> list(UUID potatoId, Integer page, Integer size);
}
