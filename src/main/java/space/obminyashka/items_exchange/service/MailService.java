package space.obminyashka.items_exchange.service;

import org.springframework.scheduling.annotation.Scheduled;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.util.EmailType;

import java.io.IOException;
import java.util.UUID;

public interface MailService {

    /**
     * Send a simple email to an end-user with provided locale
     *
     * @param emailTo   a receiver email address
     * @param emailType one of the supported email types to be sent
     * @param codeId code for generating link for confirming email
     * @param host domain name url
     * @throws IOException when service is unavailable or some unexpected case happened
     */
    void sendMail(String emailTo, EmailType emailType, UUID codeId, String host) throws IOException;

    /**
     * Validate users' email by previously generated UUID code
     * @param validationCode a code for an email activation
     * @throws EmailValidationCodeNotFoundException when the code isn't found
     */
    void validateEmail(UUID validationCode) throws EmailValidationCodeNotFoundException;

    /**
     * Scheduled job which checks emails that needs to be removed from DB after exhaustion of the grace period
     */
    @Scheduled(cron = "${cron.expression.once_per_day_at_3am}")
    void permanentlyDeleteEmailConfirmationCode();
}
