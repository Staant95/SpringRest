package com.smartshop.services;

import com.smartshop.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SsePushNotification {

    private final Map<String, List<SseEmitter>> clients = new ConcurrentHashMap<>();

    public boolean topicHasAnySubscriber(String topic) {
        if(clients.containsKey(topic))
            return clients.get(topic).size() > 0;
        return false;
    }

    public void subscribeClientToTopic(SseEmitter client, String topic) {

        if (!clients.containsKey(topic))
            clients.put(topic, new CopyOnWriteArrayList<>());

        clients.get(topic).add(client);
        log.info("MAP SIZE: " + clients.get(topic).size());
    }

    public void unsubscribeClientFromTopic(SseEmitter client, String topic) {

        if(clients.containsKey(topic))
            clients.get(topic).removeIf(c -> c.equals(client));

    }



    public void sendByTopic(String topic, Notification notification) {

        if(clients.containsKey(topic)) {

            for(SseEmitter client: clients.get(topic)) {
                try {
                    client.send(SseEmitter.event().name(topic).data(notification));
                    clients.get(topic).clear();
                    client.complete();
                } catch (IOException e) {
                    client.completeWithError(e);
                    log.info("IOException during sending...");
                }
            }

        }
        log.info("MAP SIZE: " + clients.get(topic).size());
    }




}

