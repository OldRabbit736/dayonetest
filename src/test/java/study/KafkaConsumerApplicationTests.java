package study;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import study.service.KafkaConsumerService;
import study.service.KafkaProducerService;

// 카프카 테스트 추가 이후 모든 테스트를 동작시킬 때 테스트가 성공했다 실패했다 하는 경우에 아래처럼 해주세요!
// https://busy-jellyfish-dcb.notion.site/5ef30f66cb7d4c65937e79d7b0763630
@Order(0)
@DirtiesContext
public class KafkaConsumerApplicationTests extends IntegrationTest {

  @Autowired private KafkaProducerService kafkaProducerService;

  @MockBean private KafkaConsumerService kafkaConsumerService;

  @Test
  public void kafkaSendAndConsumeTest() {
    // given
    String topic = "test-topic";
    String expectValue = "expect-value";

    // when
    kafkaProducerService.send(topic, expectValue);

    // then
    var stringCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(kafkaConsumerService, Mockito.timeout(5000).times(1))
        .process(stringCaptor.capture());
    assertEquals(expectValue, stringCaptor.getValue());
  }
}
