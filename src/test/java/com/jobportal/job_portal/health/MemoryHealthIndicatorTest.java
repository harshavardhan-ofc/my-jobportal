package com.jobportal.job_portal.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import static org.junit.jupiter.api.Assertions.*;

class MemoryHealthIndicatorTest {

    @Test
    void testHealthStatusUp() {
        MemoryHealthIndicator indicator = new MemoryHealthIndicator(
                200L * 1024 * 1024, 300L * 1024 * 1024, 500L * 1024 * 1024);

        Health health = indicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(200L * 1024 * 1024, health.getDetails().get("freeMemory"));
        assertEquals(500L * 1024 * 1024, health.getDetails().get("maxMemory"));
    }

    @Test
    void testHealthStatusDown() {
        MemoryHealthIndicator indicator = new MemoryHealthIndicator(
                10L * 1024 * 1024, 490L * 1024 * 1024, 500L * 1024 * 1024);

        Health health = indicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals("Low available memory", health.getDetails().get("error"));
        assertEquals(10L * 1024 * 1024, health.getDetails().get("freeMemory"));
    }
}
