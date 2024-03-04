package com.heima.minio;

import com.heima.file.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("E:\\game\\edge\\index.js");


        //1 获取minio的链接信息,创建一个minio客户端
        MinioClient minioClient = MinioClient.builder().credentials("minio", "minio123").endpoint("http://192.168.200.130:9000").build();



        //2 上传
        PutObjectArgs putObjectArgs=PutObjectArgs.builder()
                .object("plugins/js/index.js")//文件名
                .contentType("application/javascript") //文件类型
                .bucket("leadnews") //桶名称
                // -1 是将所有的内容全部上传刀minio上  fileInputStream.available() 是表示文件的大小 上传文件
                .stream(fileInputStream,fileInputStream.available(),-1).build();
        minioClient.putObject(putObjectArgs);

//        System.out.println("http://192.168.200.130:9000/leadnews/list.html");
    } catch (Exception e) {
        e.printStackTrace();
     }

    }


//    @Autowired
//    private FileStorageService fileStorageService;
//    @Test
//    public void test() throws FileNotFoundException {
//        FileInputStream fileInputStream = new FileInputStream("D:\\list.html");
//        String path = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
//        System.out.println(path);
//    }
}
