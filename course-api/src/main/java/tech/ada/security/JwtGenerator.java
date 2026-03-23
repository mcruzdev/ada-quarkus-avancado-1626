package tech.ada.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtGenerator {

    public String generateSignJwt() {
        return Jwt.claims().jws().sign();
    }

    public String generateEncryptedJwt() {
        return Jwt.claims().jwe().encrypt();
    }
}
