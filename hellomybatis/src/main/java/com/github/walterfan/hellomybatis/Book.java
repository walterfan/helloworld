package com.github.walterfan.hellomybatis;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Book implements Serializable {

    private Long bookId;

    private String isbn;

    private String name;

    private String author;

    private String tags;

    private Date borrowTime;

    private Date returnTime;


}
