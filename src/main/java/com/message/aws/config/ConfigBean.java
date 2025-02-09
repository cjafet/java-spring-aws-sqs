package com.message.aws.config;

import com.message.aws.core.useCase.DownloadUseCase;
import com.message.aws.core.useCase.UploadUseCase;
import com.message.aws.core.port.DatabasePort;
import com.message.aws.infrastructure.adapter.SNSPublisherAdapter;
import com.message.aws.infrastructure.configuration.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${s3.bucket-video-original}")
    private String bucketVideoName;

    @Value("${s3.bucket-frames}")
    private String bucketZipName;

    @Bean
    public UploadUseCase getUploadUseCase(){
        return new UploadUseCase(s3Config,snsPublisherAdapter, databasePort, bucketVideoName);
    }

    @Bean
    public DownloadUseCase getSNSPublisherAdapter(){
        return new DownloadUseCase(s3Config, bucketZipName);
    }
}
