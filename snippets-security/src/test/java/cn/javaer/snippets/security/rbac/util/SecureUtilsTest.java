package cn.javaer.snippets.security.rbac.util;

import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author cn-src
 */
class SecureUtilsTest {

    @Test
    void jwtDecoder() {
    }

    @Test
    void jwtGenerator() {
        final JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .claim("scp", "read")
            .subject("alice")
            .issuer("https://c2id.com")
            .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000))
            .build();
//        System.out.println(SecureUtils.jwtGenerator(claimsSet,
//        "qazwsxedcrfvtgbyhnujmiklopqazwsx"));
    }
}