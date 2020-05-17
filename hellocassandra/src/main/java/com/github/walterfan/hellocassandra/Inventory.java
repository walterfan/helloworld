package com.github.walterfan.hellocassandra;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;

import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table
public class Inventory {
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID userId;

    @PrimaryKeyColumn(name = "inventory_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private UUID inventoryId;

    @Column("inventory_name")
    private String inventoryName;

    @Column("name")
    private String name;

    @Column("tags")
    private String tags;

    @Column("create_time")
    private Instant createTime;

    @Column("last_modified_time")
    private Instant lastmodifiedTime;

    public Inventory(UUID userId, UUID inventoryId, String inventoryName, String name, String tags) {
        this.userId = userId;
        this.inventoryId = inventoryId;
        this.inventoryName = inventoryName;
        this.name = name;
        this.tags = tags;
        this.createTime = Instant.now();
        this.lastmodifiedTime = Instant.now();
    }



}
