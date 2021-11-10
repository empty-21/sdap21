package shinhands.com.processor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import io.smallrye.jwt.build.Jwt;
import shinhands.com.model.hub.misc.policy.userifs.UserPayload;

@ApplicationScoped
public class JwtGenerateProcessor implements Processor {
// https://quarkus.pro/guides/security-jwt.html
    public void process(Exchange exchange) throws Exception {
        UserPayload user = exchange.getIn().getBody(UserPayload.class);
        JsonObject json = Json.createObjectBuilder()
            .add("userid", user.getUserid())
            .add("sub","sdap Oauth")
            .build();
        Jwt.claims(json)
            // .subject("quarkus-camel-project")
            .upn(user.getUsername())
            .groups(user.getAuthgrp())
            .issuedAt(Instant.now().toEpochMilli());
            // .expiresIn(0);
            // .expiresAt(Instant.now().plus(2, ChronoUnit.DAYS));
        String key = Jwt.claims(json).sign();
        exchange.getIn().setHeader("token", key);
        exchange.getIn().setHeader("accessToken",Map.of("accessToken", key));
        // exchange.getIn().setBody(key);
    }
}