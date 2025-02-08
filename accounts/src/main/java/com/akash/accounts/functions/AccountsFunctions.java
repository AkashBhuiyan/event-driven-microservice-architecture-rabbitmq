package com.akash.accounts.functions;

import com.akash.accounts.service.IAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * Author: akash
 * Date: 8/2/25
 */

@Slf4j
@Configuration
public class AccountsFunctions {

    @Bean
    public Consumer<Long> updateCommunication(IAccountsService accountsService) {
        return accountNumber -> {
            log.info("Updating Communication status for the account number : {}", accountNumber.toString());
            accountsService.updateCommunicationStatus(accountNumber);
        };
    }
}
