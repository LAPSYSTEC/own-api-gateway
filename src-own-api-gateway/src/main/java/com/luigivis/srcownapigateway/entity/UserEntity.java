package com.luigivis.srcownapigateway.entity;


import brave.internal.Nullable;
import com.luigivis.srcownapigateway.enums.RolesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private String uuid;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "ENUM('ADMIN','CLIENT','EMPLOYEES') notnull default 'ADMIN' ")
    private RolesEnum roles;

    @Column(nullable = false, columnDefinition = "default true")
    private Boolean status;
}
