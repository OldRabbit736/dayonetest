package study;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

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

            TestPropertyValues.of(properties).applyTo(applicationContext);
        }
    }

}
