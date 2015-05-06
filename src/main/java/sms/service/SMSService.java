package sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sms.exception.SMSAPIException;

import java.net.URI;
import java.util.Base64;

/**
 * @author Chris Campo
 */
@Service
public class SMSService {

    private static final Logger log = LoggerFactory.getLogger(SMSService.class);
    private final String messageUrl;
    private final RestTemplate restTemplate;
    private final String botKey;
    private final String from;
    private final String username;
    private final String password;

    @Autowired
    public SMSService(final RestTemplate restTemplate,
            @Value("${messageUrl}") final String messageUrl,
            @Value("${botkey}") final String botKey,
            @Value("${from}") final String from,
            @Value("${username}") final String username,
            @Value("${password}") final String password) {
        this.restTemplate = restTemplate;
        this.messageUrl = messageUrl;
        this.botKey = botKey;
        this.from = from;
        this.username = username;
        this.password = password;
    }

    public void sendMessage(final String message, final String userKey) {
        final URI messageURI = UriComponentsBuilder.fromHttpUrl(messageUrl)
                .queryParam("botkey", botKey)
                .queryParam("from", from)
                .queryParam("apimethod", "send")
                .queryParam("userkey", userKey)
                .queryParam("msg", message)
                .build().toUri();

        final HttpHeaders headers = new HttpHeaders();
        final String encodedCreds = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        headers.add("Authorization", "Basic " + encodedCreds);
        final HttpEntity<?> request = new HttpEntity<>(headers);

        final String response = restTemplate.postForObject(messageURI, request, String.class);
        log.info("SMS API response: body = {}", response);

        if (response.contains("stat=\"fail\"")) {
            final String errMsg = response.substring(response.indexOf("err msg=") + 8, response.indexOf("/>"));
            throw new SMSAPIException(errMsg);
        }
        log.info("Outbound SMS was successfully sent.");
    }
}
