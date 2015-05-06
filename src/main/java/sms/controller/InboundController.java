package sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sms.exception.SMSAPIException;
import sms.service.SMSService;

/**
 * @author Chris Campo
 */
@RestController
public class InboundController {

    private static final Logger log = LoggerFactory.getLogger(InboundController.class);
    private final SMSService smsService;

    @Autowired
    public InboundController(final SMSService smsService) {
        this.smsService = smsService;
    }

    @RequestMapping(value = "inbound", method = RequestMethod.POST)
    public ResponseEntity<Void> handleInboundSms(@RequestParam final String userkey, @RequestParam final String msg) {
        log.info("Request: userkey = {}, msg = {}", userkey, msg);
        try {
            smsService.sendMessage("I received the following message: \"" + msg + "\"", userkey);
        } catch (SMSAPIException ex) {
            log.error("Exception while sending outbound SMS message.", ex);
            throw ex;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
