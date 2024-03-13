package io.skoman.multitenant.entities.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Table(name = "privileges")
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
