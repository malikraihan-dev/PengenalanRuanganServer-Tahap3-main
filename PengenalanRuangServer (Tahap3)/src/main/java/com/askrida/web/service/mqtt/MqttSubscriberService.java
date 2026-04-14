package com.askrida.web.service.mqtt;

import com.askrida.web.service.realtime.MonitorRealtimePublisher;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MqttSubscriberService {

    @Value("${mqtt.broker.enabled:false}")
    private boolean brokerEnabled;

    @Value("${mqtt.broker.host:localhost}")
    private String brokerHost;

    @Value("${mqtt.broker.port:1883}")
    private int brokerPort;

    @Value("${spring.mqtt.client-id:ruangserver-server}")
    private String clientId;

    @Value("${spring.mqtt.username:}")
    private String username;

    @Value("${spring.mqtt.password:}")
    private String password;

    @Value("${mqtt.topic.fingerprint:device/fingerprint/verify}")
    private String fingerprintVerifyTopic;

    @Value("${mqtt.topic.fingerprint.enroll:device/fingerprint/enroll}")
    private String fingerprintEnrollTopic;

    @Value("${mqtt.topic.sensor:device/sensor/data}")
    private String sensorTopic;

    @Value("${mqtt.topic.logs:device/logs/access}")
    private String logsTopic;

    @Value("${mqtt.topic.status:device/status/+}")
    private String statusTopic;

    private final MonitorRealtimePublisher realtimePublisher;

    private final Gson gson = new Gson();

    private MqttClient subscribeClient;

    public MqttSubscriberService(MonitorRealtimePublisher realtimePublisher) {
        this.realtimePublisher = realtimePublisher;
    }

    @PostConstruct
    public void start() {
        if (!brokerEnabled) {
            System.out.println("[MQTT Subscriber] Disabled (mqtt.broker.enabled=false)");
            return;
        }

        try {
            String brokerUrl = "tcp://" + brokerHost + ":" + brokerPort;
            subscribeClient = new MqttClient(
                    brokerUrl,
                    clientId + "_subscriber_" + System.currentTimeMillis(),
                    new MemoryPersistence()
            );

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            if (username != null && !username.isBlank()) {
                options.setUserName(username);
            }
            if (password != null && !password.isBlank()) {
                options.setPassword(password.toCharArray());
            }

            subscribeClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.err.println("[MQTT Subscriber] Connection lost: " + (cause != null ? cause.getMessage() : "unknown"));
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String body = new String(message.getPayload(), StandardCharsets.UTF_8);

                    Map<String, Object> event = new HashMap<>();
                    event.put("source", "mqtt");
                    event.put("topic", topic);
                    event.put("raw", body);

                    try {
                        JsonElement parsed = JsonParser.parseString(body);
                        event.put("data", gson.fromJson(parsed, Object.class));
                    } catch (Exception ignored) {
                        // keep raw
                    }

                    realtimePublisher.publish("mqtt-message", event);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // no-op (subscriber)
                }
            });

            subscribeClient.connect(options);

            subscribeClient.subscribe(fingerprintVerifyTopic, 1);
            subscribeClient.subscribe(fingerprintEnrollTopic, 1);
            subscribeClient.subscribe(sensorTopic, 1);
            subscribeClient.subscribe(logsTopic, 1);
            subscribeClient.subscribe(statusTopic, 1);

            System.out.println("[MQTT Subscriber] Connected & subscribed to broker " + brokerUrl);

        } catch (MqttException e) {
            System.err.println("[MQTT Subscriber] Failed to start: " + e.getMessage());
        }
    }
}
