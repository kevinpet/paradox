package net.kdpeterson.paradox;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParadoxAgentTest {

  Logger log = LoggerFactory.getLogger(ParadoxAgentTest.class);

  @Before @After
  public void beforeAndAfter() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  @Test
  public void testFixedTime() {
    ParadoxAgent.premain("fixed=2016-01-01T12:00:00.000Z");
    DateTime now = new DateTime(DateTimeZone.UTC);
    assertEquals(new DateTime(2016, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), now);
  }

  @Test
  public void testOffset() {
    DateTime realNow = new DateTime(System.currentTimeMillis());
    ParadoxAgent.premain("offset=-100");
    DateTime now = new DateTime();
    int offset = Seconds.secondsBetween(realNow, now).getSeconds();
    assertTrue("expected " + -100 + ", was " + offset, -101 <= offset && offset <= -99);
  }

  private static class MinMaxConsumer<T extends Comparable<? super T>> implements Consumer<T> {
    T min;
    T max;

    @Override
    public void accept(T t) {
      if (min == null || t.compareTo(min) < 0) {
        min = t;
      }
      if (max == null || t.compareTo(max) > 0) {
        max = t;
      }
    }
  }
  @Test
  public void testRandomDay() {
    DateTime realNow = new DateTime(System.currentTimeMillis());
    ParadoxAgent.premain("random=day");
    MinMaxConsumer<DateTime> minMax = new MinMaxConsumer<>();
    IntStream.range(1, 20).mapToObj(i -> new DateTime()).forEach(minMax);
    Duration range = new Duration(minMax.min, minMax.max);
    assertTrue("Expected min...max to span 12-24 hours, but was " + range,
            Duration.standardHours(12).isShorterThan(range) && Duration.standardHours(24).isLongerThan(range));
  }

  @Test
  public void testRandomYear() {
    DateTime realNow = new DateTime(System.currentTimeMillis());
    ParadoxAgent.premain("random=year");
    MinMaxConsumer<DateTime> minMax = new MinMaxConsumer<>();
    IntStream.range(1, 20).mapToObj(i -> new DateTime()).forEach(minMax);
    Duration range = new Duration(minMax.min, minMax.max);
    assertTrue("Expected min...max to span 180-366 days, but was " + range,
            Duration.standardDays(180).isShorterThan(range) && Duration.standardDays(366).isLongerThan(range));
  }
}
