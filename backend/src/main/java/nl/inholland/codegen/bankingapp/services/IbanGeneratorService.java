package nl.inholland.codegen.bankingapp.services;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import nl.inholland.codegen.bankingapp.repositories.AccountRepository;

@Service
public class IbanGeneratorService {

    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();

    public IbanGeneratorService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String generate() {

        String iban;

        do {
            String accountNumber = String.format(
                    "%010d",
                    Math.abs(random.nextLong()) % 10000000000L
            );

            iban = "NL91INHO" + accountNumber;

        } while (accountRepository.findByIban(iban).isPresent());

        return iban;
    }
}