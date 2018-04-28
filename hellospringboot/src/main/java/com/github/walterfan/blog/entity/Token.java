package com.github.walterfan.blog.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * Created by walter on 12/03/2017.
 */
@Data
@Entity
@Table(name = "token")
public class Token {

    private static final long FIVE_MINUTES_MILLI_SECONDS = 5 * 60 * 1000;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    //@Column(name = "token_id")
    private String id;

    //@Column(name = "token_type")
    private TokenType type;

    //@Column(name = "token_value")
    private String tokenValue;

    @Column(name = "expiry_time")
    private Date expiryTime;


    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_modified_time")
    private Date lastModifiedTime;

    public void generateToken() {
        this.tokenValue = UUID.randomUUID().toString();
        this.expiryTime = calculateExpiryTime(FIVE_MINUTES_MILLI_SECONDS);
    }

    public Date calculateExpiryTime() {
        return new Date(System.currentTimeMillis() + FIVE_MINUTES_MILLI_SECONDS);
    }

    public Date calculateExpiryTime(long timeoutMilliSeconds) {
        return new Date(System.currentTimeMillis() + timeoutMilliSeconds);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - expiryTime.getTime() > 0;
    }

}
