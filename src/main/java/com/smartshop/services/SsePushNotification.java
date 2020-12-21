package com.smartshop.services;

import com.smartshop.models.Notification;
import lombok.extern.slf4j.Slf4j;
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

    private final Map<String, List<SseEmitter>> clients = new ConcurrentHashMap<>();

    public boolean topicHasAnySubscriber(String topic) {
        if(clients.containsKey(topic))
            return clients.get(topic).size() > 0;
        return false;
    }

    public void subscribeClientToTopic(final SseEmitter client, String topic) {

        if (!clients.containsKey(topic))
            clients.put(topic, new ArrayList<>());

        clients.get(topic).add(client);
        log.info("Subscribed client to " + topic + " topic");
        log.info("Map size for " + topic + " is " + clients.get(topic).size());
    }

    public void unsubscribeClientFromTopic(final SseEmitter client, String topic) {

        if(clients.containsKey(topic)) {

            if(clients.get(topic).size() == 0) clients.remove(topic);

            clients.get(topic).removeIf(c -> c.equals(client));

            log.info("Unsubscribed client. Map size for " + topic + " is " + clients.get(topic).size());
        }

    }


    public void sendByTopic(String topic, Notification notification) {
        List<SseEmitter> deadClients = new CopyOnWriteArrayList<>();
        if(clients.containsKey(topic)) {

            for(SseEmitter client: clients.get(topic)) {
                try {
                    client.send(SseEmitter.event()
                            .name(topic)
                            .data(notification)
                    );
                } catch (IOException e) {
                    deadClients.add(client);
                }
            }

        }
        log.info("Message sended... Clients map size -> " + clients.get(topic).size());
        this.clients.get(topic).removeAll(deadClients);
    }




}

