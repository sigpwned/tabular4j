# TABULAR4J [![tests](https://github.com/sigpwned/tabular4j/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/tabular4j/actions/workflows/tests.yml) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_tabular4j&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_tabular4j) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_tabular4j&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_tabular4j) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_tabular4j&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_tabular4j) [![Maven Central Version](https://badgen.net/maven/v/maven-central/com.sigpwned/tabular4j)](https://search.maven.org/artifact/com.sigpwned/tabular4j)

A framework for reading and writing tabular data using popular data formats, especially spreadsheets.

## Motivation

Tabular data is an important interface between humans and machine. However, supporting multiple spreadsheet formats transparently is complex. This library provides a framework of adapters and facades for reading and writing tabular data using popular spreadsheet data formats.

## Goals

* To support the straightforward reading and writing of tabular data in common file formats, especially spreadsheets.
* To support common value types for cells to streamline development and minimize errors.

## Non-Goals

* To support non-tabular layouts of data, e.g. merged cells in Excel.
* To expose all features for all data formats, e.g. charts in Excel. Outstanding specialized libraries already exist for the use and manipulation of specific data formats.
* To support all data formats that can represent tabular data. The library's primary goal is to support the data formats most commonly exchanged between humans and computers.

## Data Model

### Worksheets

A worksheet is a single spreadsheet containing one rectangular table of data. The first row of a worksheet is interpreted as column names (i.e., headers), and determines the width of the table of data. Each addditional row of a worksheet is interpreted as a value in each of the named columns. If a row's actual width is greater than the number of columns, then it is truncated before it is interpreted; if its actual width is less than the number of the columns, then it is right-padded with null-valued cells before it is interpreted.

    +---------+---------+---------+---------+---------+
    |  alpha  |  bravo  | charlie |  delta  |         | <-- Column Names/Headers. Width = 4.
    +---------+---------+---------+---------+---------+
    |    a    |    b    |    c    |    d    |         | <-- Width 4. No transformation.
    +---------+---------+---------+---------+---------+
    |    a    |    b    |    c    |         |         | <-- Width 3. Will be right-padded with null.
    +---------+---------+---------+---------+---------+
    |         |    b    |    c    |    d    |         | <-- Width 4. First cell is empty.
    +---------+---------+---------+---------+---------+
    |    a    |    b    |    c    |    d    |    e    | <-- Width 5. Will be truncated to width 4.
    +---------+---------+---------+---------+---------+
    |    a    |    b    |    c    |    d    |         | <-- Width 4. No transformation.
    +---------+---------+---------+---------+---------+
    
### Workbooks

A workbook is an ordered list of spreadsheets gathered into a single file. Each worksheet in a workbook has a unique name. One sheet in the workbook is "active". For a workbook created by a human, this is the worksheet that was last edited by the user and/or is shown when the file is opened; for a workbook created by the library, it is simply the worksheet with the active flag set.

## Code Examples

### Opening a File to Read

Users open an existing workbook file to read using a `ByteSource`. Note that the spreadsheet format (e.g., CSV, XLSX, etc.) is never given explicitly; rather, the framework detects the format of the spreadsheet and reacts accordingly.

    try (WorkbookReader workbook=SpreadsheetFactory.getInstance().readTabularWorkbook(ByteSource.ofFile(file))) {
        // Handle workbook here...
    }

### Reading a Worksheet

This approach opens a workbook file and processes its active worksheet only.

#### Manual Iteration 

First, the user can open it and then iterate over its rows manually:

    try (WorksheetReader worksheet=SpreadsheetFactory.getInstance().readTabularActiveWorksheet(source)) {
        for(TabularWorksheetRow row=worksheet.readRow();row!=null;row=worksheet.readRow()) {
            // Handle row here...
        }
    }
    
#### Automatic Iteration using Iterator

Alternatively, the user can use an `Iterator` directly. Note that any `IOException` exceptions are thrown as `UncheckedIOException` in this case.

    try (WorksheetReader worksheet=SpreadsheetFactory.getInstance().readTabularActiveWorksheet(source)) {
        for(TabularWorksheetRow row : worksheet) {
            // Handle row here...
        }
    }

#### Automatic Processing using Stream

The user an also use a Java 8 `Stream` to process rows:

    try (WorksheetReader worksheet=SpreadsheetFactory.getInstance().readTabularActiveWorksheet(source)) {
        worksheet.stream().forEach(c -> {
            // Handle row here...
        });
    }

