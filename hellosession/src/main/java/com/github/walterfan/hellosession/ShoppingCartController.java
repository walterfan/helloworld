package com.github.walterfan.hellosession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class ShoppingCartController {
    @RequestMapping(value = "/carts/{cartId}", method = RequestMethod.GET)
    public ShoppingCart getShoppingCart (HttpServletRequest request, @PathVariable String cartId){
        HttpSession httpSession = request.getSession();

        ShoppingCart cart = (ShoppingCart) httpSession.getAttribute(cartId);

        log.info("getShoppingCart sessionId={}, cartId={}", httpSession.getId(), cartId);
        if(null != cart)
            log.info("cart={}", cart);
        return cart;
    }

    @RequestMapping(value = "/carts/{cartId}" , method = RequestMethod.PUT)
    public ShoppingCart setShoppingCart (HttpServletRequest request, @PathVariable String cartId, @RequestBody ShoppingCart cart){
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(cartId, cart);
        log.info("setShoppingCart sessionId={}, cart={}", httpSession.getId(), cart);
        return cart;
    }


    @RequestMapping(value = "/session" , method = RequestMethod.GET)
    public Map<String, String> getVersionInfo (HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        Enumeration<String> names = httpSession.getAttributeNames();
        Map<String, String> map = new HashMap<>();

        map.put("sessionId", httpSession.getId());

        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String value = String.valueOf(httpSession.getAttribute(key));
            map.put(key, value);
        }

        return map;
    }

}