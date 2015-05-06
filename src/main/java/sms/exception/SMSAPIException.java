package sms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Chris Campo
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "The SMS API returned an error.")
public class SMSAPIException extends RuntimeException {

    public SMSAPIException(final String message) {
        super(message);
    }
}
