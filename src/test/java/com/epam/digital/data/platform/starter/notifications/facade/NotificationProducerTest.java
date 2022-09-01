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

package com.epam.digital.data.platform.starter.notifications.facade;

import com.epam.digital.data.platform.starter.kafka.config.properties.KafkaProperties;
import com.epam.digital.data.platform.notification.dto.NotificationContextDto;
import com.epam.digital.data.platform.notification.dto.NotificationDto;
import com.epam.digital.data.platform.notification.dto.NotificationRecordDto;
import com.epam.digital.data.platform.starter.notifications.producer.NotificationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationProducerTest {

    private NotificationFacade notificationFacade;

    @InjectMocks
    private NotificationProducer notificationProducer;

    @Mock
    private KafkaProperties kafkaProperties;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    void setUp() {
        notificationFacade = new KafkaNotificationFacade(notificationProducer);
    }

    @Test
    void shouldSendNotification() {
        when(kafkaProperties.getTopics()).thenReturn(Map.of("user-notifications", "topic"));
        NotificationRecordDto notificationRecord = createNotificationRecord();

        notificationFacade.sendNotification(notificationRecord);

        verify(kafkaTemplate).send("topic", notificationRecord);
    }

    private NotificationRecordDto createNotificationRecord() {
        return NotificationRecordDto.builder()
                .context(NotificationContextDto.builder()
                        .application("bpms")
                        .businessActivity("Activity_1")
                        .businessActivityInstanceId("e2503352-bcb2-11ec-b217-0a580a831053")
                        .businessProcess("add-lab")
                        .businessProcessDefinitionId("add-lab:5:ac2dfa60-bbe2-11ec-8421-0a58")
                        .businessProcessInstanceId("e2503352-bcb2-11ec-b217-0a580a831054")
                        .build())
                .notification(NotificationDto.builder()
                        .recipient("testuser")
                        .subject("sign notification")
                        .template("template-id")
                        .templateModel(Map.of("key", "value"))
                        .build())
                .build();
    }
}
