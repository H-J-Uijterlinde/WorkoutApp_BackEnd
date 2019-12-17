package com.semafoor.semaforce.services;

import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.model.view.UserView;
import com.semafoor.semaforce.model.view.WorkoutView;
import com.semafoor.semaforce.repositories.UserRepository;
import com.semafoor.semaforce.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private PasswordEncoder bcryptEncoder;

    @InjectMocks
    private UserService userService;


    private User user1;
    private User user2;
    private List<User> users = new ArrayList<>();
    private UserView userView;
    private WorkoutView workoutView;
    private Workout workout;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.user1 = mock(User.class);
        this.user2 = mock(User.class);
        this.userView = mock(UserView.class);
        this.workoutView = mock(WorkoutView.class);
        this.workout = mock(Workout.class);
        users.add(user1);
        users.add(user2);
    }

    @Test
    void findAll() {
        when(this.userRepository.findAll()).thenReturn(this.users);

        List<User> returnedUsers = this.userService.findAll();
        verify(this.userRepository).findAll();
        assertThat(returnedUsers).hasSize(2).contains(user1, user2);
    }

    @Test
    void findUserViewByUsername() {
        when(this.userRepository.findLoggedInUserByUserName("User")).thenReturn(userView);

        UserView returnedUser = this.userService.findUserViewByUsername("User");
        verify(this.userRepository).findLoggedInUserByUserName("User");
        assertThat(returnedUser).isNotNull();
    }

    @Test
    void findById() {
        when(this.userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        User returnedUser = this.userService.findById(1L);
        verify(this.userRepository).findById(1L);
        assertThat(returnedUser).isEqualTo(user1);
    }

    @Test
    void testFindByInvalidIdReturnsNull() {
        when(this.userRepository.findById(3L)).thenReturn(Optional.empty());

        User returnedUser = this.userService.findById(3L);
        verify(this.userRepository).findById(3L);
        assertThat(returnedUser).isNull();
    }

    @Test
    void getUserViews() {
        when(this.userRepository.getLoggedInUsers()).thenReturn(Collections.singletonList(this.userView));

        List<UserView> returnedUserViews = this.userService.getUserViews();
        verify(this.userRepository).getLoggedInUsers();
        assertThat(returnedUserViews).hasSize(1).contains(this.userView);
    }

    @Test
    void getCurrentWorkoutView() {
        when(this.userRepository.getCurrentWorkoutView(1L)).thenReturn(this.workoutView);

        WorkoutView returnedWorkout = this.userService.getCurrentWorkoutView(1L);
        verify(this.userRepository).getCurrentWorkoutView(1L);
        assertThat(returnedWorkout).isNotNull().isEqualTo(this.workoutView);
    }

    @Test
    void saveUser() {
        when(this.user1.getPassword()).thenReturn("password");
        when(this.bcryptEncoder.encode("password")).thenReturn("encryptedPassword");
        when(this.userRepository.save(user1)).thenReturn(user1);

        User returnedUser = this.userService.saveUser(user1);
        verify(this.user1, times(1)).getPassword();
        verify(this.bcryptEncoder, times(1)).encode("password");
        assertThat(returnedUser).isNotNull().isEqualTo(user1);
    }

    @Test
    void deleteUser() {
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(this.workoutRepository.findAllByUser(user1)).thenReturn(Collections.singletonList(this.workout));

        this.userService.deleteUser(1L);
        verify(this.userRepository).findById(1L);
        verify(this.workoutRepository).findAllByUser(user1);
        verify(this.workoutRepository).delete(this.workout);
    }
}