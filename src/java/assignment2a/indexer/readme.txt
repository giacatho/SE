Currently, the solution is naive. We need a better solution.
1. Indexing: indexing documents. Each document store the year and all titles in that year.
2. Searching most common: to get common topic for a year, retrieve the respective document. Get the term vector of that document titles. Retrieve the 10 most common terms.

Usage:
1. To build the index, run the A2aXmlParser.java.
2. To search most common, run A2aCommonTopicSeacher.java
