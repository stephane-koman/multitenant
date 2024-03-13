package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.TenantCreaDTO;
import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.services.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/tenants")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantDTO> addTenant(@RequestBody TenantCreaDTO dto){
        return new ResponseEntity<>(tenantService.addTenant(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TenantDTO>> search(Pageable pageable){
        return ResponseEntity.ok(tenantService.search(pageable));
    }
}
