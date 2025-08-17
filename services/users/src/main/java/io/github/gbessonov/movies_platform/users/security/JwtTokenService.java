package io.github.gbessonov.movies_platform.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class JwtTokenService {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.audience}")
    private String audience;
    @Value("${jwt.expiration.seconds}")
    private int expirationSeconds;

    @Autowired
    public JwtTokenService(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = "users:read movies:write"; //TODO: set proper roles/scopes based on authentication
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(List.of(audience)) //TODO: set proper audience
                .issuedAt(now)
                .expiresAt(now.plus(expirationSeconds, ChronoUnit.SECONDS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("roles", List.of("ADMIN"))
                .build();
        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    public Long extractExpirationTime(String token) {
        Jwt jwt = decoder.decode(token);
        var exp = (Instant) jwt.getClaim("exp");
        return exp.toEpochMilli();
    }
}
