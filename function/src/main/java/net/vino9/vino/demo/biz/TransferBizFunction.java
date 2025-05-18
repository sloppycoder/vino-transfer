package net.vino9.vino.demo.biz;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import net.vino9.vino.demo.biz.exception.ProcessFailedException;
import net.vino9.vino.demo.biz.exception.ValidationException;
import net.vino9.vino.demo.biz.model.Transfer;
import net.vino9.vino.demo.biz.model.TransferRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferBizFunction {

    private final Map<String, Transfer> transferStore = new HashMap<>();

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Bean
    public Function<TransferRequest, String> validate() {
        return request -> {
            Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
            if (!violations.isEmpty()) {
                throw new ValidationException(violations.iterator().next().getMessage());
            }

            if (!request.getFromAccount().equals("1111")) {
                String refId = UUID.randomUUID().toString();
                request.setRefId(refId);
                transferStore.put(refId, toTransfer(request, "VALIDATED"));
                return refId;
            } else {
                throw new ValidationException("Invalid account 1111");
            }
        };
    }

    @Bean
    public Function<String, String> process() {
        return refId -> {
            Transfer transfer = transferStore.get(refId);
            if (transfer == null) throw new ProcessFailedException("Transfer not found");
            // Simulate processing
            transfer.setStatus("PROCESSED");
            return refId;
        };
    }

    @Bean
    public Function<String, Transfer> result() {
        return refId -> {
            Transfer transfer = transferStore.get(refId);
            if (transfer == null) throw new ProcessFailedException("Transfer not found");
            return transfer;
        };
    }

    @Bean
    public Function<TransferRequest, Transfer> transfer_request() {
        return request -> {
            String ref = validate().apply(request);
            ref = process().apply(ref);
            return result().apply(ref);
        };
    }

    private Transfer toTransfer(TransferRequest req, String status) {
        return Transfer.builder()
                .refId(req.getRefId())
                .fromAccount(req.getFromAccount())
                .toAccount(req.getToAccount())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .memo(req.getMemo())
                .status(status)
                .fromAccountBalance(new BigDecimal("200.00"))
                .toAccountBalance(new BigDecimal("400.00"))
                .build();
    }
}