#### Automatic Processing using Consumer

Finally, if the user doesn't need to control how rows are read and processed ("pull" style), then the user can also use a consumer that walks the sheet automatically ("push" style):

    Spreadsheets.processTabularWorksheet(source, new TabularWorksheetConsumer() {
        public default void beginTabularWorksheet(int sheetIndex, String sheetName, List<String> columnNames) {
            // Handle setup here...
        }

        public default void tabularRow(int rowIndex, List<TabularWorksheetCell> cells) {
            // Handle row here...
        }

        public default void endTabularWorksheet() {
            // Handle cleanup here...
        }
    });
    
### Reading a Workbook

This approach opens a workbook file and processes each of its worksheets in order. It shares much logic with the worksheet examples above.

#### By Index

First, the user can open the file and iterate over its sheets by index:

    try (WorkbookReader workbook=SpreadsheetFactory.getInstance().readTabularWorkbook(source)) {
        for(int i=0;i<workbook.getWorksheetCount();i++) {
            try (WorksheetReader worksheet=workbook.getWorksheet(i)) {
                // Handle worksheet here...
            }
        }
    }
    
#### By Name

Alternatively, the user can open the file and iterate over its sheets by name:

    try (WorkbookReader workbook=SpreadsheetFactory.getInstance().readTabularWorkbook(source)) {
        for(String worksheetName : workbook.getWorksheetNames()) {
            try (WorksheetReader worksheet=workbook.findWorksheetByName(worksheetName).get()) {
                // Handle worksheet here...
            }
        }
    }

### Opening a File to Write

Users open a workbook file to write using a `ByteSink`. The user gives the desired file format in this case.

    try (WorkbookWriter workbook=SpreadsheetFactory.getInstance().writeTabularWorkbook(ByteSink.ofFile(file))) {
        // Handle workbook here...
    }

### Writing a Worksheet

In this approach, the user simply opens writer, writes the rows, and closes the writer. This results in a workbook with one sheet.

    try (WorksheetWriter worksheet=SpreadsheetFactory.getInstance().writeTabularActiveWorksheet(sink, "csv")
            .writeHeaders("alpha", "bravo")) {
        worksheet.writeValuesRow("a", "b");
        worksheet.writeValuesRow("1", "2");
    }

### Writing a Workbook

In this approach, the user simply opens writer, writes the rows, and closes the writer. The user may then open and write additional sheets the same way.

    try (WorkbookWriter workbook=SpreadsheetFactory.getInstance().writeTabularWorkbook(sink, "csv")) {
        try (WorksheetWriter worksheet=workbook.getWorksheet("sheet1").writeHeaders("alpha", "bravo")) {
            // Write worksheet...
        }
        try (WorksheetWriter worksheet=workbook.getWorksheet("sheet2").writeHeaders("charlie", "delta")) {
            // Write worksheet...
        }
    }
    
## FAQ

### What file formats are supported?

The library currently supports the following file formats:

* `xlsx` -- Using the excellent [Apache POI](https://poi.apache.org/) library
* `xls` -- Also using the excellent [Apache POI](https://poi.apache.org/) library
* `csv` -- Using the [`csv4j`](https://github.com/sigpwned/csv4j)

### What file formats do you plan to support in the future?

If there is interest, I'd like to support formats like the following:

* `jsonl` -- One JSON object per line
* `orc` -- [Apache ORC](https://orc.apache.org/) Tabular data store for Hadoop
* `parquet` -- [Apache Parquet](https://parquet.apache.org/) column-oriented data file

### What value types does the library support?

Out of the box, the library supports the following data types:

* All Java primitives (`byte`, `short`, `int`, `long`, `boolean`, `float`, `double`, `char`)
* All Java boxed types (`Byte`, `Short`, `Integer`, `Long`, `Boolean`, `Float`, `Double`, `Character`)
* Java 8 Time types (`Instant`, `LocalDate`, `LocalTime`, `LocalDateTime`, `OffsetDateTime`, `ZonedDateTime`, `ZoneId`)
* Internet types (`URL`, `URI`, `InetAddress`)
* Various and sundry others (`UUID`, `BigDecimal`, `BigInteger`, `String`, `byte[]`, `Date`, `Calendar`)

### Can I add my own value types?

One goal of this library is to make it easier to add new data types for processing. For now, to add new types, look at the `CoreCsvValueMapperFactory` and `CoreExcelValueMapperFactory` classes. I hope to add an easier way to add types soon.