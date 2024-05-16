package com.picpaysimplificado.repository;

import com.picpaysimplificado.domain.User;
import com.picpaysimplificado.domain.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository  userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Shoudl get User success from DB")
    void findUserByDocumentSuccess() {
        String document = "3454365";
        UserDTO userDTO = new UserDTO("Fabiano", "Teste", "3454365", new BigDecimal(10), "teste@gamil.com", "33333", UserType.COMMON);
        this.createUser(userDTO);
        Optional<User> userByDocument = this.userRepository.findUserByDocument(document);

        assertThat(userByDocument.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Shoudl not get User from DB when user not exists")
    void findUserByDocumentTwo() {
        String document = "3454365";

        Optional<User> userByDocument = this.userRepository.findUserByDocument(document);

        assertThat(userByDocument.isEmpty()).isTrue();
    }

    private User createUser(UserDTO userDTO){
        User user = new User(userDTO);
        this.entityManager.persist(user);
        return user;
    }
}