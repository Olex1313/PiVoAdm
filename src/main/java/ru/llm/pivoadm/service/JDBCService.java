package ru.llm.pivoadm.service;

import ru.llm.pivoadm.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCService {
    private final Connection connection = ConnectionManager.open();

    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }
}
