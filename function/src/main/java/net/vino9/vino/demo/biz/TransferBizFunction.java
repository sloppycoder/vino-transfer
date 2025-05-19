package net.vino9.vino.demo.biz;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import net.vino9.vino.demo.biz.exception.ProcessFailedException;
import net.vino9.vino.demo.biz.exception.ValidationException;
import net.vino9.vino.demo.biz.model.Transfer;
import net.vino9.vino.demo.biz.model.TransferRequest;
import net.vino9.vino.demo.biz.service.TransferStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TransferBizFunction {

    private final TransferStore transferStore;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public TransferBizFunction(TransferStore store) {
        this.transferStore = store;
    }

    @Bean
    public Function<TransferRequest, String> submit() {
        return request -> {
            Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
            if (!violations.isEmpty()) {
                throw new ValidationException(violations.iterator().next().getMessage());
            }

            if (!request.getFromAccount().equals("1111")) {
                String refId = UUID.randomUUID().toString().substring(24);
                request.setRefId(refId);
                transferStore.save(toTransfer(request, "VALIDATED"));
                log.info("Transfer {} submitted", refId);
                return refId;
            } else {
                throw new ValidationException("Invalid account 1111");
            }
        };
    }

    @Bean
    public Function<String, String> process() {
        return refId -> {
            Transfer transfer = transferStore.find(refId);
            if (transfer == null) throw new ProcessFailedException("Transfer not found");
            if (transfer.getAmount().equals(new BigDecimal("88"))) {
                log.info("Transfer {} failed", refId);
                transfer.setStatus("FAILED");
            } else {
                log.info("Transfer {} success", refId);
                transfer.setStatus("PROCESSED");
            }
            transferStore.save(transfer);

            return refId;
        };
    }

    @Bean
    public Function<String, Transfer> result() {
        return refId -> {
            Transfer transfer = transferStore.find(refId);
            if (transfer == null) throw new ProcessFailedException("Transfer not found");
            log.info("Transfer {} result: {}", refId, transfer.getStatus());
            return transfer;
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
