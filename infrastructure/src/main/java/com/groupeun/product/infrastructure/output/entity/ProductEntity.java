package com.groupeun.product.infrastructure.output.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity implements Serializable {

    @Id
    private UUID id;
    @NonNull
    private String name;
    private String description;
    private double nutritionalScore;
    @NonNull
    private String type;
    @NonNull
    private String category;

    @Setter(AccessLevel.NONE)
    @ElementCollection
    private Set<String> allergens = new HashSet<>();

}
