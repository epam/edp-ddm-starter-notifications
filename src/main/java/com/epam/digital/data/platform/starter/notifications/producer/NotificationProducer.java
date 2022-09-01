/*
 * Copyright 2022 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.starter.notifications.producer;


import com.epam.digital.data.platform.starter.kafka.config.properties.KafkaProperties;
import com.epam.digital.data.platform.notification.dto.NotificationRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationProducer {

    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(NotificationRecordDto notificationRecord) {
        var topic = kafkaProperties.getTopics().get("user-notifications");
        kafkaTemplate.send(topic, notificationRecord);
        log.info("Kafka event sent, context: {}", notificationRecord.getContext());
    }
}