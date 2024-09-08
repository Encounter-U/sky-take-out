package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Encounter
 * @date 2024/09/07 15:34<br/>
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api("通用接口")
public class CommonController
    {
        @Autowired
        private AliOssUtil aliOssUtil;
        
        /**
         * 上传文件
         *
         * @param file 文件
         * @return {@link Result }<{@link String }>
         */
        @ApiOperation("文件上传")
        @PostMapping("/upload")
        public Result<String> upload(MultipartFile file)
            {
                log.info("文件上传：{}", file);
                
                try
                    {
                        //获取原始文件名
                        String originalFilename = file.getOriginalFilename();
                        //截取原始文件名后缀
                        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                        //拼接新文件名
                        String objectName = UUID.randomUUID() + extension;
                        //上传文件，返回文件访问路径
                        String uploadPath = aliOssUtil.upload(file.getBytes(), objectName);
                        return Result.success(uploadPath);
                    }
                catch (IOException e)
                    {
//                        throw new RuntimeException(e);
                        log.error("文件上传失败：{}", e.getMessage());
//                        return Result.error("上传失败");
                    }
                return Result.error(MessageConstant.UPLOAD_FAILED);
            }
    }
