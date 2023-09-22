package my.finr.finr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.finr.finr.enumeration.RoleEnum;
import my.finr.finr.exceptionHandling.UserNotFoundException;
import my.finr.finr.model.User;
import my.finr.finr.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    public User findByNickname(String nickname) throws UserNotFoundException {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new UserNotFoundException());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAll(RoleEnum role) {
        return userRepository.findAllByRole(role);
    }
}
