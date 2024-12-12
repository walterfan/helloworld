package com.github.walterfan.hellometrics;


public interface ServerStatusMBean {
    Long getTimeToLiveSeconds();
    String getProgramName();
    Long getTimestamp();
    Boolean getSwitchStatus();
}