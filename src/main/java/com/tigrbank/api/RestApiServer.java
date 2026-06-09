package com.tigrbank.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.facade.AccountFacade;
import com.tigrbank.facade.AnalyticsFacade;
import com.tigrbank.facade.CategoryFacade;
import com.tigrbank.facade.OperationFacade;
import spark.Spark;

import java.time.LocalDate;
import java.util.Map;

import static spark.Spark.*;

public class RestApiServer {
    private final AccountFacade accountFacade;
    private final CategoryFacade categoryFacade;
    private final OperationFacade operationFacade;
    private final AnalyticsFacade analyticsFacade;
    private final ObjectMapper mapper;

    public RestApiServer(AccountFacade accountFacade, CategoryFacade categoryFacade,
                         OperationFacade operationFacade, AnalyticsFacade analyticsFacade) {
        this.accountFacade = accountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
        this.analyticsFacade = analyticsFacade;
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public void start(int port) {
        port(port);
        // Настройка CORS (для удобства)
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            response.header("Access-Control-Allow-Headers", "Content-Type");
        });
        options("/*", (request, response) -> "OK");

        // Эндпоинты
        post("/api/accounts", (req, res) -> {
            Map<String, Object> body = mapper.readValue(req.body(), Map.class);
            String name = (String) body.get("name");
            double initialBalance = ((Number) body.get("initialBalance")).doubleValue();
            BankAccount account = accountFacade.createAccount(name, initialBalance);
            res.status(201);
            return mapper.writeValueAsString(account);
        });

        get("/api/accounts", (req, res) -> mapper.writeValueAsString(accountFacade.getAllAccounts()));

        post("/api/categories", (req, res) -> {
            Map<String, Object> body = mapper.readValue(req.body(), Map.class);
            Type type = Type.valueOf((String) body.get("type"));
            String name = (String) body.get("name");
            Category category = categoryFacade.createCategory(type, name);
            res.status(201);
            return mapper.writeValueAsString(category);
        });

        get("/api/categories", (req, res) -> mapper.writeValueAsString(categoryFacade.getAllCategories()));

        post("/api/operations", (req, res) -> {
            Map<String, Object> body = mapper.readValue(req.body(), Map.class);
            Type type = Type.valueOf((String) body.get("type"));
            Long accountId = ((Number) body.get("bankAccountId")).longValue();
            double amount = ((Number) body.get("amount")).doubleValue();
            LocalDate date = LocalDate.parse((String) body.get("date"));
            String description = (String) body.get("description");
            Long categoryId = ((Number) body.get("categoryId")).longValue();
            Operation op = operationFacade.createOperation(type, accountId, amount, date, description, categoryId);
            res.status(201);
            return mapper.writeValueAsString(op);
        });

        get("/api/operations", (req, res) -> mapper.writeValueAsString(operationFacade.getAllOperations()));

        delete("/api/operations/:id", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            operationFacade.deleteOperation(id);
            res.status(204);
            return "";
        });

        get("/api/analytics", (req, res) -> {
            LocalDate start = LocalDate.parse(req.queryParams("start"));
            LocalDate end = LocalDate.parse(req.queryParams("end"));
            double diff = analyticsFacade.getIncomeExpenseDifference(start, end);
            Map<String, Double> result = Map.of("difference", diff);
            return mapper.writeValueAsString(result);
        });

        // Обработчик ошибок
        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.body("Error: " + e.getMessage());
            e.printStackTrace();
        });
    }

    public void stop() {
        Spark.stop();
    }
}