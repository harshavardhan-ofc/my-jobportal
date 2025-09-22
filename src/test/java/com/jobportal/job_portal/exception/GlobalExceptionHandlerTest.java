package com.jobportal.job_portal.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleAllExceptions_Returns500WithMessage() {
        Exception ex = new Exception("Test error");

        ResponseEntity<String> response = exceptionHandler.handleAllExceptions(ex);

        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error: Test error", response.getBody());
    }

    @Test
    void testHandleAllExceptions_NullMessage() {
        Exception ex = new Exception();

        ResponseEntity<String> response = exceptionHandler.handleAllExceptions(ex);

        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().startsWith("Error:"));
    }
}
