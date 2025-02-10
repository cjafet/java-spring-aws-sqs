package com.message.aws.infrastructure.configuration;

import com.message.aws.core.useCase.DownloadUseCase;
import com.message.aws.core.useCase.UploadUseCase;
import com.message.aws.core.port.DatabasePort;
import com.message.aws.infrastructure.adapter.SNSPublisherAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {
    @Autowired
    private SNSPublisherAdapter snsPublisherAdapter;

    @Autowired
    private S3Config s3Config;

    @Autowired
    private DatabasePort databasePort;

    @Bean
    public UploadUseCase getUploadUseCase(){
        return new UploadUseCase(s3Config,snsPublisherAdapter, databasePort);
    }

    @Bean
    public DownloadUseCase getSNSPublisherAdapter(){
        return new DownloadUseCase(s3Config);
    }
}
