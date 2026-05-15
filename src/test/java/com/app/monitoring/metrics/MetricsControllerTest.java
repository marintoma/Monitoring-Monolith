package com.app.monitoring.metrics;

import com.app.monitoring.BaseIntegrationTest;
import com.app.monitoring.metrics.dto.MetricRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MetricsControllerTest extends BaseIntegrationTest {

    private final String METRICS_URL = "/api/metrics";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldIngestMetricAndReturn201() throws Exception {
        MetricRequest request = MetricRequest.builder()
                .name("cpu.usage")
                .value(72.5)
                .serviceName("auth-service")
                .timestamp(Instant.now())
                .build();

        mockMvc.perform(post(METRICS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("cpu.usage"))
                .andExpect(jsonPath("$.value").value(72.5))
                .andExpect(jsonPath("$.serviceName").value("auth-service"))
                .andExpect(jsonPath("$.id").isNumber());


    }

    @Test
    void shouldReturnBadRequestWhenFieldsMissing() throws Exception {
        mockMvc.perform(post(METRICS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldQueryMetricsByServiceName() throws Exception {
        MetricRequest request = MetricRequest.builder()
                .name("memory.usage")
                .value(55.0)
                .serviceName("payment-service")
                .timestamp(Instant.now())
                .build();

        mockMvc.perform(post(METRICS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(METRICS_URL + "/service/payment-service"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].serviceName").value("payment-service"));
    }

    @Test
    void shouldQueryMetricsByTimeRange() throws Exception {
        Instant now = Instant.now();
        MetricRequest request = MetricRequest.builder()
                .name("cpu.usage")
                .value(60.0)
                .serviceName("order-service")
                .timestamp(now)
                .build();

        mockMvc.perform(post(METRICS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(METRICS_URL  + "/range")
                        .param("from", now.minusSeconds(10).toString())
                        .param("to", now.plusSeconds(10).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").isNumber());
    }
}
