package bg.sofia.uni.fmi.mjt.csvprocessor.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class MarkdownTablePrinterTest {
    static Table table = new BaseTable();

    @BeforeAll
    static void createTable() {
        String[] headers = {"column1", "column2", "c"};
        String[] row1 = {"row1", "row11", "ro"};
        String[] row2 = {"row2", "row22", "r"};
        String[] row3 = {"r", "r", "ra"};
        table.addData(headers);
        table.addData(row1);
        table.addData(row2);
        table.addData(row3);
    }
    @Test
    void testPrintTable() {
        MarkdownTablePrinter markdownTablePrinter = new MarkdownTablePrinter();
        String[] expectedTable = {"| column1 | column2 | c   |",
            "| :------ | :-----: | --- |",
            "| row1    | row11   | ro  |",
            "| row2    | row22   | r   |" ,
            "| r       | r       | ra  |"};
        ColumnAlignment[] al = {ColumnAlignment.LEFT, ColumnAlignment.CENTER};
        Collection<String> rows = new LinkedHashSet<>(List.of(expectedTable));
        assertIterableEquals(rows, markdownTablePrinter.printTable(table, al));
    }
}
