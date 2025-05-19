package net.vino9.vino.demo.biz.service;

import lombok.extern.slf4j.Slf4j;
import net.vino9.vino.demo.biz.model.Transfer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransferStore {

    private final RedisTemplate<String, Transfer> redisTemplate;
    private static final String KEY_PREFIX = "transfer:";

    public TransferStore(RedisTemplate<String, Transfer> redisTemplate) {
        this.redisTemplate = redisTemplate;
        log.info("TransferStore initialized");
    }

    public void save(Transfer transfer) {
        redisTemplate
                .opsForValue()
                .set(String.format("%s:%s", KEY_PREFIX, transfer.getRefId()), transfer);
    }

    public Transfer find(String refId) {
        return redisTemplate.opsForValue().get(String.format("%s:%s", KEY_PREFIX, refId));
    }
}
