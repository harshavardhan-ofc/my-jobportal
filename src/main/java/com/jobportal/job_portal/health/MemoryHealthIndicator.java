package com.jobportal.job_portal.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MemoryHealthIndicator implements HealthIndicator {

    private static final long THRESHOLD = 50 * 1024 * 1024; // 50 MB

    private final Long freeMemory;
    private final Long totalMemory;
    private final Long maxMemory;


    public MemoryHealthIndicator() {
        Runtime runtime = Runtime.getRuntime();
        this.freeMemory = runtime.freeMemory();
        this.totalMemory = runtime.totalMemory();
        this.maxMemory = runtime.maxMemory();
    }


    public MemoryHealthIndicator(long freeMemory, long totalMemory, long maxMemory) {
        this.freeMemory = freeMemory;
        this.totalMemory = totalMemory;
        this.maxMemory = maxMemory;
    }

    @Override
    public Health health() {
        long usedMemory = totalMemory - freeMemory;
        if ((maxMemory - usedMemory) < THRESHOLD) {
            return Health.down()
                    .withDetail("error", "Low available memory")
                    .withDetail("freeMemory", freeMemory)
                    .withDetail("usedMemory", usedMemory)
                    .withDetail("maxMemory", maxMemory)
                    .build();
        }
        return Health.up()
                .withDetail("freeMemory", freeMemory)
                .withDetail("usedMemory", usedMemory)
                .withDetail("maxMemory", maxMemory)
                .build();
    }
}
