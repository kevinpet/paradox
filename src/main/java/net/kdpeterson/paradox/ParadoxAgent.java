package net.kdpeterson.paradox;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.convert.DurationConverter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
      default:
        log.warn("Unknown time setting: " + agentArgs);
        break;
    }
  }
}
