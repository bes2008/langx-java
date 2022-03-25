package com.jn.langx.util.progress;

import com.jn.langx.event.EventListener;

public class ProgressListener implements EventListener<ProgressEvent> {
    @Override
    public void on(ProgressEvent event) {
        ProgressEventType eventType = event.getEventType();
        if (eventType == ProgressEventType.START) {
            onProgressStart(event);
        } else if (eventType == ProgressEventType.UPDATE) {
            onProgressUpdate(event);
        } else if (eventType == ProgressEventType.FINISH) {
            onProgressFinish(event);
        }
    }

    protected void onProgressStart(ProgressEvent event){

    }

    protected void onProgressUpdate(ProgressEvent event){

    }

    protected void onProgressFinish(ProgressEvent event){

    }
}
