package com.dmdev.spring.integration.database.repository;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.spring.dto.PersonalInfo;
import com.dmdev.spring.dto.PersonalInfo2;
import com.dmdev.spring.dto.UserFilter;
import com.dmdev.spring.integration.IntegrationTestBase;
import com.dmdev.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {
    private final UserRepository userRepository;

    @Test
    void checkJdbcTemplate() {
        List<PersonalInfo> users = userRepository.findAllByCompanyIdAndRole(1, Role.USER);
        assertThat(users).hasSize(1);
    }

    @Test
    void checkAuditing() {
        var ivan = userRepository.findById(1L).get();
        ivan.setBirthDate(ivan.getBirthDate().plusYears(1L));
        userRepository.flush();
    }

    @Test
    void checkCustomImplementation() {
        UserFilter filter = new UserFilter(null, "%ov%", LocalDate.now());
        List<User> users = userRepository.findAllByFilter(filter);

    }

    @Test
    void checkProjections() {
        List<PersonalInfo2> users = userRepository.findAllByCompanyId(1);
        users.forEach(user -> System.out.println(user.getBirthDate()));
        assertThat(users).hasSize(2);
    }

    @Test
    void testPageable() {
        var pageable = PageRequest.of(0, 2, Sort.by("id"));
        var slice = userRepository.findAllBy(pageable);
        slice.forEach(user -> System.out.println(user.getCompany().getName()));

        while (slice.hasNext()) {
            slice = userRepository.findAllBy(slice.nextPageable());
            slice.forEach(user -> System.out.println(user.getId()));
        }
    }

    @Test
    void checkSort() {
        var sortById = Sort.by("id");
    }

    @Test
    void checkFirstTop() {
        var topUser = userRepository.findTopByOrderByIdDesc();
        assertTrue(topUser.isPresent());
        topUser.ifPresent(user -> assertEquals(5L, user.getId()));
    }

    @Test
    void checkUpdate() {
        int resultCount = userRepository.updateRole(Role.USER, 1L, 5L);
        assertEquals(2, resultCount);
    }

    @Test
    void checkQueries() {
        var users = userRepository.findAllBy("a", "ov");
        System.out.println(users);
    }

}