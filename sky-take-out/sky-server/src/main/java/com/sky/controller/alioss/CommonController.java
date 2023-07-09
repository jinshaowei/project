package com.sky.controller.alioss;

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

@Slf4j
@Api(tags = "图片上传")
@RestController
@RequestMapping("admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        //获取文件原始名
        String originalFilename = file.getOriginalFilename();
        //截取以最后一个点后面的字符串然后做拼接
        String ObjectName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //获取路径
        String url = aliOssUtil.upload(file.getBytes(), ObjectName);
        //返回路径
        return Result.success(url);
    }

}
