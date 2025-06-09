package ru.comavp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalId;
    private LocalDateTime receiptCreationDate;
    private Long totalSum;
    private String retailPlace;
    private String type;

    @OneToMany(mappedBy = "receipt")
    private List<Item> items;

    @ManyToOne
    private Request request;
}
