package com.askrida.web.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * MQTT Publisher Service
 * Mengirim pesan ke Arduino via MQTT broker
 */
@Service
public class MqttPublisherService {

    @Value("${mqtt.broker.host}")
    private String brokerHost;

    @Value("${mqtt.broker.port}")
    private int brokerPort;

    @Value("${spring.mqtt.client-id}")
    private String clientId;

    private MqttClient publishClient;
    private Gson gson = new Gson();

    /**
     * Initialize MQTT Publisher Client
     */
    private void initPublisher() {
        try {
            if (publishClient == null || !publishClient.isConnected()) {
                String brokerUrl = "tcp://" + brokerHost + ":" + brokerPort;
                publishClient = new MqttClient(
                    brokerUrl,
                    clientId + "_publisher_" + System.currentTimeMillis(),
                    new MemoryPersistence()
                );

                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setAutomaticReconnect(true);
                options.setMaxInflight(2000);

                publishClient.connect(options);
                System.out.println("[MQTT Publisher] Connected to " + brokerUrl);
            }
        } catch (MqttException e) {
            System.err.println("[MQTT Publisher] Connection error: " + e.getMessage());
        }
    }

    /**
     * Publish fingerprint verification result
     */
    public void publishFingerprintResult(int fpId, boolean accessGranted, String userName) {
        JsonObject json = new JsonObject();
        json.addProperty("fingerprintId", fpId);
        json.addProperty("accessGranted", accessGranted);
        json.addProperty("userName", userName);
        json.addProperty("timestamp", System.currentTimeMillis());

        publish("device/fingerprint/result", json.toString());
    }

    /**
     * Publish sensor data
     */
    public void publishSensorData(String ruangan, double sensorValue) {
        JsonObject json = new JsonObject();
        json.addProperty("ruangan", ruangan);
        json.addProperty("nilaiSensor", sensorValue);
        json.addProperty("timestamp", System.currentTimeMillis());

        publish("device/sensor/data", json.toString());
    }

    /**
     * Publish command ke Arduino
     */
    public void publishCommand(String command, String ruangan) {
        JsonObject json = new JsonObject();
        json.addProperty("command", command);
        json.addProperty("ruangan", ruangan);
        json.addProperty("timestamp", System.currentTimeMillis());

        publish("device/command/" + ruangan, json.toString());
    }

    /**
     * Publish access log
     */
    public void publishAccessLog(String nim, String method, boolean success) {
        JsonObject json = new JsonObject();
        json.addProperty("nim", nim);
        json.addProperty("method", method);
        json.addProperty("success", success);
        json.addProperty("timestamp", System.currentTimeMillis());

        publish("device/logs/access", json.toString());
    }

    /**
     * Generic publish method
     */
    public void publish(String topic, String message) {
        try {
            initPublisher();

            if (publishClient != null && publishClient.isConnected()) {
                publishClient.publish(topic, message.getBytes(), 1, false);
                System.out.println("[MQTT Publisher] Message sent to " + topic);
            } else {
                System.err.println("[MQTT Publisher] Client not connected");
            }
        } catch (MqttException e) {
            System.err.println("[MQTT Publisher] Publish error: " + e.getMessage());
        }
    }

    /**
     * Disconnect MQTT publisher
     */
    public void disconnect() {
        try {
            if (publishClient != null && publishClient.isConnected()) {
                publishClient.disconnect();
                System.out.println("[MQTT Publisher] Disconnected");
            }
        } catch (MqttException e) {
            System.err.println("[MQTT Publisher] Disconnect error: " + e.getMessage());
        }
    }
}
