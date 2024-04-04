package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

public class CsvProcessor implements CsvProcessorAPI {
    Table table;
    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        try (var bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\\Q" + delimiter + "\\E");
                table.addData(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        try (var bufferedWriter = new BufferedWriter(writer)) {
            TablePrinter tablePrinter = new MarkdownTablePrinter();
            Collection<String> rows = tablePrinter.printTable(table, alignments);

            int id = 1;
            for (String row : rows) {
                if (id == rows.size()) {
                    row = row.replace("\n", "").replace("\r", "");
                    bufferedWriter.write(row);
                    bufferedWriter.flush();
                    break;
                }
                bufferedWriter.write(row);
                bufferedWriter.write(System.lineSeparator());
                bufferedWriter.flush();
                id++;
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }
}
