package com.wudgaby.starter.oss.minio;

import com.wudgaby.starter.oss.OssProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OssProperties.class)
public class MinioAutoConfiguration {
    @Bean
    public MinioClient minioClient(OssProperties ossProperties) {
        log.info("开始初始化MinioClient, url为{}, accessKey为:{}", ossProperties.getUrl(), ossProperties.getAccessKey());
        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(ossProperties.getUrl())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
                .build();

        minioClient.setTimeout(
                ossProperties.getConnectTimeout(),
                ossProperties.getWriteTimeout(),
                ossProperties.getReadTimeout()
        );
        // Start detection
        if (ossProperties.isCheckBucket()) {
            log.info("checkBucket为{}, 开始检测桶是否存在", ossProperties.isCheckBucket());
            String bucketName = ossProperties.getBucket();
            if (!checkBucket(bucketName, minioClient)) {
                log.info("文件桶[{}]不存在, 开始检查是否可以新建桶", bucketName);
                if (ossProperties.isCreateBucket()) {
                    log.info("createBucket为{},开始新建文件桶", ossProperties.isCreateBucket());
                    createBucket(bucketName, minioClient);
                }
            }
            log.info("文件桶[{}]已存在, minio客户端连接成功!", bucketName);
        } else {
            throw new RuntimeException("桶不存在, 请检查桶名称是否正确或者将checkBucket属性改为false");
        }
        return minioClient;
    }

    private boolean checkBucket(String bucketName, MinioClient minioClient) {
        boolean isExists = false;
        try {
            isExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new RuntimeException("failed to check if the bucket exists", e);
        }
        return isExists;
    }

    private void createBucket(String bucketName, MinioClient minioClient) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("文件桶[{}]新建成功, minio客户端已连接", bucketName);
        } catch (Exception e) {
            throw new RuntimeException("failed to create default bucket", e);
        }
    }
}
