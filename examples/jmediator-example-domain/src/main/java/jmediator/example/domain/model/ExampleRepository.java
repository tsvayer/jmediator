package jmediator.example.domain.model;

import java.util.Optional;

public interface ExampleRepository extends Repository<ExampleEntity, Long> {

    Optional<ExampleEntity> findByValue(String value);
}
