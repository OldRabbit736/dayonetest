package study;

import com.redis.testcontainers.RedisContainer;
import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 통합 테스트 원형(부모)
 * 부모이므로 테스트 자체는 실행되지 않도록 @Ignore를 붙여주었다.
 */
@Ignore
@Transactional
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {

    static DockerComposeContainer rdbms;
    static RedisContainer redis;
    static LocalStackContainer aws;

    static {
        rdbms = new DockerComposeContainer(new File("infra/test/docker-compose.yml"))
                // 연결하고자 하는 docker compose의 서비스의 이름과 포트 번호(컨테이너 내부 포트 번호) 지정
                // 해당 서비스에 연결하기 위한 호스트와 포트 번호가 testcontainers에 의해 동적으로 할당(포트 바인딩)된다. (예: localhost:51212)
                .withExposedService(
                        "local-db",
                        3306,
                        Wait.forLogMessage(".*ready for connections.*", 1)
                                .withStartupTimeout(Duration.ofSeconds(30))
                )
                .withExposedService(
                        "local-db-migrate",
                        0,
                        Wait.forLogMessage("(.*Successfully applied.*)|(.*Successfully validated.*)", 1)
                                .withStartupTimeout(Duration.ofSeconds(30))
                );

        rdbms.start();

        redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("6"));
        redis.start();

        aws = (new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.3")))
                .withServices(LocalStackContainer.Service.S3)
                .withStartupTimeout(Duration.ofSeconds(600));
        aws.start();
    }

    /**
     * 동적으로 할당된 호스트와 포트 번호로 연결하기 위해 프로퍼티 수정
     */
    static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Map<String, String> properties = new HashMap<>();

            String rdbmsHost = rdbms.getServiceHost("local-db", 3306);
            Integer rdbmsServicePort = rdbms.getServicePort("local-db", 3306);

            properties.put(
                    "spring.datasource.url",
                    "jdbc:mysql://" + rdbmsHost + ":" + rdbmsServicePort + "/score"
            );

            String redisHost = redis.getHost();
            String redisPort = redis.getFirstMappedPort().toString();

            properties.put("spring.data.redis.host", redisHost);
            properties.put("spring.data.redis.port", redisPort);

            try {
                aws.execInContainer(
                        "awslocal",
                        "s3api",
                        "create-bucket",
                        "--bucket",
                        "test-bucket"
                );

                properties.put("aws.endpoint", aws.getEndpoint().toString());
            } catch (Exception e) {
                // ignore
            }

            TestPropertyValues.of(properties).applyTo(applicationContext);
        }
    }

}
