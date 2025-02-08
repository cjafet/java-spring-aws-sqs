package com.message.aws.common.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FileUploadExceptionTest {

    private FileUploadException fileUploadException;

    @BeforeEach
    void setUp() {
        fileUploadException = new FileUploadException();
    }

    @Test
    void shouldReturnPayloadTooLargeForMaxUploadSizeException() {
        MaxUploadSizeExceededException exception = new MaxUploadSizeExceededException(1024);

        Map<String, Object> response = fileUploadException.handleMaxSizeException(exception);

        assertThat(response).isNotNull();
        assertThat(response.get("statusCode")).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE.value());
        assertThat(response.get("error")).isEqualTo("O arquivo enviado é muito grande. O tamanho máximo permitido é 1GB.");
        assertThat(response.get("timestamp")).isInstanceOf(String.class);
    }
}
