package com.smartshop.services;

import com.smartshop.models.responses.Notification;
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

    private final Map<Long, List<SseEmitter>> clients = new ConcurrentHashMap<>();


    public void subscribeClientToTopic(final SseEmitter client, Long topic) {

        if (!clients.containsKey(topic))
            clients.put(topic, new ArrayList<>());

        clients.get(topic).add(client);
        log.info("Subscribed client to " + topic + " list");
    }

    public void unsubscribeClientFromTopic(final SseEmitter client, Long topic) {

        if(clients.containsKey(topic)) {

            if(clients.get(topic).size() == 0) clients.remove(topic);

            clients.get(topic).removeIf(c -> c.equals(client));
        }

    }


    public void sendByTopic(Long listId, Notification notification) {

        List<SseEmitter> deadClients = new CopyOnWriteArrayList<>();

        if(clients.containsKey(listId)) {

            for(SseEmitter client: clients.get(listId)) {
                try {
                    client.send(SseEmitter.event()
                            .name(String.valueOf(listId))
                            .data(notification)
                    );
                } catch (IOException e) {
                    deadClients.add(client);
                }
            }

        }
        log.info("Client count on topic " + clients.get(listId).size());
        this.clients.get(listId).removeAll(deadClients);
    }




}

