package com.sandeepprabhakula;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenGeneratorAutoConfig {
    @Bean
    @ConditionalOnProperty(prefix = "jwt.abstract",name = "enabled",havingValue = "true",matchIfMissing = false)
    @ConditionalOnProperty(prefix = "jwt.abstract",name = "secret",matchIfMissing = false)
    public TokenGenerator getTokenGenerator(){
        System.out.println("JWT abstract autoconfiguration done");
        return new TokenGenerator();
    }
}
