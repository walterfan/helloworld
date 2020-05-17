package com.github.walterfan.hellocassandra;

import java.net.InetSocketAddress;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import org.springframework.cassandra.core.RowMapper;
import org.springframework.cassandra.support.exception.CassandraTypeMismatchException;
import org.springframework.data.cassandra.core.CassandraTemplate;

import org.springframework.data.cassandra.mapping.Table;


import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yafan on 15/11/2017.
 */

@Slf4j
public class CassandraTemplateExample {

    private String username = "test";

    private String password  = "pass";

    static final String AGE_COLUMN_NAME = "age";
    static final String ID_COLUMN_NAME = "id";
    static final String NAME_COLUMN_NAME = "name";

    CassandraTemplate template;
    CassandraClient client;


    CassandraTemplateExample(String hostnames, int port, String localDC, String keyspace) {
        client = CassandraClient.builder()
                .contactPoints(hostnames)
                .port(port)
                .localDC(localDC)
                .keyspace(keyspace)
                .username(username)
                .password(password)
                .maxConnectionsPerHost(2048)
                .reconnectBaseDelayMs(1000)
                .reconnectMaxDelayMs(600_000)
                .build();


    }


    private void testCql() {
        try(Session session = client.connect()) {

            template = new CassandraTemplate(session);
            testCrud();
            testTransaction();
            testPagination();
        }


    }

    private void execute(String cql) {

        log.info("execute {}" , cql);
        if(cql.startsWith("select")) {
            List<?> aList = template.select(cql, List.class);
            aList.forEach(System.out::println);
        } else {
            template.execute(cql);
        }


    }

    private void testCrud() {
        log.info("--------- testCrud ----------------");
        Person thePerson = template.insert(Person.create("Walter Fan", 37));


        log.info("Inserted [{}]", thePerson);

        Person queriedPerson = queryPersonById(thePerson.getId());
        assertThat(queriedPerson).isNotSameAs(thePerson);
        assertThat(queriedPerson).isEqualTo(thePerson);
    }


    private Person queryPersonById(String id) {
        Select personQuery = selectPerson(id);


        log.info("CQL SELECT [{}]", personQuery);

        Person queriedPerson = template.queryForObject(personQuery, personRowMapper());

        log.info("* Query Result [{}]", queriedPerson);

        return queriedPerson;
    }

    private void testTransaction() {
        System.out.println("--------- testTransaction ----------------");
        template.execute("insert into person(id, name, age) values ('a', 'alice', 20)");
        template.execute("update person set name = 'ada' where id='a' ");
        template.execute("update person set name = 'adam' where id='a' IF name ='alice'");

        template.execute("insert into person(id, name, age) values ('b', 'bob', 21)");
        template.execute("insert into person(id, name, age) values ('c', 'carl', 22)");

        template.execute("insert into person(id, name, age) values ('d', 'david', 31)");
        template.execute("insert into person(id, name, age) values ('d', 'dean', 32) ");
        template.execute("insert into person(id, name, age) values ('d', 'dog', 33)  IF NOT EXISTS");


        Person a = queryPersonById( "a");
        Person d = queryPersonById( "d");

        assertThat(a.getName().equals("ada"));
        assertThat(a.getAge() == 20);
        assertThat(a.getName().equals("dean"));

        template.execute("delete from person where id in ('a','b','c','d')");


    }


    private void testPagination() {
        System.out.println("--------- testPagination ----------------");
        String user_id = "e7d6038e-7a07-4dca-a98f-939428ded582";

        String[] inventoryIDs = {"01786fd5-92ef-491c-b64e-7d83a624b95d",
                "12a7cd81-d384-44d4-8919-5fa092f6427f",
                "1cd52315-fba7-4461-99b3-8e44b7b589e8",
                "2a7a3b39-f080-40c1-b5c8-08b743d66076",
                "2b2b458e-75e4-4709-9853-8491cfeb13e9"};

        int i = 0;
        for(String inventory_id: inventoryIDs) {

            String cql = String.format("insert into inventory(user_id, inventory_id, inventory_name, name, tags, create_time, last_modified_time) " +
                            "values (%s, %s, '%s','%s', '%s', '%s', '%s')",
                    user_id, inventory_id, "book", "posa" + (++i), "tech", Instant.now().toString(), Instant.now().toString());
            log.info("execute {}", cql);
            template.execute(cql);

        }


        List<Inventory> inventories0 = template.select(String.format("select * from inventory WHERE user_id=%s", user_id), Inventory.class);

        List<Inventory> inventories2 = template.select(String.format("select * from inventory WHERE user_id=%s and inventory_id > %s ALLOW FILTERING", user_id, inventoryIDs[2]), Inventory.class);


        System.out.println("---------all inventories----------------");
        inventories0.forEach(System.out::println);

        System.out.println("---------filterd inventories----------------");

        inventories2.forEach(System.out::println);

    }

    public InetSocketAddress newSocketAddress(String hostname, int port) {
        return new InetSocketAddress(hostname, port);
    }



    public void close() {
        this.client.close();
    }

    protected static RowMapper<Person> personRowMapper() {
        return new RowMapper<Person>() {
            public Person mapRow(Row row, int rowNum) throws DriverException {
                try {

                    log.debug("row [{}] @ index [{}]", row, rowNum);


                    Person person = Person.create(row.getString(ID_COLUMN_NAME),
                            row.getString(NAME_COLUMN_NAME), row.getInt(AGE_COLUMN_NAME));


                    log.debug("person [{}]", person);


                    return person;
                }
                catch (Exception e) {
                    throw new CassandraTypeMismatchException(String.format(
                            "failed to map row [%1$] @ index [%2$d] to object of type [%3$s]",
                            row, rowNum, Person.class.getName()), e);
                }
            }
        };
    }

    protected static Select selectPerson(String personId) {
        Select selectStatement = QueryBuilder.select().from(toTableName(Person.class));
        selectStatement.where(QueryBuilder.eq(ID_COLUMN_NAME, personId));
        return selectStatement;
    }

    @SuppressWarnings("unused")
    protected static String toTableName(Object obj) {
        return toTableName(obj.getClass());
    }

    protected static String toTableName(Class<?> type) {
        Table tableAnnotation = type.getAnnotation(Table.class);

        return (tableAnnotation != null && StringUtils.isNotEmpty(tableAnnotation.value())
                ? tableAnnotation.value() : type.getSimpleName());
    }

    public static void main(String[] args) throws Exception {

        CassandraTemplateExample exam = new CassandraTemplateExample("10.224.38.139", 9042, "HF1","walter_apjc");
        exam.testCql();
        exam.close();
    }

}
