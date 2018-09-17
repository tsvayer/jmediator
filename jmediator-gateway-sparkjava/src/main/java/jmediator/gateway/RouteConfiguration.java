package jmediator.gateway;

import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;

public class RouteConfiguration {
  private final static String ENDPOINT = "/api/:module/:message";

  public RouteConfiguration configure(SparkjavaGateway apiController, Integer port) {
    port(port);

    get(ENDPOINT, apiController::handle);
    post(ENDPOINT, apiController::handle);
    return this;
  }
}
