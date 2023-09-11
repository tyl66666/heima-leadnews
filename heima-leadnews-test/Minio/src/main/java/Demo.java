import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author tyl
 * @date 2023/9/6
 */
public class Demo {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream=new FileInputStream("d:\\temp\\js\\index.js");

            MinioClient minioClient=MinioClient.builder().credentials("minio","minio123").endpoint("http://192.168.200.130:9000").build();

            PutObjectArgs putObjectArgs = PutObjectArgs.builder().object("plugins/js/index.js")
                    .contentType("text/js")
                    .bucket("leadnews")
                    .stream(fileInputStream, fileInputStream.available(), -1).build();

            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
