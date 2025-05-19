package net.vino9.vino.demo.biz;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.function.Function;
import net.vino9.vino.demo.biz.exception.ValidationException;
import net.vino9.vino.demo.biz.model.Transfer;
import net.vino9.vino.demo.biz.model.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TestApplication.class})
class TransferBizFuncTests {

    @Autowired TransferBizFunction functions;

    @Test
    void testSubmitSuccess() {
        TransferRequest request =
                TransferRequest.builder()
                        .fromAccount("1234")
                        .currency("USD")
                        .amount(new BigDecimal("100"))
                        .toAccount("4321")
                        .memo("test transfer")
                        .build();
        Function<TransferRequest, String> validate = functions.submit();
        String refId = validate.apply(request);

        assertNotNull(refId);
    }

    @Test
    void testSubmitFailure() {
        TransferRequest request =
                TransferRequest.builder()
                        .fromAccount("1111")
                        .currency("USD")
                        .amount(new BigDecimal("100"))
                        .toAccount("4321")
                        .memo("test transfer")
                        .build();
        assertThrows(ValidationException.class, () -> functions.submit().apply(request));
    }

    @Test
    void testPipelineSuccess() {
        TransferRequest request =
                TransferRequest.builder()
                        .fromAccount("1234")
                        .currency("USD")
                        .amount(new BigDecimal("100"))
                        .toAccount("4321")
                        .memo("test transfer")
                        .build();
        String refId = functions.submit().apply(request);
        String processedRefId = functions.process().apply(refId);
        Transfer transfer = functions.result().apply(processedRefId);

        assertEquals("PROCESSED", transfer.getStatus());
    }
}
