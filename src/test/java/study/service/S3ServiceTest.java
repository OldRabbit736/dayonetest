package study.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import study.IntegrationTest;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class S3ServiceTest extends IntegrationTest {

    @Autowired
    private S3Service s3Service;

    @Test
    public void s3PutAndGetTest() throws Exception {
        // given
        var bucket = "test-bucket";
        var key = "sampleObject.txt";
        var sampleFile = new ClassPathResource("static/sample.txt").getFile();

        // when
        s3Service.putFile(bucket, key, sampleFile);

        // then
        var resultFile = s3Service.getFile(bucket, key);

        List<String> sampleFileLines = FileUtils.readLines(sampleFile, StandardCharsets.UTF_8);
        List<String> resultFileLines = FileUtils.readLines(resultFile, StandardCharsets.UTF_8);

        assertEquals(sampleFileLines, resultFileLines);
    }
}
