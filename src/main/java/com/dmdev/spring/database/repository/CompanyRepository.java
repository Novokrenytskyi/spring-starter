package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findByName(String name);

    List<Company> findAllByNameContainingIgnoreCase(String fragment);
}
