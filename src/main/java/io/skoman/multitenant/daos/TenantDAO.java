package io.skoman.multitenant.daos;

import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.entities.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TenantDAO extends JpaRepository<Tenant, UUID> {

    @Query("select new io.skoman.multitenant.dtos.TenantDTO(t.id, t.name) from Tenant t")
    Page<TenantDTO> tenants(Pageable pageable);
}
