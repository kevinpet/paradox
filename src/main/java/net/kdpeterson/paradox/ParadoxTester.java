package net.kdpeterson.paradox;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * A class with a simple main method for testing Paradox Agent
 */
public class ParadoxTester {
  public static void main(String[] args) {
    System.out.println(new DateTime(DateTimeZone.UTC));
  }
}
