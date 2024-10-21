package com.ninjaone.dundie_awards.activity;

import com.ninjaone.dundie_awards.config.DundieProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Answers.RETURNS_SMART_NULLS;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class MessageBrokerTest {

    @Mock(answer = RETURNS_SMART_NULLS)
    private DundieProperties dundieProperties;

    private MessageBroker messageBroker;

    @Nested
    class MessageBrokerStandardTests {

        @BeforeEach
        public void setUp() {
            given(dundieProperties.messageBrokerSize())
                    .willReturn(10);
            given(dundieProperties.messageBrokerTimeoutMs())
                    .willReturn(5000);

            messageBroker = new MessageBroker(dundieProperties);
        }

        @Test
        public void shouldGetAndSendMessages() {
            List<Activity> messagesBefore = messageBroker.getMessages();
            assertThat(messagesBefore).isEmpty();

            Activity testMessage = new Activity(Instant.now(), "test message");
            messageBroker.sendMessage(testMessage);

            List<Activity> messagesAfter = messageBroker.getMessages();
            assertThat(messagesAfter).hasSize(1);
            assertThat(messagesAfter.get(0)).isEqualTo(testMessage);
        }
    }

    @Nested
    class MessageBrokerTimeoutTests {

        @BeforeEach
        public void setUp() {
            given(dundieProperties.messageBrokerSize())
                    .willReturn(1);
            given(dundieProperties.messageBrokerTimeoutMs())
                    .willReturn(1);

            messageBroker = new MessageBroker(dundieProperties);
        }

        @Test
        public void shouldGetAndSendMessages() {
            List<Activity> messagesBefore = messageBroker.getMessages();
            assertThat(messagesBefore).isEmpty();

            Activity testMessage = new Activity(Instant.now(), "test message");
            assertThatThrownBy(() -> {
                for (int x = 0; x < 5; x++) {
                    messageBroker.sendMessage(testMessage);
                }
            }).isInstanceOf(MessageBrokerTimeoutException.class);

            List<Activity> messagesAfter = messageBroker.getMessages();
            assertThat(messagesAfter).hasSize(1);
            assertThat(messagesAfter.get(0)).isEqualTo(testMessage);
        }
    }
}