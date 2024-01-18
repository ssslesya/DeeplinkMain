package ru.olesya.Deeplink.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olesya.Deeplink.model.Ozon;

import java.util.List;

@Repository
public interface OzonRepo extends JpaRepository<Ozon, String> {
    List<Ozon> findAllByProduct(Long product);
}
