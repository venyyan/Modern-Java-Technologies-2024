package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvProcessorTest {

    @Mock
    private Table tableMock;

    @InjectMocks
    private CsvProcessor csvProcessor;

    @Test
    void testReadCsv() {
        Reader reader = new StringReader("h1,h2,h3");
        assertDoesNotThrow(() -> csvProcessor.readCsv(reader, ","));
    }

    @Test
    void testWriteTable() {
        TablePrinter printer = new MarkdownTablePrinter();
        when(tableMock.getColumnNames()).thenReturn(List.of("column1", "column2", "c"));
        when(tableMock.getColumnData("column1")).thenReturn(List.of("row1", "row2"));
        when(tableMock.getColumnData("column2")).thenReturn(List.of("row11", "row22"));
        when(tableMock.getColumnData("c")).thenReturn(List.of("ro", "r"));
        when(tableMock.getRowsCount()).thenReturn(3);
        Writer writer = new StringWriter();
        ColumnAlignment[] alignments = {ColumnAlignment.RIGHT};
        Collection<String> rows = printer.printTable(tableMock, alignments);
        assertDoesNotThrow(() -> csvProcessor.writeTable(writer, alignments));
        String expectedOutput = "| column1 | column2 | c   |\r\n" +
            "| ------: | ------- | --- |\r\n" +
            "| row1    | row11   | ro  |\r\n" +
            "| row2    | row22   | r   |";
        assertEquals(expectedOutput, writer.toString());
    }
}
