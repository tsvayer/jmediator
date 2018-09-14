package jmediator.example.persistence;

import jmediator.example.domain.model.ExampleEntity;
import jmediator.example.domain.model.ExampleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleRepositoryTest.class)
@ComponentScan(basePackages = "jmediator")
@EntityScan(basePackages = "jmediator")
@AutoConfigurationPackage
public class ExampleRepositoryTest {
    @Autowired
    ExampleRepository repository;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void doit() {
        var entity = new ExampleEntity();
        var value = "Yay!";
        entity.setValue(value);
        repository.save(entity);

        repository.findByValue(value)
                .map(ExampleEntity::getValue)
                .ifPresent(val -> logger.info("Value : {}", val));
    }

}