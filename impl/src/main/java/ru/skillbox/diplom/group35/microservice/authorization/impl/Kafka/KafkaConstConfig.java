package ru.skillbox.diplom.group35.microservice.authorization.impl.Kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class KafkaConstConfig {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.topic.login}")
    private String requestLogin;

    @Value(value = "${kafka.topic.registration}")
    private String requestRegistration;

    @Value(value = "${kafka.topic.partition-count}")
    private Integer partitionCount;

    @Value(value = "${kafka.topic.replication-factor}")
    private Short replicationFactor;

    @Value(value = "${kafka.producer.retries}")
    private Integer retries;
}
