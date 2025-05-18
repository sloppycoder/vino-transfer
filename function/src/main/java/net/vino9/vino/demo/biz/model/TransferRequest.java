package net.vino9.vino.demo.biz.model;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    String refId;
    @NotNull String fromAccount;
    @NotNull String toAccount;
    @NotNull BigDecimal amount;
    @NotNull String currency;
    @NotNull String memo;
}
