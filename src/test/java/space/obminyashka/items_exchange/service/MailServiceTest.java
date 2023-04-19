package space.obminyashka.items_exchange.service;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.service.impl.SendGridService;
import space.obminyashka.items_exchange.util.EmailType;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;
    @Mock
    private SendGrid sendGrid;
    @Captor
    private ArgumentCaptor<Request> requestCapture;
    @InjectMocks
    private SendGridService mailService;

    @BeforeEach
    void setUp() {
        messageSourceUtil.setMSource(mock(MessageSource.class));
    }


    @Test
    void sendMail_shouldPassTheFlow() throws IOException {
        when(sendGrid.api(any())).thenReturn(new Response());

        final var emailTo = "test@mail.ua";
        UUID codeId = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3");
        var endpointUrl = "https://obminyashka.space/api/v1/email/validate/" + codeId;

        mailService.sendMail(emailTo, EmailType.REGISTRATION, Locale.ENGLISH, codeId);

        verify(sendGrid).api(requestCapture.capture());
        assertTrue(requestCapture.getValue().getBody().contains(emailTo));
        assertTrue(requestCapture.getValue().getEndpoint().contains(endpointUrl));
        assertEquals(endpointUrl, requestCapture.getValue().getEndpoint());
    }
}