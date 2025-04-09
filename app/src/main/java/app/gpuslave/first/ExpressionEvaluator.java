package app.gpuslave.first;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class ExpressionEvaluator {

  private Map<String, Double> variables;

  private Scanner scanner;

  public ExpressionEvaluator() {
    variables = new HashMap<>();
    scanner = new Scanner(System.in);
  }

  public void close() {
    if (scanner != null) {
      scanner.close();
    }
  }

  public double evaluate(String expression) {
    try {
      expression = expression.replaceAll("\\s+", ""); // remove spaces

      collectVariables(expression);

      for (String key : variables.keySet()) {
        System.out.println(key);
      }

      System.exit(0); // TEST -----------------------

      if (!variables.isEmpty()) {
        promptForVariables();
        expression = replaceVariables(expression);
      }

      if (!checkBrackets(expression)) {
        throw new IllegalArgumentException("Некорректное выражение," +
            " ошибка в расстановке скобок");
      }

      return evaluateExpression(expression);

    } catch (Exception e) {
      System.err.println("Ошибка: " + e.getMessage());
      throw new IllegalArgumentException("Некорректное выражение");
    }
  }

  private void collectVariables(String expression) {
    StringBuilder varName = new StringBuilder();

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);

      if (Character.isLetter(c)) {
        varName.append(c);
      } else if (varName.length() > 0) {
        variables.put(varName.toString(), null);
        varName = new StringBuilder();
      }
    }

    // edge case when varaible is at the end
    if (varName.length() > 0) {
      variables.put(varName.toString(), null);
    }
  }

}
