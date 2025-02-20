package app.gpuslave.first;

import org.junit.Test;
import static org.junit.Assert.*;

public class ContainerTest {

  @Test
  public void testIntegerContainer() {
    Container<Integer> container = new Container<>();
    container.add(1);
    container.add(2);
    container.add(3);

    assertEquals(3, container.size());
    assertEquals(Integer.valueOf(1), container.get(0));
    assertEquals(Integer.valueOf(2), container.get(1));
    assertEquals(Integer.valueOf(3), container.get(2));
  }

  @Test
  public void testStringContainer() {
    Container<String> container = new Container<>();
    container.add("Hello");
    container.add("World");

    assertEquals(2, container.size());
    assertEquals("Hello", container.get(0));
    assertEquals("World", container.get(1));
  }

  @Test
  public void testRemove() {
    Container<Double> container = new Container<>();
    container.add(1.1);
    container.add(2.2);
    container.add(3.3);

    container.remove(1);
    assertEquals(2, container.size());
    assertEquals(Double.valueOf(1.1), container.get(0));
    assertEquals(Double.valueOf(3.3), container.get(1));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetInvalidIndex() {
    Container<String> container = new Container<>();
    container.get(0);
  }

  @Test
  public void testGrow() {
    Container<Integer> container = new Container<>();
    for (int i = 0; i < 15; i++) {
      container.add(i);
    }
    assertEquals(15, container.size());
    for (int i = 0; i < 15; i++) {
      assertEquals(Integer.valueOf(i), container.get(i));
    }
  }
}