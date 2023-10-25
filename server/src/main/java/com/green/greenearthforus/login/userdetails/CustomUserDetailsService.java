package com.green.greenearthforus.login.userdetails;

import com.green.greenearthforus.exception.BusinessLogicException;
import com.green.greenearthforus.exception.ExceptionCode;
import com.green.greenearthforus.login.util.CustomAuthorityUtils;
import com.green.greenearthforus.user.repository.UserRepository;
import com.green.greenearthforus.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CustomAuthorityUtils authorityUtils;

    public CustomUserDetailsService(UserRepository userRepository, CustomAuthorityUtils authorityUtils){
        this.userRepository = userRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String userUseId) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUserUseId(userUseId);
        User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return new CustomUserDetails(findUser);
    }

    private final class CustomUserDetails extends User implements UserDetails {
        CustomUserDetails(User user){
            setUserId(user.getUserId());
            setUserUseId(user.getUserUseId());
            setUserName(user.getUserName());
            setPassword(user.getPassword());
            setRole(user.getRole());
            setGrade(user.getGrade());
            setImageUrl(user.getImageUrl());
            setCreatedAt(user.getCreatedAt());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities(){
            return authorityUtils.createAuthorities(String.valueOf(this.getRole()));
        }

        @Override
        public String getUsername(){
            return getUserName();
        }

        @Override
        public boolean isAccountNonExpired(){
            return true;
        }

        @Override
        public boolean isAccountNonLocked(){
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired(){
            return true;
        }

        @Override
        public boolean isEnabled(){
            return true;
        }

    }

}
