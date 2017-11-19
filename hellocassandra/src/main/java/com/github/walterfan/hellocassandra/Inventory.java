package com.github.walterfan.hellocassandra;

import jnr.ffi.annotations.In;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.time.Instant;
import java.util.UUID;


/**
 * Created by yafan on 14/11/2017.
 */
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


    public Inventory() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(UUID inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getLastmodifiedTime() {
        return lastmodifiedTime;
    }

    public void setLastmodifiedTime(Instant lastmodifiedTime) {
        this.lastmodifiedTime = lastmodifiedTime;
    }


    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "userId=" + userId +
                ", inventoryId=" + inventoryId +
                ", inventoryName='" + inventoryName + '\'' +
                ", name='" + name + '\'' +
                ", tags='" + tags + '\'' +
                ", createTime=" + createTime +
                ", lastmodifiedTime=" + lastmodifiedTime +
                '}';
    }
}
