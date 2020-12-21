package com.smartshop.resources;


import com.smartshop.models.Notification;
import com.smartshop.models.NotificationAction;
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
    public SseEmitter subscribe(@RequestParam("topic") String[] topics)  {
        log.info("Client connected");
        final SseEmitter client = new SseEmitter(Long.MAX_VALUE);

        for(String topic: topics)
            this.pushNotification.subscribeClientToTopic(client, topic);


        // remove user subscribed to a topic
        client.onCompletion(() -> {
            for(String topic: topics)
                this.pushNotification.unsubscribeClientFromTopic(client, topic);
        });

        client.onTimeout(() -> {
            for(String topic: topics)
                this.pushNotification.unsubscribeClientFromTopic(client, topic);
        });

        return client;
    }


    @PostMapping("/notification")
    public void list12(@RequestBody String message) {

        if(this.pushNotification.topicHasAnySubscriber("List1")) {
            Notification notification = new Notification(message.substring(9, message.length() - 2), NotificationAction.UPDATED);
            this.pushNotification.sendByTopic("List1", notification);

        }

    }


}
