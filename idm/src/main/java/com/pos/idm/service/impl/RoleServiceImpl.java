package com.pos.idm.service.impl;

import com.pos.idm.entity.RoleEntity;
import com.pos.idm.repository.RoleRepository;
import com.pos.idm.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleEntity findRoleByName(String role) throws RoleNotFoundException {
        return roleRepository.findByRole(role).orElseThrow(RoleNotFoundException::new);
    }

    @Override
    public Set<RoleEntity> findRolesByName(List<String> roles) throws RoleNotFoundException {
        Set<RoleEntity> roleEntities = new HashSet<>();
        for(String role : roles) {
            roleEntities.add(roleRepository.findByRole(role).orElseThrow(RoleNotFoundException::new));
        }
        return roleEntities;
    }
}
