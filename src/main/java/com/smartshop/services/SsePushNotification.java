package com.smartshop.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SsePushNotification {

    //thread safe map implementation
    // key -> shoplistId, value
//    final Map<SseEmitter, ?> emitters = new ConcurrentHashMap<>();

    public final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    public static int count = 0;

    @Async
    public void sendNotification(String notification) {

        emitters.forEach(emitter -> {
            try {
                emitter.send(notification);
            } catch (IOException e) {

                log.debug("IOException occured");
            }
        });



    }



    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }
}

