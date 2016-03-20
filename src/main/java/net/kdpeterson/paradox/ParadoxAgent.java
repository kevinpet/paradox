package net.kdpeterson.paradox;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeUtils.MillisProvider;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.convert.DurationConverter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParadoxAgent {

  private static final Logger log = LoggerFactory.getLogger(ParadoxAgent.class);

  public static void premain(String agentArgs) {
    String[] fields = agentArgs.split("=");
    switch (fields[0]) {
      case "fixed":
        String dateString = fields[1];
        DateTime supplied = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(dateString);
        log.info("Setting fixed time of {} (supplied: {})", dateString, supplied);
        DateTimeUtils.setCurrentMillisFixed(supplied.getMillis());
        break;
      case "offset":
        int seconds = Integer.parseInt(fields[1]);
        log.info("Setting offset of {} seconds", seconds);
        DateTimeUtils.setCurrentMillisOffset(seconds * 1000);
        break;
      case "random":
        DateTimeUtils.setCurrentMillisProvider(random(fields[1]));
        break;
      default:
        log.warn("Unknown time setting: " + agentArgs);
        break;
    }
  }

  static RandomMillisProvider random(String range) {
    String seed = System.getProperty("paradox.random.seed");
    Random random = seed != null ? new Random(seed.hashCode()) : new Random();
    String anchor = System.getProperty("paradox.random.anchor");
    DateTime now = anchor != null ? new DateTime(anchor) : new DateTime();
    {
      Interval interval;
      switch (range) {
        case "year":
          DateTime start = new DateTime(now.getYear(), 1, 1, 0, 0, 0, 0, now.getZone());
          DateTime end = new DateTime(now.getYear() + 1, 1, 1, 0, 0, 0, 0, now.getZone());
          interval = new Interval(start, end);
          log.info("Providing random dates over {}.", interval);
          return new RandomMillisProvider(random, interval);
        case "day":
        default:
          interval = new Interval(now.toLocalDate().toDateTimeAtStartOfDay(),
                  now.toLocalDate().plusDays(1).toDateTimeAtStartOfDay());
          log.info("Providing random dates over {}.", interval);
          return new RandomMillisProvider(random, interval);
      }
    }
  }

  static class RandomMillisProvider implements MillisProvider {
    private final Random random;
    private final Interval interval;
    RandomMillisProvider(Random random, Interval interval) {
      this.random = random;
      this.interval = interval;
    }

    @Override
    public long getMillis() {
      double r = random.nextDouble();
      long im = interval.getEndMillis() - interval.getStartMillis();
      long offset = (long) (r * (double) im);
      return interval.getStartMillis() + offset;
    }
  }
}
