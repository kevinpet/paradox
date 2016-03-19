package net.kdpeterson.paradox;

import static org.junit.Assert.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.*;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class ParadoxAgentTest {

  @Test
  public void testStringParsing() {
    String dateString = "2016-01-01T12:00:00.000Z";
    DateTime parsed = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(dateString);
    DateTime expected = new DateTime(2016, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
    assertEquals(expected, parsed);
  }

  @Test
  public void testFixedTime() {
    ParadoxAgent.premain("fixed=2016-01-01T12:00:00.000Z");
    DateTime now = new DateTime(DateTimeZone.UTC);
    assertEquals(new DateTime(2016, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), now);
  }

  @Test
  public void testOffset() {
    ParadoxAgent.premain("fixed=2016-01-01T12:00:00.000Z");
    DateTime now = new DateTime(DateTimeZone.UTC);
    assertEquals(new DateTime(2016, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), now);
  }

}
