package com.wudgaby.starter.oss;

/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * aws-s3 通用存储操作 支持所有兼容s3协议的云存储: {阿里云OSS，腾讯云COS，七牛云，京东云，minio 等}
 *
 * @author lengleng
 * @author 858695266
 * @author L.cm
 * @date 2020/5/23 6:36 上午
 * @since 1.0
 */
@RequiredArgsConstructor
public class OssTemplate implements InitializingBean {

    private final OssProperties ossProperties;

    private AmazonS3 amazonS3;

    /**
     * 创建bucket
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket((bucketName));
        }
    }

    /**
     * 获取全部bucket
     * <p>
     *
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS API
     * Documentation</a>
     */
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    /**
     * @param bucketName bucket名称
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS API
     * Documentation</a>
     */
    public Optional<Bucket> getBucket(String bucketName) {
        return amazonS3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst();
    }

    /**
     * @param bucketName bucket名称
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API
     * Documentation</a>
     */
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    /**
     * 根据文件前置查询文件
     * @param bucketName bucket名称
     * @param prefix 前缀
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS API
     * Documentation</a>
     */
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return new ArrayList<>(objectListing.getObjectSummaries());
    }

    /**
     * 获取文件外链，只用于下载
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param minutes 过期时间，单位分钟,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getObjectURL(String bucketName, String objectName, int minutes) {
        return getObjectURL(bucketName, objectName, Duration.ofMinutes(minutes));
    }

    /**
     * 获取文件外链，只用于下载
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires 过期时间,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getObjectURL(String bucketName, String objectName, Duration expires) {
        return getObjectURL(bucketName, objectName, expires, HttpMethod.GET);
    }

    /**
     * 获取文件上传外链，只用于上传
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param minutes 过期时间，单位分钟,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getPutObjectURL(String bucketName, String objectName, int minutes) {
        return getPutObjectURL(bucketName, objectName, Duration.ofMinutes(minutes));
    }

    /**
     * 获取文件上传外链，只用于上传
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires 过期时间,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getPutObjectURL(String bucketName, String objectName, Duration expires) {
        return getObjectURL(bucketName, objectName, expires, HttpMethod.PUT);
    }

    /**
     * 获取文件外链
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param minutes 过期时间，单位分钟,请注意该值必须小于7天
     * @param method 文件操作方法：GET（下载）、PUT（上传）
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration,
     * HttpMethod method)
     */
    public String getObjectURL(String bucketName, String objectName, int minutes, HttpMethod method) {
        return getObjectURL(bucketName, objectName, Duration.ofMinutes(minutes), method);
    }

    /**
     * 获取文件外链
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires 过期时间，请注意该值必须小于7天
     * @param method 文件操作方法：GET（下载）、PUT（上传）
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration,
     * HttpMethod method)
     */
    public String getObjectURL(String bucketName, String objectName, Duration expires, HttpMethod method) {
        // Set the pre-signed URL to expire after `expires`.
        Date expiration = Date.from(Instant.now().plus(expires));

        // Generate the pre-signed URL.
        URL url = amazonS3.generatePresignedUrl(
                new GeneratePresignedUrlRequest(bucketName, objectName).withMethod(method).withExpiration(expiration));
        return url.toString();
    }

    /**
     * 获取文件URL
     * <p>
     * If the object identified by the given bucket and key has public read permissions
     * (ex: {@link CannedAccessControlList#PublicRead}), then this URL can be directly
     * accessed to retrieve the object's data.
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName) {
        URL url = amazonS3.getUrl(bucketName, objectName);
        return url.toString();
    }

    /**
     * 获取文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     * @see <a href= "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @throws IOException IOException
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws IOException {
        putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 上传文件 指定 contextType
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @param contextType 文件类型
     * @throws IOException IOException
     */
    public void putObject(String bucketName, String objectName, String contextType, InputStream stream)
            throws IOException {
        putObject(bucketName, objectName, stream, stream.available(), contextType);
    }

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @param size 大小
     * @param contextType 类型
     * @see <a href= "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
     * API Documentation</a>
     */
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
                                     String contextType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contextType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, stream, objectMetadata);
        // Setting the read limit value to one byte greater than the size of stream will
        // reliably avoid a ResetException
        putObjectRequest.getRequestClientOptions().setReadLimit(Long.valueOf(size).intValue() + 1);
        return amazonS3.putObject(putObjectRequest);

    }

    /**
     * 获取文件信息
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @see <a href= "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */
    public S3Object getObjectInfo(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    /**
     * 删除文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject">AWS API
     * Documentation</a>
     */
    public void removeObject(String bucketName, String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                ossProperties.getUrl(), ossProperties.getRegion());
        AWSCredentials awsCredentials = new BasicAWSCredentials(ossProperties.getAccessKey(),
                ossProperties.getSecretKey());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        this.amazonS3 = AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(clientConfiguration).withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding().withPathStyleAccessEnabled(ossProperties.getPathStyleAccess()).build();
    }

}