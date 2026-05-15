package com.app.monitoring.logs;

import com.app.monitoring.BaseIntegrationTest;
import com.app.monitoring.logs.dto.LogRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LogsControllerTest extends BaseIntegrationTest {

    private final String LOGS_URL = "/api/logs";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldIngestLogAndReturn201() throws Exception {
        LogRequest request = LogRequest.builder()
                .level(LogLevel.ERROR)
                .message("Database connection failed")
                .serviceName("auth-service")
                .timestamp(Instant.now())
                .build();

        mockMvc.perform(post(LOGS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.level").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Database connection failed"))
                .andExpect(jsonPath("$.serviceName").value("auth-service"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldReturnBadRequestWhenFieldsMissing() throws Exception {
        mockMvc.perform(post(LOGS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldQueryLogsByServiceName() throws Exception {
        LogRequest request = LogRequest.builder()
                .level(LogLevel.INFO)
                .message("Service started")
                .serviceName("payment-service")
                .timestamp(Instant.now())
                .build();

        mockMvc.perform(post(LOGS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(LOGS_URL + "/service/payment-service"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].serviceName").value("payment-service"));
    }

    @Test
    void shouldQueryLogsByLevel() throws Exception {
        LogRequest request = LogRequest.builder()
                .level(LogLevel.WARN)
                .message("High memory usage")
                .serviceName("order-service")
                .timestamp(Instant.now())
                .build();

        mockMvc.perform(post(LOGS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(LOGS_URL + "/level/WARN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].level").value("WARN"));
    }

    @Test
    void shouldIngestBatchAndReturn201() throws Exception {
        List<LogRequest> requests = List.of(
                LogRequest.builder()
                        .level(LogLevel.ERROR)
                        .message("Connection failed")
                        .serviceName("auth-service")
                        .timestamp(Instant.now())
                        .build(),
                LogRequest.builder()
                        .level(LogLevel.INFO)
                        .message("Service started")
                        .serviceName("auth-service")
                        .timestamp(Instant.now())
                        .build()
        );

        mockMvc.perform(post(LOGS_URL + "/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ingested").value(2))
                .andExpect(jsonPath("$.failed").value(0));
    }
}
