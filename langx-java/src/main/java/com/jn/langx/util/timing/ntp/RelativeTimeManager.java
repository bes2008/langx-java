package com.jn.langx.util.timing.ntp;

import java.util.concurrent.ConcurrentHashMap;

public class RelativeTimeManager {
    private final ConcurrentHashMap<String, RelativeTime> cache = new ConcurrentHashMap<String, RelativeTime>();

    public RelativeTimeManager() {
    }

    public void add(String machineId, long machineTime) {
        if (machineId != null) {
            RelativeTime time = new RelativeTime(machineId, machineTime);
            add(time);
        }
    }

    public void add(RelativeTime time) {
        if (time != null) {
            cache.put(time.getMachineId(), time);
        }
    }

    public RelativeTime remove(String machineId) {
        if (machineId != null) {
            return cache.remove(machineId);
        }
        return null;
    }

    public boolean hasMachine(String machineId) {
        if (machineId == null) {
            return false;
        }
        return cache.get(machineId) != null;
    }

    public long getDelteTime(String machineId) {
        if (hasMachine(machineId)) {
            RelativeTime t = cache.get(machineId);
            return t == null ? 0 : t.getDelta();
        }
        return 0;
    }

    public long getMachineCurrentTime(String machineId) {
        return getMachineRelativeTime(machineId, System.currentTimeMillis());
    }

    /**
     * get a machine's time based on specified time
     *
     * @param machineId
     * @param specifiedTime specified time at current machine
     */
    public long getMachineRelativeTime(String machineId, long specifiedTime) {
        return specifiedTime - getDelteTime(machineId);
    }
}
