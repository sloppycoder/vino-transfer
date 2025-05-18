package net.vino9.vino.demo.biz;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.function.Function;
import net.vino9.vino.demo.biz.exception.ValidationException;
import net.vino9.vino.demo.biz.model.Transfer;
import net.vino9.vino.demo.biz.model.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TransferBizFunction.class)
class TransferBizFuncTests {
    TransferBizFunction functions = new TransferBizFunction();

    @Test
    void testValidateSuccess() {
        TransferRequest request =
                TransferRequest.builder()
                        .fromAccount("1234")
                        .currency("USD")
                        .amount(new BigDecimal("100"))
                        .toAccount("4321")
                        .memo("test transfer")
                        .build();
        Function<TransferRequest, String> validate = functions.validate();
        String refId = validate.apply(request);

        assertNotNull(refId);
    }

    @Test
    void testValidateFailure() {
        TransferRequest request =
                TransferRequest.builder()
                        .fromAccount("1111")
                        .currency("USD")
                        .amount(new BigDecimal("100"))
                        .toAccount("4321")
                        .memo("test transfer")
                        .build();
        assertThrows(ValidationException.class, () -> functions.validate().apply(request));
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
        String refId = functions.validate().apply(request);
        String processedRefId = functions.process().apply(refId);
        Transfer transfer = functions.result().apply(processedRefId);

        assertEquals("PROCESSED", transfer.getStatus());
    }
}
