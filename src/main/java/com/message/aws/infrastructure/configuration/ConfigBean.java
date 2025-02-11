package com.message.aws.infrastructure.configuration;

import com.message.aws.core.usecase.DownloadUseCase;
import com.message.aws.core.usecase.UploadUseCase;
import com.message.aws.core.port.DatabasePort;
import com.message.aws.infrastructure.adapter.SNSPublisherAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {
    private final SNSPublisherAdapter snsPublisherAdapter;

    private final S3Config s3Config;

    private final DatabasePort databasePort;

    public ConfigBean(SNSPublisherAdapter snsPublisherAdapter, S3Config s3Config, DatabasePort databasePort) {

        this.snsPublisherAdapter = snsPublisherAdapter;
        this.s3Config = s3Config;
        this.databasePort = databasePort;
    }

    @Bean
    public UploadUseCase getUploadUseCase() {
        return new UploadUseCase(s3Config, snsPublisherAdapter, databasePort);
    }

    @Bean
    public DownloadUseCase getSNSPublisherAdapter() {
        return new DownloadUseCase(s3Config);
    }
}
