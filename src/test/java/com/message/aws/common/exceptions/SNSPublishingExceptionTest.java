package com.message.aws.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNSPublishingExceptionTest {

    @Test
    void shouldCreateSNSPublishingExceptionWithMessageAndCause() {
        Exception cause = new Exception("Erro original");
        SNSPublishingException exception = new SNSPublishingException("Erro ao publicar no SNS", cause);

        assertEquals("Erro ao publicar no SNS", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
