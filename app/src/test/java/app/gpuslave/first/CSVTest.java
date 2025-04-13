package app.gpuslave.first;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Test class for the CSV parser functionality.
 * Tests reading and processing data from the CSV file.
 */
public class CSVTest {

  private final String csvFilePath = "/home/gpuslave/pet/java-gpuslave/foreign_names.csv";

  /**
   * Test that the CSV file is successfully loaded and parsed.
   * Verifies that the list of people is not empty.
   */
  @Test
  public void testCSVLoad() {
    List<CSV.Person> people = CSV.parseCSV(csvFilePath, ';');
    assertNotNull("The list of people should not be null", people);
    assertFalse("The list of people should not be empty", people.isEmpty());
  }

  /**
   * Test that the person data is correctly parsed from the CSV file.
   * Checks the first entry in the loaded data against expected values.
   */
  @Test
  public void testPersonData() {
    List<CSV.Person> people = CSV.parseCSV(csvFilePath, ';');
    assertFalse("The list of people should not be empty", people.isEmpty());

    CSV.Person firstPerson = people.get(0);

    assertEquals("The first person ID should be 28281", 28281, firstPerson.getId());
    assertEquals("The first person name should be Aahan", "Aahan", firstPerson.getName());
    assertEquals("The first person gender should be M", 'M', firstPerson.getGender());
    assertEquals("The first person salary should be 4800", 4800, firstPerson.getSalary());

    CSV.Division division = firstPerson.getDivision();
    assertNotNull("Division should not be null", division);
    assertEquals("Division title should be I", "I", division.getTitle());
  }

  /**
   * Test that the division data is correctly parsed and divisions are uniquely
   * assigned to people.
   */
  @Test
  public void testDivisionAssignment() {
    List<CSV.Person> people = CSV.parseCSV(csvFilePath, ';');

    // Map to keep track of division objects by title
    Map<String, CSV.Division> divisions = new HashMap<>();

    // For each person, put their division in the map
    for (CSV.Person person : people) {
      CSV.Division division = person.getDivision();
      String title = division.getTitle();

      if (divisions.containsKey(title)) {
        // If we've seen this division title before, verify it's the same object
        // This tests the logic that reuses division objects
        assertSame("Division objects with the same title should be the same instance",
            divisions.get(title), division);
      } else {
        // If this is a new division title, add it to our map
        divisions.put(title, division);
      }
    }

    // Verify we have multiple divisions
    assertTrue("There should be multiple divisions", divisions.size() > 1);
  }

  /**
   * Test that the date parsing works correctly.
   */
  @Test
  public void testDateParsing() {
    List<CSV.Person> people = CSV.parseCSV(csvFilePath, ';');

    // Verify that dates are not null
    for (int i = 0; i < Math.min(10, people.size()); i++) {
      CSV.Person person = people.get(i);
      assertNotNull("Date of birth should not be null", person.getDateOfBirth());
    }
  }

  /**
   * Test statistics calculation for divisions.
   */
  @Test
  public void testDivisionStatistics() {
    List<CSV.Person> people = CSV.parseCSV(csvFilePath, ';');

    // Group people by division and count
    Map<String, Long> divisionCounts = people.stream()
        .collect(Collectors.groupingBy(
            person -> person.getDivision().getTitle(),
            Collectors.counting()));

    // Check that each division has at least one person
    for (Long count : divisionCounts.values()) {
      assertTrue("Each division should have at least one person", count > 0);
    }

    // Calculate average salary per division
    Map<String, Double> averageSalaries = people.stream()
        .collect(Collectors.groupingBy(
            person -> person.getDivision().getTitle(),
            Collectors.averagingInt(CSV.Person::getSalary)));

    // Check that average salaries make sense
    for (Double avgSalary : averageSalaries.values()) {
      assertTrue("Average salary should be positive", avgSalary > 0);
      assertTrue("Average salary should be reasonable", avgSalary < 100000);
    }
  }

  /**
   * Test the wrong separator scenario.
   * The test verifies that using the wrong separator results in an empty list rather than an exception.
   */
  @Test
  public void testWrongSeparator() {
    // Using comma instead of semicolon should result in empty or malformed data
    List<CSV.Person> people = CSV.parseCSV(csvFilePath, ',');
    // Either the list is empty or the first person has default/invalid values
    if (!people.isEmpty()) {
      // If not empty, the data should be malformed/incorrect
      CSV.Person firstPerson = people.get(0);
      // Check that at least one of the fields is incorrect
      boolean isMalformed = false;
      if (firstPerson.getName() == null || firstPerson.getName().isEmpty()) {
        isMalformed = true;
      }
      if (firstPerson.getSalary() <= 0) {
        isMalformed = true;
      }
      assertTrue("Data should be malformed when using wrong separator", isMalformed);
    }
  }
}