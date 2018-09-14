package jmediator.example.persistence;

import jmediator.example.domain.model.ExampleEntity;
import jmediator.example.domain.model.ExampleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepositoryJpa
        extends JpaRepository<ExampleEntity, Long>, ExampleRepository {
}
