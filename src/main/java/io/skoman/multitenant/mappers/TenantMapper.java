package io.skoman.multitenant.mappers;

import io.skoman.multitenant.dtos.TenantCreaDTO;
import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.entities.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TenantMapper {
    TenantMapper INSTANCE = Mappers.getMapper(TenantMapper.class);

    Tenant tenantCreaDTOToTenant(TenantCreaDTO dto);

    TenantDTO tenantToTenantDTO(Tenant tenant);
}
