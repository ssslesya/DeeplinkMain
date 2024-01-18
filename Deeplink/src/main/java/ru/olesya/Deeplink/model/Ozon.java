package ru.olesya.Deeplink.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ozon")
public class Ozon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;
    private Long product;

    public Ozon(LocalDateTime time, Long product) {
        this.time = time;
        this.product = product;
    }
}
