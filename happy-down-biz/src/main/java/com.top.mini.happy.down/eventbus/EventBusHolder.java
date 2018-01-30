package com.top.mini.happy.down.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by xugang on 17/6/26.
 */
@Component
public class EventBusHolder {
    private EventBus asyncEventBus;
    @PostConstruct
    public void init(){
        Executor executor = Executors.newCachedThreadPool();
        asyncEventBus = new AsyncEventBus(executor);
    }

    public EventBus getAsyncEventBus() {
        return asyncEventBus;
    }

}
