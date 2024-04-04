package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseTable implements Table {
    Collection<Column> columns;
    int rowsCount = 0;
    public BaseTable() {
        this.columns = new LinkedHashSet<>();
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("Data is null!");
        }

        if (columns.isEmpty()) {
            Set<String> duplications = new HashSet<>();
            for (String colData : data) {
                if (!duplications.add(colData)) {
                    throw new CsvDataNotCorrectException("Cannot add duplicated headers!");
                }
                Column column = new BaseColumn();
                column.addData(colData);
                columns.add(column);
            }
        } else {
            if (data.length != columns.size()) {
                throw new CsvDataNotCorrectException("The count of \n" +
                    " the provided data parts is less or more than the number of columns in the table!");
            }

            int colId = 0;
            for (Column column : columns) {
                column.addData(data[colId++]);
            }
        }
        rowsCount++;
    }

    @Override
    public Collection<String> getColumnNames() {
        Collection<String> columnNames = new LinkedHashSet<>();

        for (Column column : columns) {
            for (String s : column.getData()) {
                columnNames.add(s);
                break;
            }
        }

        return Collections.unmodifiableCollection(columnNames);
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isBlank()) {
            throw new IllegalArgumentException("Column is null or empty!");
        }
        boolean foundColName = false;
        Collection<String> columnData = new LinkedHashSet<>();
        for (Column col : columns) {
            for (String s : col.getData()) {
                if (s.equals(column)) {
                    foundColName = true;
                    continue;
                }

                if (foundColName) {
                    columnData.add(s);
                }
            }
            if (foundColName) {
                break;
            }
        }

        if (!foundColName) {
            throw new IllegalArgumentException("There is no corresponding column with " +
                "that name in the table!");
        } else {
            return Collections.unmodifiableCollection(columnData);
        }
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }
}