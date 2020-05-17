package com.github.walterfan.hellocassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.*;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
public class CassandraClient {

    private String contactPoints;
    private int port;
    private String localDC;
    private String keyspace;
    private String username;
    private String password;
    private int maxConnectionsPerHost = 2048;
    private int usedHostsPerRemote = 1;
    private long reconnectBaseDelayMs = 1000;
    private long reconnectMaxDelayMs = 300 * reconnectBaseDelayMs;

    private Cluster cluster;

    public synchronized Cluster createCluster() {

            DCAwareRoundRobinPolicy loadBanalcePolicy = DCAwareRoundRobinPolicy.builder()
                    .withLocalDc(localDC)
                    .withUsedHostsPerRemoteDc(usedHostsPerRemote)
                    .allowRemoteDCsForLocalConsistencyLevel()
                    .build();

            PoolingOptions poolingOptions =new PoolingOptions();
            poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, maxConnectionsPerHost);
            poolingOptions.setMaxConnectionsPerHost(HostDistance.REMOTE, maxConnectionsPerHost);

            Cluster.Builder clusterBuilder = Cluster.builder()
                    .withReconnectionPolicy(new ExponentialReconnectionPolicy(reconnectBaseDelayMs,reconnectMaxDelayMs))
                    .withRetryPolicy(new LoggingRetryPolicy(DefaultRetryPolicy.INSTANCE))
                    .withPoolingOptions(poolingOptions)
                    .withLoadBalancingPolicy(new TokenAwarePolicy(loadBanalcePolicy))
                    .withPort(port)
                    .addContactPoints(contactPoints.split(","))
                    .withoutJMXReporting();

            if(StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password) ) {
                clusterBuilder.withCredentials(username, password);
            }

            cluster = clusterBuilder.build();
            return cluster;
    }

    public Session connect() {
        return cluster.connect();
    }

    public void close() {
        cluster.close();
    }
}
