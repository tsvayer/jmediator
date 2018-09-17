package jmediator.validation;

import jmediator.Message;
import jmediator.MessagingMiddleware;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

public class HibernateValidationMiddleware implements MessagingMiddleware {
  private final MessagingMiddleware next;
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public HibernateValidationMiddleware(MessagingMiddleware next) {
    this.next = next;
  }

  @Override
  public <T extends Message<R>, R> Mono<R> invoke(T message) {
    var constraintViolations = validator.validate(message);
    if (constraintViolations.size() > 0)
      throw new ConstraintViolationException(constraintViolations);
    return next.invoke(message);
  }
}