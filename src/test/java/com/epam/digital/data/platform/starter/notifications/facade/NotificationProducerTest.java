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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.digital.data.platform.notification.dto.ChannelObject;
import com.epam.digital.data.platform.notification.dto.NotificationContextDto;
import com.epam.digital.data.platform.notification.dto.Recipient;
import com.epam.digital.data.platform.notification.dto.UserNotificationDto;
import com.epam.digital.data.platform.notification.dto.UserNotificationMessageDto;
import com.epam.digital.data.platform.starter.kafka.config.properties.KafkaProperties;
import com.epam.digital.data.platform.starter.notifications.producer.NotificationProducer;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class NotificationProducerTest {

  private UserNotificationFacade notificationFacade;

  @InjectMocks
  private NotificationProducer notificationProducer;

  @Mock
  private KafkaProperties kafkaProperties;
  @Mock
  private KafkaTemplate<String, Object> kafkaTemplate;

  @BeforeEach
  void setUp() {
    notificationFacade = new UserKafkaNotificationFacade(notificationProducer);
  }

  @Test
  void shouldSendNotification() {
    when(kafkaProperties.getTopics()).thenReturn(Map.of("user-notifications", "topic"));
    var notificationRecord = createNotificationRecord();

    notificationFacade.sendNotification(notificationRecord);

    verify(kafkaTemplate).send("topic", notificationRecord);
  }

  private UserNotificationMessageDto createNotificationRecord() {
    return UserNotificationMessageDto.builder()
        .recipients(List.of(Recipient.builder()
            .channels(List.of(ChannelObject.builder()
                .channel("EMAIL")
                .email("email@test.com")
                .build()))
            .id("testuser")
            .build()))
        .context(NotificationContextDto.builder()
            .application("bpms")
            .businessActivity("Activity_1")
            .businessActivityInstanceId("e2503352-bcb2-11ec-b217-0a580a831053")
            .businessProcess("add-lab")
            .businessProcessDefinitionId("add-lab:5:ac2dfa60-bbe2-11ec-8421-0a58")
            .businessProcessInstanceId("e2503352-bcb2-11ec-b217-0a580a831054")
            .build())
        .notification(UserNotificationDto.builder()
            .templateName("template-id")
            .title("sign notification")
            .ignoreChannelPreferences(true)
            .build())
        .build();
  }
}
