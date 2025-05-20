package net.vino9.vino.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import net.vino9.vino.demo.biz.model.Transfer;
import net.vino9.vino.demo.biz.model.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(JedisMockConfig.class)
class TransferAPITests {

    @Autowired TestRestTemplate restTemplate;
    @Autowired ObjectMapper objectMapper;

    @Test
    void testTransferPipeline() {
        TransferRequest request =
                TransferRequest.builder()
                        .fromAccount("1234")
                        .currency("USD")
                        .amount(new BigDecimal("100"))
                        .toAccount("4321")
                        .memo("test transfer")
                        .build();

        ResponseEntity<Transfer> response =
                restTemplate.postForEntity("/submit,process,result", request, Transfer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getRefId());
    }

    @Test
    void testTransferBadAccount() {
        // fromAccount 1111 will always return error
        TransferRequest request =
                TransferRequest.builder()
                        .fromAccount("1111")
                        .currency("USD")
                        .amount(new BigDecimal("100"))
                        .toAccount("4321")
                        .memo("test transfer")
                        .build();

        ResponseEntity<String> response =
                restTemplate.postForEntity("/submit,process,result", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Invalid account"));
    }
}
