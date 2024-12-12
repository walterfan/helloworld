package com.github.walterfan.hellospringsecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.github.walterfan.hellospringsecurity.User user = userRepository.findByUsername(username);
        List<GrantedAuthority> authorities=buildUserAuthority(user.getUserRole());
        return buildUserForAuthentication(user,authorities);
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(
            com.github.walterfan.hellospringsecurity.User user,List<GrantedAuthority> authorities){
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user.isEnabled(),true,true,true,authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles){
        Set<GrantedAuthority> setAuths=new HashSet<GrantedAuthority>();
        for(UserRole userRole:userRoles){
            setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
        }
        List<GrantedAuthority> result=new ArrayList<GrantedAuthority>(setAuths);
        return result;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public User getActiveUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User activeUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return userRepository.findByEmail(activeUser.getUsername());
    }
}