package com.levimartines.codewithspring.service;

import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.entities.vo.UserVO;
import com.levimartines.codewithspring.exceptions.AuthorizationException;
import com.levimartines.codewithspring.exceptions.ObjectNotFoundException;
import com.levimartines.codewithspring.kafka.UserProducer;
import com.levimartines.codewithspring.repository.UserRepository;
import com.levimartines.codewithspring.security.CustomUserDetails;
import com.levimartines.codewithspring.security.PrincipalService;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserProducer producer;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    void shouldCallRepositoryFindById() {
        String name = "Test";
        String email = "test@test.com";
        User user = User.builder().id(1L).name(name).email(email).isAdmin(true).build();
        Mockito.when(repository.findById(2L))
            .thenReturn(Optional.of(user));

        try (MockedStatic<PrincipalService> mocked = Mockito.mockStatic(PrincipalService.class)) {
            mocked.when(PrincipalService::authenticated).thenReturn(new CustomUserDetails(user));

            User resp = service.findById(2L);

            mocked.verify(PrincipalService::authenticated);
            Mockito.verify(repository, Mockito.times(1)).findById(2L);
            assertThat(resp).isNotNull();
            assertThat(resp.getName()).isEqualTo(name);
            assertThat(resp.getEmail()).isEqualTo(email);
        }
    }

    @Test
    void shouldThrowAnExceptionWhenNotFound() {
        String name = "Test";
        String email = "test@test.com";
        User user = User.builder().id(1L).name(name).email(email).isAdmin(true).build();

        try (MockedStatic<PrincipalService> mocked = Mockito.mockStatic(PrincipalService.class)) {
            mocked.when(PrincipalService::authenticated).thenReturn(new CustomUserDetails(user));
            assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));
        }
    }

    @Test
    void shouldThrowAnExceptionWhenUserHasNoPermission() {
        String name = "Test";
        String email = "test@test.com";
        User user = User.builder().id(1L).name(name).email(email).isAdmin(false).build();

        assertThrows(AuthorizationException.class, () -> service.findById(1L));
        try (MockedStatic<PrincipalService> mocked = Mockito.mockStatic(PrincipalService.class)) {
            mocked.when(PrincipalService::authenticated).thenReturn(new CustomUserDetails(user));
            assertThrows(AuthorizationException.class, () -> service.findById(2L));
        }
    }

    @Test
    void shouldCallRepositorySave() {
        String name = "Test";
        String email = "test@test.com";
        Mockito.when(repository.save(Mockito.any()))
            .thenReturn(User.builder().id(1L).name(name).email(email).build());

        User user = service.create(UserVO.builder().name(name).email(email).password("password").build());

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertNotNull(user);
        assertEquals(1L, user.getId());
    }
}