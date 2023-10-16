package com.dmdev.spring.database.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@NamedQuery(
        name = "Company.findByName",
        query = "select c from Company c where lower(c.name) = lower(:name)"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "locales")
@EqualsAndHashCode(of = "locales")
@Builder
@Entity
@Table(name = "company")
public class Company  implements BaseEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locales", joinColumns = @JoinColumn(name = "company_id"))
    @MapKeyColumn(name = "lang")
    @Column(name = "description")
    private Map<String, String> locales = new HashMap<>();



}
