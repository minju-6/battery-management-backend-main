package wid.bmsbackend.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
public class MqttConfig {
    private final String mqttBroker;
    private final String subMqttClientId;
    private final String pubMqttClientId;
    private final String mqttUsername;
    private final String mqttPassword;
    private final String mqttTopic;

    public MqttConfig(
            @Value("${mqtt.broker}") String mqttBroker,
            @Value("${mqtt.subMqttClientId}") String subMqttClientId,
            @Value("${mqtt.pubMqttClientId}") String pubMqttClientId,
            @Value("${mqtt.username}") String mqttUsername,
            @Value("${mqtt.password}") String mqttPassword,
            @Value("${mqtt.topic}") String mqttTopic) {
        this.mqttBroker = mqttBroker;
        this.subMqttClientId = subMqttClientId;
        this.pubMqttClientId = pubMqttClientId;
        this.mqttUsername = mqttUsername;
        this.mqttPassword = mqttPassword;
        this.mqttTopic = mqttTopic;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        factory.setServerURIs(mqttBroker);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{mqttBroker});
        mqttConnectOptions.setUserName(mqttUsername);
        mqttConnectOptions.setPassword(mqttPassword.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);

        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(subMqttClientId, mqttClientFactory, mqttTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(pubMqttClientId, mqttClientFactory());
        messageHandler.setAsync(true);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MyGateWay{
        void sentToMqtt(@Header(MqttHeaders.TOPIC) String topic, String data);
    }
}
