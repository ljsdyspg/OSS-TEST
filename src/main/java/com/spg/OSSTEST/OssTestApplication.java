package com.spg.OSSTEST;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectAcl;
import com.aliyun.oss.model.ObjectListing;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class OssTestApplication {

    /*public static void main(String[] args) {
        SpringApplication.run(OssTestApplication.class, args);
    }*/
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    // LTAIsiYRN51xsn0b
    String accessKeyId = "LTAIsiYRN51xsn0b";
    // Sz6jBb39XIHJHniCHsJ6wvWDnW1HAP
    String accessKeySecret = "Sz6jBb39XIHJHniCHsJ6wvWDnW1HAP";
    // spg-test
    String bucketName = "spg-test";


    /**
     * 创建存储空间
     */
    private void init() {
        String temp_bucketName = "";

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 创建存储空间。
        ossClient.createBucket(temp_bucketName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 上传文件
     */
    private void upload() {

        // 上传的文件
        String objectName = "pic2.jpg";

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 上传内容到指定的存储空间(buckName) 并保存为指定的文件名称 (objectName)
        // String content = "Hello Oss";
        // ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));

        /*// 上传文件流
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(objectName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ossClient.putObject(bucketName, objectName,inputStream);*/

        // 上传文件
        ossClient.putObject(bucketName, objectName, new File(objectName));


        // 关闭OSSClient
        ossClient.shutdown();
    }

    /**
     *  下载文件到本地
     */
    private void download() {
        // 下载的文件
        String objectName = "pic1.jpg";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(objectName));

        // 关闭OSSClient
        ossClient.shutdown();
    }

    /**
     *  是否存在目标文件
     * @return
     */
    private boolean isExist(){

        // 目标文件
        String objectName = "pic1.jpg";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 判断文件是否存在
        boolean found = ossClient.doesObjectExist(bucketName, objectName);

        if (found) {
            System.out.println("存在");
        }else {
            System.out.println("不存在");
        }

        ossClient.shutdown();

        return found;
    }

    /**
     * 获取对目标文件的权限
     */
    private void getPermission(){
        // 目标文件的名称
        String objectName = "pic1.jpg";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 获取文件的访问权限
        ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, objectName);

        System.out.println("Permission! ");
        System.out.println(objectAcl.getPermission().toString());

        // 关闭OSSClient
        ossClient.shutdown();
    }

    /**
     *  列举所有的文件
     */
    private void showFiles(){

        String KeyPrefix = "";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 列举文件
        ObjectListing objectListing = ossClient.listObjects(bucketName, KeyPrefix);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary s : sums) {
            System.out.println("\t" + s.getKey());
        }

        // 关闭OSSClient
        ossClient.shutdown();
    }

    private void deleteFile(){
        // 目标文件
        String targetFile = "pic1.jpg";

        // 创建OSSClient 实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        if (ossClient.doesObjectExist(bucketName, targetFile)) {
            System.out.println("目标文件存在");
            // 删除文件
            ossClient.deleteObject(bucketName, targetFile);
        } else {
            System.out.println("目标文件不存在");
        }

        ossClient.shutdown();
    }


    public static void main(String[] args) {
        OssTestApplication app = new OssTestApplication();
        // app.download();
        app.deleteFile();
    }
}
