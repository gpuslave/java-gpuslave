package app.gpuslave.first;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ExpressionEvaluatorTest {

  private ExpressionEvaluator evaluator;
  private final InputStream originalInput = System.in;

  @Before
  public void setUp() {
    evaluator = new ExpressionEvaluator();
  }

  @After
  public void tearDown() {
    evaluator.close();
    System.setIn(originalInput);
  }

  @Test
  public void testSimpleExpression() {
    assertEquals(5.0, evaluator.evaluate("2 + 3"), 0.001);
    assertEquals(10.0, evaluator.evaluate("5 * 2"), 0.001);
    assertEquals(2.5, evaluator.evaluate("5 / 2"), 0.001);
    assertEquals(3.0, evaluator.evaluate("5 - 2"), 0.001);
  }

  @Test
  public void testParenthesizedExpression() {
    assertEquals(14.0, evaluator.evaluate("2 * (5 + 2)"), 0.001);
    assertEquals(15.0, evaluator.evaluate("(2 + 3) * (4 - 1)"), 0.001);
    assertEquals(7.0, evaluator.evaluate("(10 + 4) / 2"), 0.001);
  }

  @Test
  public void testComplexExpression() {
    assertEquals(26.0, evaluator.evaluate("2 * 3 + 4 * 5"), 0.001);
    assertEquals(23.0, evaluator.evaluate("3 + 4 * 5"), 0.001);
    assertEquals(35.0, evaluator.evaluate("(3 + 4) * 5"), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidBrackets() {
    evaluator.evaluate("(2 + 3");
  }

  @Test
  public void testExpressionWithVariables() {
    // user input for variables
    String input = "10\n20\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ExpressionEvaluator newEvaluator = new ExpressionEvaluator();
    try {
      assertEquals(42.0, newEvaluator.evaluate("x + y + 12"), 0.001);
    } finally {
      newEvaluator.close();
    }
  }
}