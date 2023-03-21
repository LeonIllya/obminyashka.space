package space.obminyashka.items_exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.exception.EmailTokenExpiredException;
import space.obminyashka.items_exchange.exception.EmailTokenNotExistsException;
import space.obminyashka.items_exchange.service.MailService;

import java.util.UUID;

@RestController
@Tag(name = "Mail")
@RequiredArgsConstructor
@Validated
public class EmailController {
    private final MailService mailService;

    @PutMapping(value = ApiKey.MAIL_VALIDATE_TOKEN)
    @Operation(summary = "Validate confirmation email token id and then activate email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public void validateEmailAndThenActivate(@PathVariable("token_id") UUID tokenID) throws EmailTokenExpiredException, EmailTokenNotExistsException {
        mailService.validateEmail(tokenID);
    }
}
