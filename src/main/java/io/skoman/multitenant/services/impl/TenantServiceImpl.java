package io.skoman.multitenant.services.impl;

import io.skoman.multitenant.daos.TenantDAO;
import io.skoman.multitenant.dtos.TenantCreaDTO;
import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.entities.Tenant;
import io.skoman.multitenant.mappers.TenantMapper;
import io.skoman.multitenant.services.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantDAO tenantDAO;

    @Override
    public TenantDTO addTenant(TenantCreaDTO dto) {
        Tenant tenant = TenantMapper.INSTANCE.tenantCreaDTOToTenant(dto);
        Tenant tenantSaved = tenantDAO.save(tenant);
        return TenantMapper.INSTANCE.tenantToTenantDTO(tenantSaved);
    }

    @Override
    public Page<TenantDTO> search(Pageable pageable) {
        return tenantDAO.tenants(pageable);
    }
}
