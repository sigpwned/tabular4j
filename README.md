# TABULAR4J [![tests](https://github.com/sigpwned/tabular4j/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/tabular4j/actions/workflows/tests.yml) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_tabular4j&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_tabular4j) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_tabular4j&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_tabular4j) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_tabular4j&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_tabular4j) [![Maven Central Version](https://badgen.net/maven/v/maven-central/com.sigpwned/tabular4j)](https://search.maven.org/artifact/com.sigpwned/tabular4j)

A framework for reading and writing tabular data using popular data formats, especially spreadsheets.

## Motivation

Tabular data is an important interface between humans and machine. However, supporting multiple spreadsheet formats transparently is complex. This library provides a framework of adapters and facades for reading and writing tabular data using popular spreadsheet data formats.

## Goals

* To support the straightforward reading and writing of tabular data in common file formats, especially spreadsheets.
* To support common value types for cells to streamline development and minimize errors, and provide a method for adding new value types.

## Non-Goals

* To support non-tabular layouts of data, e.g. merged cells in Excel.
* To expose all features for all data formats, e.g. charts in Excel. Outstanding specialized libraries already exist for the use and manipulation of specific data formats.
* To support all data formats that can represent tabular data. The library's primary goal is to support the data formats most commonly exchanged between humans and computers.
