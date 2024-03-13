package io.skoman.multitenant;

import io.skoman.multitenant.config.TenantContext;
import io.skoman.multitenant.daos.TenantDAO;
import io.skoman.multitenant.daos.user.PrivilegeDAO;
import io.skoman.multitenant.daos.user.RoleDAO;
import io.skoman.multitenant.daos.user.UserDAO;
import io.skoman.multitenant.entities.Tenant;
import io.skoman.multitenant.entities.user.Privilege;
import io.skoman.multitenant.entities.user.Role;
import io.skoman.multitenant.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private TenantDAO tenantDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PrivilegeDAO privilegeDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            if (alreadySetup)
                return;

            Tenant tenant = tenantDAO.save(Tenant.builder()
                    .name("Test Company").build());

            TenantContext.setTenantInfo(String.valueOf(tenant.getId()));

            Privilege readPrivilege
                    = createPrivilegeIfNotFound("READ_PRIVILEGE");
            Privilege writePrivilege
                    = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

            List<Privilege> adminPrivileges = Arrays.asList(
                    readPrivilege, writePrivilege);
            createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
            createRoleIfNotFound("ROLE_STAFF", Collections.singletonList(writePrivilege));
            createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

            Role adminRole = roleDAO.findByName("ROLE_USER");
            User user = new User();
            user.setFirstName("Test");
            user.setLastName("Test");
            user.setPassword(passwordEncoder.encode("test"));
            user.setEmail("test@test.com");
            user.setRoles(Collections.singletonList(adminRole));
            user.setTenant(String.valueOf(tenant.getId()));
            user.setEnabled(true);
            userDAO.save(user);

            alreadySetup = true;
        });
        executor.close();
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeDAO.findByName(name);
        if (privilege == null) {
            privilege = Privilege.builder()
                    .name(name).build();
            privilegeDAO.save(privilege);
        }
        return privilege;
    }

    private void createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleDAO.findByName(name);
        if (role == null) {
            role = Role.builder().name(name).build();
            role.setPrivileges(privileges);
            roleDAO.save(role);
        }
    }
}