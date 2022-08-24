package com.pedro.arauz.entity;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "permissions")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Builder
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    @NotNull
    private String name;
    @NotNull
    private String domainAction;
    @ManyToMany(mappedBy = "permissions", cascade = {CascadeType.ALL})
    @Builder.Default
    private Set<Roles> roles = new HashSet<>();
}
