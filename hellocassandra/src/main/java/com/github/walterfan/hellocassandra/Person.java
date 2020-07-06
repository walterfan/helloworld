package com.github.walterfan.hellocassandra;


import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
@Data
@Table("person")
public class Person {

    @PrimaryKey
    private final String id;

    private final String name;

    private int age;

    public static Person create(String name, int age) {
        return create(UUID.randomUUID().toString(), name, age);
    }

    public static Person create(String id, String name, int age) {
        return new Person(id, name, age);
    }

    @PersistenceConstructor
    public Person(String id, String name, int age) {
        Assert.hasText(id, "'id' must be set");
        Assert.hasText(name, "'name' must be set");

        this.id = id;
        this.name = name;
        this.age = validateAge(age);
    }

    private int validateAge(int age) {
        Assert.isTrue(age > 0, "age must be greater than 0");
        return age;
    }


}