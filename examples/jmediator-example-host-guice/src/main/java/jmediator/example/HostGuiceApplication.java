package jmediator.example;

import jmediator.MessageHandlersInstaller;
import jmediator.MessagingInstaller;
import jmediator.gateway.Installer;
import com.google.inject.Guice;

import java.util.Scanner;

public class HostGuiceApplication {
  public static void main(String[] args) {
    Guice.createInjector(
      new MessagingInstaller(),
      new MessageHandlersInstaller(),
      new Installer());

    System.out.println("started...");
    new Scanner(System.in).next();
  }
}
