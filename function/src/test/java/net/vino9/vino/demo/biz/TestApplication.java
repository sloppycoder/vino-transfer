package net.vino9.vino.demo.biz;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(JedisMockConfig.class)
public class TestApplication {}
