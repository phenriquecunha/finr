package my.finr.finr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import my.finr.finr.dto.RegisterDTO;
import my.finr.finr.enumeration.RoleEnum;
import my.finr.finr.exceptionHandling.UserExistsException;
import my.finr.finr.model.User;
import my.finr.finr.repository.UserRepository;

@Service
public class RegisterService {

    @Autowired
    UserRepository userRepository;

    public void registerUser(RegisterDTO dto) throws UserExistsException {
        var user = userRepository.findByNickname(dto.nickname());
        if (user.isPresent()) {
            throw new UserExistsException();
        }
        
        User newUser = convertRegisterDTOtoUser(dto);
        newUser.setPassword(new BCryptPasswordEncoder().encode(dto.password()));
        userRepository.save(newUser);
    }

    private static User convertRegisterDTOtoUser(RegisterDTO dto){
        User user = new User();
        user.setNickname(dto.nickname());
        user.setPassword(dto.password());
        user.setRole(RoleEnum.valueOf(dto.role()));
        return user;
    }
}
