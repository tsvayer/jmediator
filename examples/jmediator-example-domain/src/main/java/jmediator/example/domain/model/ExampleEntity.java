package jmediator.example.domain.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@javax.persistence.Entity
public class ExampleEntity implements Entity<Long> {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String value;

  public Long getId() {
    return id;
  }

  public void setId(Long value) {
    id = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
