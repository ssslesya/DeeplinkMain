package ru.olesya.Deeplink.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dp")
public class Dp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userr;
    private String mp;
    private Long product;

    public Dp(Long user, String mp, Long product) {
        this.userr = user;
        this.mp = mp;
        this.product = product;
    }
}
