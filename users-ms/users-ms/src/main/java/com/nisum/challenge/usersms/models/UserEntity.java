package com.nisum.challenge.usersms.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "phone",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private List<PhoneEmbedded> phones;

    @CreationTimestamp
    private ZonedDateTime created;

    @UpdateTimestamp
    private ZonedDateTime modified;

    private ZonedDateTime lastLogin;

    private String token;

    private Boolean isActive;
}
