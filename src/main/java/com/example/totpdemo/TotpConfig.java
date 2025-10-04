package com.example.totpdemo;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.spring.autoconfigure.TotpProperties;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TotpConfig {

    @Bean
    public CodeGenerator codeGenerator(TotpProperties totpProperties) {
        return new DefaultCodeGenerator(totpProperties.getAlgorithm());
    }

    @Bean
    public TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }
}
