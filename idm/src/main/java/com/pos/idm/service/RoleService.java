package com.pos.idm.service;

import com.pos.idm.entity.RoleEntity;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleEntity findRoleByName(String role) throws RoleNotFoundException;
    Set<RoleEntity> findRolesByName(List<String> roles) throws RoleNotFoundException;
}
