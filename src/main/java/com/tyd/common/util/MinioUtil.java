package com.tyd.common.util;

import com.tyd.common.config.MinioConfig;
import com.tyd.common.exception.CustomaizeExpetion;
import com.tyd.common.exception.ExceptionEnum;
import com.tyd.module.pojo.Vo.FileVo;
import io.minio.*;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class MinioUtil {
    @Autowired
    private MinioConfig prop;

    @Resource
    private MinioClient minioClient;

//    @Autowired
//    private CodeService codeService;

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file) {
        return upload(file, prop.getBucketName());
    }

    /**
     * 自定义桶上传
     *
     * @param file       文件
     * @param bucketName bucket名称
     * @return {@link String}
     */
    public String upload(MultipartFile file, String bucketName) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new CustomaizeExpetion(ExceptionEnum.NOT_FOUND);
        }
        String fileName = BaseUtils.noUnderscoreUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = BaseUtils.timeFormat(new Date(), "yyyy-MM/dd") + "/" + fileName;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectName;
    }

    public Map<Object, String> upload(MultipartFile[] file, String bucketName) {
        Map<Object, String> map = new HashMap<>();
        String s = null;
        try {
            //存入bucket不存在则创建，并设置为只读
            if (!bucketExists(bucketName)) {
                makeBucket(bucketName);
            }
            for (int i = 0; i < file.length; i++) {
                String filename = file[i].getOriginalFilename();
                // 文件存储的目录结构
                String objectName = BaseUtils.timeFormat(new Date(), "yyyy-MM/dd");
                // 存储文件
                PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                        .stream(file[i].getInputStream(), file[i].getSize(), -1).contentType(file[i].getContentType()).build();
                minioClient.putObject(objectArgs);
                log.info("文件上传成功!");
                s = prop.getEndpoint() + "/" + bucketName + "/" + objectName/*+ "/" + filename*/;
                map.put("bucket", objectName);
                map.put("url", s);
            }
        } catch (Exception e) {
            log.info("上传发生错误: {}！", e.getMessage());
        }
        return map;
    }

    /**
     * 预览图片
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder().expiry(60 * 60 * 24).bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回临时带签名、过期时间一天、Get请求方式的访问URL
     *
     * @param bucketName 桶名
     * @param fileName   Oss文件路径
     * @return
     */
    @SneakyThrows
    public String preview(String fileName, String bucketName) {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(fileName)
                        .expiry(60 * 60 * 24)
                        .build());
    }


    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) {
        download(fileName, res, prop.getBucketName());
        return;
    }

    /**
     * 下载
     *
     * @param fileName   文件名称
     * @param res        res
     * @param bucketName bucket名称
     */
    public void download(String fileName, HttpServletResponse res, String bucketName) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName)
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        return listObjects(prop.getBucketName());
    }

    /**
     * 列表对象
     *
     * @param bucketName bucket名称
     * @return {@link List}<{@link Item}>
     */
    public List<Item> listObjects(String bucketName) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName) {
        return remove(fileName, prop.getBucketName());
    }

    /**
     * 删除
     *
     * @param fileName   文件名称
     * @param bucketName bucket名称
     * @return boolean
     */
    public boolean remove(String fileName, String bucketName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 初始化默认存储桶
     */
    @PostConstruct
    public void initDefaultBucket() {
        String defaultBucketName = prop.getBucketName();
        if (bucketExists(defaultBucketName)) {
            log.info("默认存储桶已存在");
        } else {
            log.info("创建默认存储桶");
            makeBucket(prop.getBucketName());
        }
        ;
    }


    @SneakyThrows
    public List<FileVo> getFileList(Integer pageNum, Integer pageSize) {

        DecimalFormat df = new DecimalFormat("0.00");
        List<Bucket> buckets = minioClient.listBuckets();
        List<FileVo> list = new ArrayList<>(32);
        if (!buckets.isEmpty()) {
            buckets.forEach(s -> {
                // 得到bucket下的文件
                Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(s.name()).recursive(true).build());
                // 循环遍历获取每一个文件对象
                results.forEach(g -> {
                    try {
                        FileVo fileVo = new FileVo();
                        fileVo.setBucketName(s.name());  // 文件夹名称
                        fileVo.setFileName(g.get().objectName());  // 文件名称
                        fileVo.setUpdateTime(new Date());  // 文件上传时间
                        Long size = g.get().size();
                        if (size > (1024 * 1024)) {
                            fileVo.setFileSize(df.format(((double) size / 1024 / 1024)) + "MB");  // 文件大小，如果超过1M，则把单位换成MB
                        } else if (size > 1024) {
                            fileVo.setFileSize(df.format(((double) size / 1024)) + "KB"); // 文件大小，如果没超过1M但是超过1000字节，则把单位换成KB
                        } else {
                            fileVo.setFileSize(size + "bytes");  // // 文件大小，如果没超过1000字节，则把单位换成bytes
                        }
                        list.add(fileVo);
                    } catch (ErrorResponseException e) {
                        e.printStackTrace();
                    } catch (InsufficientDataException e) {
                        e.printStackTrace();
                    } catch (InternalException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidResponseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (ServerException e) {
                        e.printStackTrace();
                    } catch (XmlParserException e) {
                        e.printStackTrace();
                    }
                });
            });
        }
        // 按最后上传时间排序
        list.sort(new Comparator<FileVo>() {
            @Override
            public int compare(FileVo o1, FileVo o2) {
                return o2.getUpdateTime().compareTo(o1.getUpdateTime());
            }
        });
        return list;
    }
}
