package com.app.monitoring.notifications;

import com.app.monitoring.BaseIntegrationTest;
import com.app.monitoring.metrics.dto.MetricRequest;
import com.app.monitoring.notifications.dto.AlertRuleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class NotificationsControllerTest extends BaseIntegrationTest {

    private final String NOTIFICATION_URL = "/api/notifications";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAlertRule() throws Exception {
        AlertRuleRequest request = AlertRuleRequest.builder()
                .metricName("cpu.usage")
                .serviceName("auth-service")
                .threshold(80.0)
                .direction(ThresholdDirection.ABOVE)
                .cooldownSeconds(300L)
                .build();

        mockMvc.perform(post(NOTIFICATION_URL + "/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.metricName").value("cpu.usage"))
                .andExpect(jsonPath("$.threshold").value(80.0));
    }

    @Test
    void shouldFireNotificationWhenThresholdBreached() throws Exception {
        AlertRuleRequest ruleRequest = AlertRuleRequest.builder()
                .metricName("cpu.usage")
                .serviceName("test-service")
                .threshold(80.0)
                .direction(ThresholdDirection.ABOVE)
                .cooldownSeconds(300L)
                .build();

        mockMvc.perform(post( NOTIFICATION_URL + "/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleRequest)))
                .andExpect(status().isCreated());

        MetricRequest metricRequest = MetricRequest.builder()
                .name("cpu.usage")
                .value(95.0)
                .serviceName("test-service")
                .timestamp(Instant.now())
                .build();

        mockMvc.perform(post("/api/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(metricRequest)))
                .andExpect(status().isCreated());

        await().atMost(5, SECONDS).untilAsserted(() ->
                mockMvc.perform(get(NOTIFICATION_URL))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.totalElements").value(1))
                        .andExpect(jsonPath("$.content[0].triggerValue").value(95.0))
        );
    }

    @Test
    void shouldNotFireNotificationWhenThresholdNotBreached() throws Exception {
        AlertRuleRequest ruleRequest = AlertRuleRequest.builder()
                .metricName("memory.usage")
                .serviceName("cache-service")
                .threshold(80.0)
                .direction(ThresholdDirection.ABOVE)
                .cooldownSeconds(300L)
                .build();

        mockMvc.perform(post(NOTIFICATION_URL + "/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleRequest)))
                .andExpect(status().isCreated());

        MetricRequest metricRequest = MetricRequest.builder()
                .name("memory.usage")
                .value(50.0)
                .serviceName("cache-service")
                .timestamp(Instant.now())
                .build();

        mockMvc.perform(post("/api/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(metricRequest)))
                .andExpect(status().isCreated());

        await().atMost(5, SECONDS).untilAsserted(() ->
                mockMvc.perform(get(NOTIFICATION_URL))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.totalElements").value(0))
        );
    }
}
