Currently, we use a naive way to solve assignment 2a. Hopefully, we'll find a better way.
1. Indexing: indexing documents. Each document store the year and all titles in that year.
2. Searching: to get common topic for a year, retrieve the respective document. Get the term vector of that document titles. Retrieve the 10 most common terms.

Usage:
1. To build the index, run the A2aDbplXmlParser.java.
2. To search, run A2aSearchCommonTopic.java