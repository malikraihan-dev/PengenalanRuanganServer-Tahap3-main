package com.askrida.web.service.realtime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MonitorRealtimePublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public MonitorRealtimePublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publish(String type, Map<String, Object> payload) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", type);
        event.put("ts", System.currentTimeMillis());
        if (payload != null) {
            event.putAll(payload);
        }
        messagingTemplate.convertAndSend("/topic/monitor", event);
    }
}
