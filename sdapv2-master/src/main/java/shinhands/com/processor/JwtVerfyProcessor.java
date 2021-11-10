package shinhands.com.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.jwt.auth.principal.JWTParser;
import lombok.extern.slf4j.Slf4j;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.services.BucketService;

@ApplicationScoped
@Slf4j
public class JwtVerfyProcessor implements Processor {

    @Inject
    BucketService bucketService;

    @Inject
    JWTParser parser;

    @Inject
    JsonWebToken jwtPrincipal;

    // smallrye.jwt.sign.key.location
    // mp.jwt.verify.publickey.location
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("verified", false);
        try {
            String authHeader = (String) exchange.getIn().getHeader("Authorization");
            if (authHeader.startsWith("Bearer ")) {
                // String key = getPem();
                String token = authHeader.substring(7, authHeader.length());
                PublicKey pkey = readPublicKey();
                JsonWebToken jwt = parser.verify(token, pkey);
                String userid = jwt.getClaim("userid");
                // log.info("email => {}, getClaimNames => {}", email, jwt.getClaimNames().toString());
                exchange.getIn().setHeader("verified", true);
                exchange.getIn().setHeader("userid", userid);
                exchange.getIn().setHeader("token", token);
                // exchange.getIn().setBody(new UserSessionPayload(token,userid));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw(new UnauthorizedError("사용자 세션이 정보가 존재하지 않습니다."));
        }
    }

    private String getPem() throws IOException {
        // log.info("# key location => {}",location);
        // smallrye.jwt.sign.key.location
        // mp.jwt.verify.publickey.location
        String signKey = null;
        // Bucket bucket = bucketService.findById("signKey");
        // if (bucket == null || Utils.isEmpty(bucket.getValue().toString())) {
            String location = ConfigProvider.getConfig().getValue("smallrye.jwt.sign.key.location", String.class);
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(location);
            StringWriter sw = new StringWriter();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line = br.readLine();
                while (line != null) {
                    sw.write(line);
                    sw.write('\n');
                    line = br.readLine();
                }
            }
            signKey = sw.toString();
            // bucketService.save("signKey", "smallrye.jwt.sign.key.location", signKey);
        // } else {
        //     log.info("Bucket Value => {}", bucket.toString());
        //     signKey = bucket.getValue().toString();
        // }
        return signKey;
    }

    private PublicKey readPublicKey() throws Exception {
        String location = ConfigProvider.getConfig().getValue("mp.jwt.verify.publickey.location", String.class);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream contentIS = classloader.getResourceAsStream(location);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePublicKey(new String(tmp, 0, length, "UTF-8"));
    }

    private PublicKey decodePublicKey(String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }
}