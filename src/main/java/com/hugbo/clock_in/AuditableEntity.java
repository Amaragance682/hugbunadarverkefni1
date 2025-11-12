package com.hugbo.clock_in;

import java.util.Arrays;
import java.util.Optional;

public enum AuditableEntity {
    USER("users", "id", "user_company_contracts", "user_id", "company_id"),
    TASK("tasks", "id", "tasks", "id", "company_id"),
    SHIFT("shifts", "id", "shifts", "id", "company_id"),
    LOCATION("locations", "id", "locations", "id", "company_id");

    public final String entityTypeName;
    public final String entityIdColumn;
    public final String tableName;
    public final String tableJoinColumn;
    public final String companyColumn;

    AuditableEntity(String entityTypeName, String entityIdColumn, String tableName, String tableJoinColumn, String companyColumn) {
        this.entityTypeName = entityTypeName;
        this.entityIdColumn = entityIdColumn;
        this.tableName = tableName;
        this.tableJoinColumn = tableJoinColumn;
        this.companyColumn = companyColumn;
    }

    public static Optional<AuditableEntity> fromString(String type) {
        return Arrays.stream(values())
            .filter(e -> e.entityTypeName.equalsIgnoreCase(type))
            .findFirst();
    }
}
