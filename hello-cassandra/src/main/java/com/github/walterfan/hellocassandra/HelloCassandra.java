package com.github.walterfan.hellocassandra;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

/**
 * Created by yafan on 14/11/2017.
 */

@SpringBootApplication
public class HelloCassandra implements CommandLineRunner {

    private UUID userId = UUID.randomUUID();


    @Autowired
    InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        clearData();
        saveData();
        lookup();
    }

    public void clearData(){
        inventoryRepository.deleteAllByUserId(userId);
    }

    public void saveData(){

        System.out.println("===================Save Customers to Cassandra===================");
        Inventory inventory_1 = new Inventory(userId, UUID.randomUUID(), "book", "Web Scalability", "tech");
        Inventory inventory_2 = new Inventory(userId, UUID.randomUUID(), "book","Ansible", "tech");
        Inventory inventory_3 = new Inventory(userId, UUID.randomUUID(), "book", "Go in action", "tech");
        Inventory inventory_4 = new Inventory(userId, UUID.randomUUID(),"task","Write diary", "work");
        Inventory inventory_5 = new Inventory(userId, UUID.randomUUID(),"task","Write book", "work");
        Inventory inventory_6 = new Inventory(userId, UUID.randomUUID(),"task","Write reading notes", "work");

        // save customers to ElasticSearch
        inventoryRepository.save(inventory_1);
        inventoryRepository.save(inventory_2);
        inventoryRepository.save(inventory_3);
        inventoryRepository.save(inventory_4);
        inventoryRepository.save(inventory_5);
        inventoryRepository.save(inventory_6);
    }

    public void lookup(){

        System.out.println("===================Lookup Inventory from Cassandra by userId===================");
        List<Inventory> list1 = inventoryRepository.findByUserId(userId);
        list1.forEach(System.out::println);

        System.out.println("===================Lookup Inventory from Cassandra by name ===================");
        List<Inventory> list2 = inventoryRepository.findByName("Ansible");
        list2.forEach(System.out::println);


    }

    public static void main(String[] args) {
        SpringApplication.run(HelloCassandra.class, args).close();
    }
}