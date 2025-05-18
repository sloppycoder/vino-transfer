package net.vino9.vino.demo.biz.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    String refId;
    String fromAccount;
    String toAccount;
    String currency;
    BigDecimal amount;
    BigDecimal fromAccountBalance;
    BigDecimal toAccountBalance;
    String status;
    String memo;
}
