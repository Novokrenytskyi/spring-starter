package com.dmdev.spring.integration.repository;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.repository.CompanyRepository;
import com.dmdev.spring.integration.IntegrationTestBase;
import com.dmdev.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class CompanyRepositoryTest extends IntegrationTestBase {
    private static final Integer AMAZON_ID = 3;
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    private final CompanyRepository companyRepository;


    @Test
    void checkFindByQueries() {
        companyRepository.findByName("Google");
        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    void delete() {
        var maybeCompany = companyRepository.findById(AMAZON_ID);
        assertTrue(maybeCompany.isPresent());
        maybeCompany.ifPresent(companyRepository::delete);
        entityManager.flush();
        assertTrue(companyRepository.findById(AMAZON_ID).isEmpty());
    }

    @Test
    void findById() {
        transactionTemplate.executeWithoutResult(tx -> {
            var company = entityManager.find(Company.class, 1);
            assertNotNull(company);
            Assertions.assertThat(company.getLocales()).hasSize(2);

        });
    }

}