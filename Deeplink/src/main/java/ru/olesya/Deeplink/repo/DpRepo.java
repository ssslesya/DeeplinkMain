package ru.olesya.Deeplink.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olesya.Deeplink.model.Dp;

import java.util.List;

@Repository
public interface DpRepo extends JpaRepository<Dp, String> {
    List<Dp> findAllByUserr(Long user);

    Dp findByUserrAndMpAndProduct(Long user, String mp, Long product);
}
