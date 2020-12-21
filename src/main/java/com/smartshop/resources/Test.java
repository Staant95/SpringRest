package com.smartshop.resources;


import com.smartshop.services.SsePushNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/test")
@Slf4j
public class Test {

    private final SsePushNotification pushNotification;

    public Test(SsePushNotification pushNotification) {
        this.pushNotification = pushNotification;
    }


    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam("listId") Long listId)  {

        log.info("Client connected");
        final SseEmitter client = new SseEmitter(Long.MAX_VALUE);

        this.pushNotification.subscribeClientToTopic(client, listId);

        client.onCompletion(() -> this.pushNotification.unsubscribeClientFromTopic(client, listId));

        client.onTimeout(() -> this.pushNotification.unsubscribeClientFromTopic(client, listId));

        client.onError((err) -> this.pushNotification.unsubscribeClientFromTopic(client, listId));

        return client;
    }



}
