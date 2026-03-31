package com.roamster.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Mirrors 'merchant' table. Represents sellers of clothing (rent/buy) or food.
 */
@Entity
@Table(name = "merchant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "type", length = 50)
    private String type;           // CLOTHING | FOOD | MULTI

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "rating")
    private Double rating;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
