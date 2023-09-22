package my.finr.finr.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import my.finr.finr.model.User;
import my.finr.finr.repository.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findByNickname(nickname);
        if(user.isPresent()){
            return user.get();
        }
        throw new UsernameNotFoundException("'"+nickname + "' not found");
    }
    
}
