package com.tigrbank.importexport;

import java.io.IOException;

public interface DataImporter {
    ImportResult importData(String basePath) throws IOException;
}