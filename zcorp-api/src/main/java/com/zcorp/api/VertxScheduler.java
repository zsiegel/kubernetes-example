package com.zcorp.api;

import rx.Scheduler;

public class VertxScheduler {

    private Scheduler worker, eventLoop;

    public VertxScheduler(Scheduler worker, Scheduler eventLoop) {
        this.worker = worker;
        this.eventLoop = eventLoop;
    }

    public Scheduler worker() {
        return worker;
    }

    public Scheduler eventLoop() {
        return eventLoop;
    }
}
