package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.Collection;
import java.util.LinkedHashSet;

import static java.lang.Math.max;

public class MarkdownTablePrinter implements TablePrinter {
    private static final int MIN_MAX_SIZE_CELL = 3;
    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        Collection<String> rows = new LinkedHashSet<>();
        int[] columnsMaxSize = columnsMaxSize(table);
        int columnsCount = getColumnsCount(table);

        printHeaders(table, columnsMaxSize, rows);
        printAlignmentRow(columnsMaxSize, columnsCount, rows, alignments);
        printRows(table, columnsMaxSize, rows);

        return rows;
    }

    private void printRows(Table table, int[] columnsMaxSize, Collection<String> rows) {
        for (int i = 1; i < table.getRowsCount(); i++) {
            StringBuilder newRow = new StringBuilder();
            int columnNumber = 0;
            for (String colName : table.getColumnNames()) {
                int currentMaxSize = columnsMaxSize[columnNumber++];
                int j = 1;
                for (String data : table.getColumnData(colName)) {
                    if (j == i) {
                        int spacesCount = max(0, currentMaxSize - data.length());
                        newRow.append("| ").append(data).append(" ".repeat(spacesCount + 1));
                        break;
                    }
                    j++;
                }
            }
            newRow.append("|");
            rows.add(newRow.toString());
        }
    }

    private void printHeaders(Table table, int[] columnsMaxSize, Collection<String> rows) {
        StringBuilder headersRow = new StringBuilder();

        int id = 0;
        for (String colName : table.getColumnNames()) {
            int currentMaxSize = columnsMaxSize[id++];
            int spacesCount = max(0, currentMaxSize - colName.length());
            headersRow.append("| ").append(colName).append(" ".repeat(spacesCount + 1));
        }
        headersRow.append("|");
        rows.add(headersRow.toString());
    }

    private void printAlignmentRow(int[] columnsMaxSize, int columnsCount, Collection<String> rows,
                                   ColumnAlignment... alignments) {
        StringBuilder alignmentsRow = new StringBuilder();
        int id1 = 0;
        for (ColumnAlignment alignment : alignments) {
            if (id1 < columnsCount) {
                int currentMaxSize = columnsMaxSize[id1++];
                switch (alignment) {
                    case LEFT -> alignmentsRow.append("| :").append("-".repeat(currentMaxSize - 1)).append(" ");
                    case CENTER -> alignmentsRow.append("| :").append("-".repeat(currentMaxSize - 2)).append(": ");
                    case RIGHT -> alignmentsRow.append("| ").append("-".repeat(currentMaxSize - 1)).append(": ");
                    case NOALIGNMENT -> alignmentsRow.append("| ").append("-".repeat(currentMaxSize)).append(" ");
                }
            } else {
                break;
            }
        }
        while (id1 < columnsCount) {
            int currentMaxSize = columnsMaxSize[id1++];
            alignmentsRow.append("| ").append("-".repeat(currentMaxSize)).append(" ");
        }
        alignmentsRow.append("|");
        rows.add(alignmentsRow.toString());

    }

    private int[] columnsMaxSize(Table table) {
        int columnsSize = 0;
        for (String column : table.getColumnNames()) {
            columnsSize++;
        }

        int[] columnsMaxSize = new int[columnsSize];
        int id = 0;
        for (String column : table.getColumnNames()) {
            if (getColumnMaxSize(table.getColumnData(column)) < MIN_MAX_SIZE_CELL) {
                columnsMaxSize[id++] = MIN_MAX_SIZE_CELL;
                continue;
            }
            columnsMaxSize[id++] = getColumnMaxSize(table.getColumnData(column));
        }
        columnsMaxSizeWithHeaders(table, columnsMaxSize);
        return columnsMaxSize;
    }

    private void columnsMaxSizeWithHeaders(Table table, int[] sizes) {
        int sizesId = 0;
        for (String header : table.getColumnNames()) {
            if (sizes[sizesId] < header.length()) {
                sizes[sizesId] = header.length();
            }
            sizesId++;
        }
    }

    private int getColumnMaxSize(Collection<String> column) {
        int maxSize =  Integer.MIN_VALUE;
        for (String data : column) {
            if (data.length() > maxSize) {
                maxSize = data.length();
            }
        }

        return maxSize;
    }

    private int getColumnsCount(Table table) {
        int columnsSize = 0;
        for (String column : table.getColumnNames()) {
            columnsSize++;
        }
        return columnsSize;
    }
}
