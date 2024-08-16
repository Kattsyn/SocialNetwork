package kattsyn.dev.SocialNetwork.services;

import kattsyn.dev.SocialNetwork.entities.Role;
import kattsyn.dev.SocialNetwork.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static kattsyn.dev.SocialNetwork.enums.Roles.ROLE_ADMIN;
import static kattsyn.dev.SocialNetwork.enums.Roles.ROLE_USER;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public Role getUserRole() {
        return roleRepository.findByName(ROLE_USER.name()).get();

    }
    public Role getAdminRole() {
        return roleRepository.findByName(ROLE_ADMIN.name()).get();
    }
}
