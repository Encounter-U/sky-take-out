package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Encounter
 * @date 2024/09/08 19:01<br/>
 * 配置类，用于创建AliOSSUtil对象
 */
@Configuration
@Slf4j
public class OSSConfiguration
    {
        @Bean
        @ConditionalOnMissingBean//当没有该bean时再创建
        public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties)
            {
                log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
                return new AliOssUtil(aliOssProperties.getEndpoint(),
                        aliOssProperties.getAccessKeyId(),
                        aliOssProperties.getAccessKeySecret(),
                        aliOssProperties.getBucketName());
            }
    }
