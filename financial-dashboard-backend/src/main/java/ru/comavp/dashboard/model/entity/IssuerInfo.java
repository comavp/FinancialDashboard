package ru.comavp.dashboard.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issuer_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IssuerInfo extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String ticker;
    @Column
    private String issuerName;
    @Column
    private String issuerDescription;
    @Column
    private String isin;
    @Column
    private String category;
}
