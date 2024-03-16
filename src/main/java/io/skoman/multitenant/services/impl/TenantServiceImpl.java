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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantDAO tenantDAO;

    @Override
    public TenantDTO addTenant(TenantCreaDTO dto) {
        Optional<Tenant> tenantOp = tenantDAO.findByName(dto.name());
        if(tenantOp.isPresent())
            throw new RuntimeException("This company name already exists");

        Tenant tenant = TenantMapper.INSTANCE.tenantCreaDTOToTenant(dto);
        Tenant tenantSaved = tenantDAO.save(tenant);
        return TenantMapper.INSTANCE.tenantToTenantDTO(tenantSaved);
    }

    @Override
    public Page<TenantDTO> search(Pageable pageable) {
        return tenantDAO.tenants(pageable);
    }

    @Override
    public TenantDTO findByTenantId(UUID tenantId) {
        Optional<Tenant> tenant = tenantDAO.findById(tenantId);

        if(tenant.isEmpty())
            throw new RuntimeException("Company associated to this user not found");

        return TenantMapper.INSTANCE.tenantToTenantDTO(tenant.get());
    }
}
