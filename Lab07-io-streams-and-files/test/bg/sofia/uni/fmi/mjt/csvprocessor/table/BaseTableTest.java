package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseTableTest {
    Table baseTable = new BaseTable();
    @Test
    void testAddDataWithNull() {
        assertThrows(IllegalArgumentException.class, () -> baseTable.addData(null),
            "IllegalArgumentException expected to be thrown when trying to add null data!");
    }

    @Test
    void testAddDataOnlyHeaders() {
        String[] headers = {"h1", "h2", "h3"};
        assertDoesNotThrow(() -> baseTable.addData(headers));
    }

    @Test
    void testAddDataWithMoreOrLessThanDataInTable() {
        String[] headers = {"h1", "h2", "h3"};
        assertDoesNotThrow(() -> baseTable.addData(headers));
        String[] lessColumns = {"c1", "c2"};
        assertThrows(CsvDataNotCorrectException.class, () -> baseTable.addData(lessColumns));
    }

    @Test
    void testAddData() {
        String[] headers = {"h1", "h2", "h3"};
        assertDoesNotThrow(() -> baseTable.addData(headers));
        String[] row = {"c1", "c2", "c3"};
        assertDoesNotThrow(() -> baseTable.addData(row));
    }

    @Test
    void testAddDataDuplicated() {
        String[] headers = {"h1", "h1", "h3"};
        assertThrows(CsvDataNotCorrectException.class, () -> baseTable.addData(headers));
    }

    @Test
    void testGetColumnNamesNoNames() {
        assertDoesNotThrow(() -> baseTable.getColumnNames());
    }

    @Test
    void testGetColumnNames() {
        String[] headers = {"h1", "h2", "h3"};
        assertDoesNotThrow(() -> baseTable.addData(headers));
        assertDoesNotThrow(() -> baseTable.getColumnNames());
    }

    @Test
    void testGetColumnDataWithNull() {
        assertThrows(IllegalArgumentException.class, () -> baseTable.getColumnData(null));
    }

    @Test
    void testGetColumnDataWithBlank() {
        assertThrows(IllegalArgumentException.class, () -> baseTable.getColumnData("   "));
    }

    @Test
    void testGetColumnDataWithNonExistingColumn() {
        String[] headers = {"h1", "h2", "h3"};
        assertDoesNotThrow(() -> baseTable.addData(headers));
        String nonExistingHeader = "h4";
        assertThrows(IllegalArgumentException.class,() -> baseTable.getColumnData(nonExistingHeader));
    }

    @Test
    void testGetColumnData() {
        String[] headers = {"h1", "h2", "h3"};
        assertDoesNotThrow(() -> baseTable.addData(headers));
        String[] row1 = {"c1", "c2", "c3"};
        assertDoesNotThrow(() -> baseTable.addData(row1));
        String[] row2 = {"c11", "c22", "c33"};
        assertDoesNotThrow(() -> baseTable.addData(row2));
        String[] expectedString = {"c1", "c11"};
        Collection<String> expected = new LinkedHashSet<>(List.of(expectedString));
        assertIterableEquals(expected, baseTable.getColumnData("h1"));
    }

    @Test
    void testGetRowsCount() {
        assertDoesNotThrow(() -> baseTable.getRowsCount());
    }
}
