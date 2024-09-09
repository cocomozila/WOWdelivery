package com.wow.delivery.warmup;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class WarmupTokenProvider {

    @Value("${warmup.token.key}")
    private String secretKey; // 이 값을 Base64로 인코딩된 문자열로 사용
    private final long validityInMilliseconds = 1000 * 60 * 5; // 5분 유효 기간

    // Key 생성 메서드
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);  // Base64 디코딩
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String createToken() {
        Instant now = Instant.now();
        Instant validity = now.plusMillis(validityInMilliseconds); // 유효 기간을 더한 시간 계산

        return Jwts.builder()
            .setSubject("warmup")
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(validity))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // 토큰이 유효하지 않으면 false 반환
        }
    }
}
