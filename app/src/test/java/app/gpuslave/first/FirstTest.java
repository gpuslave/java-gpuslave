package app.gpuslave.first;

import org.junit.Test;
import static org.junit.Assert.*;

public class FirstTest {
  @Test
  public void testMain() {
    First classUnderTest = new First();
    assertNotNull("thisisatest", classUnderTest.Hello());
  }

}
