package com.smartremind.auth_service.entity;


import com.smartremind.auth_service.enums.Role;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "user_name", length = 250 , nullable = false)
    private String username;

    @Column(name = "password" , length = 250 , nullable = false)
    private String password;

    @Column(name = "role", length = 50 , nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "enabled" , nullable = false)
    private Boolean enabled;


}
