package net.vino9.vino.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import net.vino9.vino.demo.biz.model.Transfer;
import net.vino9.vino.demo.biz.model.TransferRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransferRestTests {

    @Autowired MockMvc mockMvc;
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
                restTemplate.postForEntity("/transfer_request", request, Transfer.class);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getRefId());
    }
}
