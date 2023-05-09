package space.obminyashka.items_exchange.service;

import com.sendgrid.Method;
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
        var expectedHost = "https://obminyashka.space";

        mailService.sendEmailTemplateAndGenerateConfrimationCode(emailTo, EmailType.REGISTRATION, expectedHost);

        verify(sendGrid).api(requestCapture.capture());
        Request capturedRequest = requestCapture.getValue();
        verify(sendGrid).api(argThat(request -> capturedRequest.getMethod() == Method.POST &&
                        capturedRequest.getEndpoint().equals("mail/send") &&
                        capturedRequest.getBody().contains(emailTo)));
    }
}
