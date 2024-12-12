package com.github.walterfan.hellosession;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ShoppingCart implements Serializable {
    private String cartId;
    private String userId;
    private List<String> shoppingList;
}
