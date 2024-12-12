package com.github.walterfan.hellometrics;

import com.sun.management.GarbageCollectionNotificationInfo;
import io.micrometer.core.instrument.Meter;
import lombok.extern.slf4j.Slf4j;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * @Author: Walter Fan
 * @Date: 24/12/2019, Tue
 **/
@Slf4j
public class GcMetricsReporter implements NotificationListener {
    @Override
    public void handleNotification(Notification notification, Object handback) {
        GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

        long duration = info.getGcInfo().getDuration();
        String gcAction = info.getGcAction();
        long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();


        long id = info.getGcInfo().getId();
        Map<String, MemoryUsage> memoryUsageAfterGc = info.getGcInfo().getMemoryUsageAfterGc();
        log.info(String.format("After Major GC(id=%d): %s", id, memoryUsageAfterGc));

    }


    public static void main(String[] args) {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            log.info("GC bean: {} , isValid: {}, memoryPools: {}" , gcbean.getName(), gcbean.isValid(), gcbean.getMemoryPoolNames());
            if (!(gcbean instanceof NotificationEmitter))
                continue;
            log.info("objectName: {}", gcbean.getObjectName());
            NotificationListener listener = new GcMetricsReporter();
            NotificationFilter filter = new NotificationFilter()  {

                @Override
                public boolean isNotificationEnabled(Notification notification) {
                    return GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION.equals(notification.getType());
                }
            };
            Object handback = "hellemetrics";

            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, filter, handback);

        }
        //Meter meter
    }
}
