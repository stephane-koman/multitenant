package io.skoman.multitenant.services;

import io.skoman.multitenant.dtos.TenantCreaDTO;
import io.skoman.multitenant.dtos.TenantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TenantService {
    TenantDTO addTenant(TenantCreaDTO dto);
    Page<TenantDTO> search(Pageable pageable);
    TenantDTO findByTenantId(UUID tenantId);
}
