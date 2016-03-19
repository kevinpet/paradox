package net.kdpeterson.paradox;

import static org.junit.Assert.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

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

}
