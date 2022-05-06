package ru.llm.pivoadm.service;


import com.google.inject.Inject;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetadataService {

    private JDBCService jdbcService;
    private DatabaseMetaData databaseMetaData;

    @Inject
    public MetadataService(JDBCService jdbcService) {
        this.jdbcService = jdbcService;
    }

    public List<String> getTables() {
        List<String> tables = new ArrayList<>();
        try {
            databaseMetaData = jdbcService.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    public List<String> getColumnsByTable(String tableName) {
        List<String> columnNames = new ArrayList<>();
        try {
            databaseMetaData = jdbcService.getMetaData();
            ResultSet columns = databaseMetaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columnNames;
    }
}
