package com.booking.arena.service.secure;


import com.booking.arena.entity.user.Privilege;
import com.booking.arena.entity.user.RoleEntity;
import com.booking.arena.entity.user.RolePrivilege;
import com.booking.arena.entity.user.UserEntity;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        RoleEntity role = user.getRole();
        Collection<Privilege> rolePrivilege =
                role.getRolePrivileges().stream()
                        .map(RolePrivilege::getPrivilege).collect(Collectors.toList());
        return UserPrincipal.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .isActive(user.isActive())
                .authorities(getAuthorities(rolePrivilege))
                .build();
    }
    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Privilege> privileges) {

        return getGrantedAuthorities(getPrivileges(privileges));
    }

    private List<String> getPrivileges(Collection<Privilege> privilege) {

        List<String> privileges = new ArrayList<>();
        for (Privilege item : privilege) {
                privileges.add(item.getName());

        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
