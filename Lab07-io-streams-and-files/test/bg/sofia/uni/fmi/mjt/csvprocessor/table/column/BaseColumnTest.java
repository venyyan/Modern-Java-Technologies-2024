package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseColumnTest {
    Column baseColumn = new BaseColumn();
    @Test
    void testAddDataWithNull() {
        assertThrows(IllegalArgumentException.class, () -> baseColumn.addData(null),
            "IllegalArgumentException expected to be thrown when trying to add null data!");
    }

    @Test
    void testAddDataWithBlank() {
        assertThrows(IllegalArgumentException.class, () -> baseColumn.addData("    "),
            "IllegalArgumentException expected to be thrown when trying to add blank data!");
    }

    @Test
    void testAddData() {
        assertDoesNotThrow(() -> baseColumn.addData("col1"));
    }

    @Test
    void testGetData() {
        Assertions.assertDoesNotThrow(() -> baseColumn.getData());
    }
}
