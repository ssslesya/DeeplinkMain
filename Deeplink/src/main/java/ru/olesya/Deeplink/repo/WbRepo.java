package ru.olesya.Deeplink.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olesya.Deeplink.model.Wb;

import java.util.List;
@Repository
public interface WbRepo extends JpaRepository<Wb, String> {
    List<Wb> findAllByProduct(Long product);
}
