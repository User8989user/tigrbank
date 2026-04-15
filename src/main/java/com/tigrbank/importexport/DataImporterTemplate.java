package com.tigrbank.importexport;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import java.io.IOException;
import java.util.List;

public abstract class DataImporterTemplate implements DataImporter {

    @Override
    public final ImportResult importData(String basePath) throws IOException {
        // 1. Чтение сырых данных
        RawData rawData = readRawData(basePath);
        // 2. Парсинг сырых данных в структурированные
        ParsedData parsedData = parseRawData(rawData);
        // 3. Валидация (может быть переопределена)
        validate(parsedData);
        // 4. Преобразование в доменные объекты
        return convertToDomain(parsedData);
    }

    protected abstract RawData readRawData(String basePath) throws IOException;

    protected abstract ParsedData parseRawData(RawData rawData);

    protected void validate(ParsedData parsedData) {
        // Базовая валидация (например, проверка на null)
        if (parsedData == null) throw new IllegalArgumentException("Parsed data is null");
    }

    protected abstract ImportResult convertToDomain(ParsedData parsedData);
}