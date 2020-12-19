package com.smartshop.resources;


import com.smartshop.dto.ProductSupermarketDto;
import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.ProductSupermarketMapper;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.*;
import com.smartshop.repositories.*;
import com.smartshop.services.SsePushNotification;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.PreRemove;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/test")
public class Test {

    private final Map<String, List<SseEmitter>> clients = new ConcurrentHashMap<>();


    @GetMapping("/subscribe")
    @CrossOrigin
    public SseEmitter subscribe(@RequestParam("topic") String[] topics)  {

        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        try {
            emitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // remove user subscribed to a topic
        emitter.onCompletion(() -> {
            for(String topic: topics) {
                if (clients.containsKey(topic)) {
                    clients.get(topic).remove(emitter);
                }
            }
        });

        for(String topic : topics) {
            if (!clients.containsKey(topic))
                clients.put(topic, new CopyOnWriteArrayList<>());

            clients.get(topic).add(emitter);
        }

        return emitter;
    }


    @PostMapping("/notification")
    @CrossOrigin
    public String list12(@RequestBody String notification) {


        if (!clients.containsKey(notification))
            clients.put(notification, new CopyOnWriteArrayList<>());

        for(SseEmitter c : clients.get(notification)) {
            try {
                c.send(SseEmitter.event().name(notification).data(notification));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       return notification;
    }


}
