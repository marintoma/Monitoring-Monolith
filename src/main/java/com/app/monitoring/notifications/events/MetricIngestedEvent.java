package com.app.monitoring.notifications.events;

import com.app.monitoring.metrics.entity.Metric;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MetricIngestedEvent extends ApplicationEvent {

    private final Metric metric;

    public MetricIngestedEvent(Object source, Metric metric) {
        super(source);
        this.metric = metric;
    }
}
