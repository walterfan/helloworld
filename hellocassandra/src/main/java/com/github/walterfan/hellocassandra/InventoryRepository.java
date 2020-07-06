package com.github.walterfan.hellocassandra;


import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface InventoryRepository extends CrudRepository<Inventory, String> {

    @Query(value="SELECT * FROM inventory WHERE name=?0")
    public List<Inventory> findByName(String name);

    @Query("SELECT * FROM inventory WHERE tags = ?0")
    public List<Inventory> findByTags(String tags);

    @Query("SELECT * FROM inventory WHERE user_id = ?0")
    public List<Inventory> findByUserId(UUID userId);


    public void deleteAllByUserId(UUID userId);


    @Query("SELECT * FROM inventory WHERE user_id = ?0 and inventory_id= ?1 ")
    public List<Inventory> findByUserAndInventoryId(UUID userId, UUID inventory_id);


}