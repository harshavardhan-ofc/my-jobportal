package com.jobportal.job_portal.service.impl;

import com.jobportal.job_portal.security.JwtService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;


@BeforeEach
void setUp() {
    String secret = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    jwtService = new JwtService(secret, 3600000);
}

    @Test
    void testGenerateAndExtractClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");

        String token = jwtService.generateToken("testUser", claims);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);

        String role = jwtService.extractUserRole(token);
        assertEquals("ADMIN", role);
    }

    @Test
    void testIsTokenValid() {
        Map<String, Object> claims = new HashMap<>();
        String token = jwtService.generateToken("testUser", claims);

        assertTrue(jwtService.isTokenValid(token, "testUser"));

        assertFalse(jwtService.isTokenValid(token, "wrongUser"));
    }

    void testExpiredToken() throws InterruptedException {
        // Expiration set to 1 second for this case
        JwtService shortLivedJwt = new JwtService("shortSecretKey12345678912341234123412341234", 1000);
        String token = shortLivedJwt.generateToken("testUser", new HashMap<>());


        Thread.sleep(1200);

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
                shortLivedJwt.isTokenValid(token, "testUser");
        });
    }
}
