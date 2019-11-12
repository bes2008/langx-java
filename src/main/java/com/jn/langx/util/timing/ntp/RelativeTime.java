package com.jn.langx.util.timing.ntp;

public class RelativeTime {
    private String machineId;
    private long delta;

    public RelativeTime() {
    }

    public RelativeTime(String machineId, long time) {
        this.machineId = machineId;
        this.delta = System.currentTimeMillis() - time;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }


}
