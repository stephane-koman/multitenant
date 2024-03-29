package io.skoman.multitenant.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class TenantContext {
    private static final ThreadLocal<String> TENANT_INFO = new InheritableThreadLocal<>();

    public static String getTenantInfo() {
        return TENANT_INFO.get();
    }

    public static void setTenantInfo(String tenant) {
        TENANT_INFO.set(tenant);
    }

    public static void clear() {
        TENANT_INFO.remove();
    }
}
