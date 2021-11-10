package shinhands.com.processor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import io.smallrye.jwt.build.Jwt;
import shinhands.com.model.hub.User;

@ApplicationScoped
public class JwtGenerateProcessorSample implements Processor {


    public void process(Exchange exchange) throws Exception {
        User user = exchange.getIn().getBody(User.class);
        JsonObject json = Json.createObjectBuilder()
            .add("email", user.getEmail())
            .add("sub","sdap Oauth")
            .build();
        Jwt.claims(json)
            // .subject("quarkus-camel-project")
            .upn(user.getName())
            .groups(user.getRole())
            .issuedAt(Instant.now().toEpochMilli())
            .expiresAt(Instant.now().plus(2, ChronoUnit.DAYS));
        String key = Jwt.claims(json).sign();
        exchange.getIn().setBody(key);
    }
}