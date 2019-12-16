package com.github.walterfan.hellometrics;


public class ServerStatus implements ServerStatusMBean {
    private Long timeToLiveSeconds;
    private String programName;
    private Long timestamp;
    private Boolean switchStatus;
    private Thread backgroundThread;

    public ServerStatus(String programName) {
        // First we initialize all the metrics
        this.backgroundThread = new Thread();
        this.programName = programName;
        this.timeToLiveSeconds = 0L;
        this.timestamp = System.currentTimeMillis() / 1000L;
        this.switchStatus = false;

        // We will use a background thread to update the metrics
        this.backgroundThread = new Thread(() -> {
            try {
                while (true) {
                    // Every second we update the metrics
                    timeToLiveSeconds += 1;
                    timestamp += 1;
                    switchStatus = !switchStatus;
                    Thread.sleep(1000L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.backgroundThread.setName("backgroundThread");
        this.backgroundThread.start();
    }


    // Through this getters, defined in the interface SystemStatusMBean,
    // all the metrics will be automatically retrieved

    @Override
    public Long getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    @Override
    public String getProgramName() {
        return programName;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public Boolean getSwitchStatus() {
        return switchStatus;
    }
}