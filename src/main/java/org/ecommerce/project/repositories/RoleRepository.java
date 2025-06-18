package org.ecommerce.project.repositories;

import org.ecommerce.project.models.Role;
import org.ecommerce.project.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
