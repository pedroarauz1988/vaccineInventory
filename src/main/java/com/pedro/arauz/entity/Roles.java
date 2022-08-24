package com.pedro.arauz.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="roles")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Builder
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique=true)
    @NotNull
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable()
    @Builder.Default
    private Set<com.pedro.arauz.entity.Permission> permissions = new HashSet<>();
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.ALL})
    @Builder.Default
    private Set<User> users = new HashSet<>();
}
