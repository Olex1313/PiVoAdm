package ru.llm.pivoadm.service;


import com.google.inject.Inject;
import ru.llm.pivoadm.utils.Characteristic;
import ru.llm.pivoadm.utils.ConnectionUtil;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetadataService{

    private ConnectionUtil connectionUtil;

    @Inject
    public MetadataService(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    public List<String> getTables() {
        List<String> tables = new ArrayList<>();
        try {
            DatabaseMetaData databaseMetaData = connectionUtil.getDataBaseMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    public List<String> getСharacteristicByTableName(String tableName, Characteristic characteristic) {
        List<String> columnNames = new ArrayList<>();
        try {
            DatabaseMetaData databaseMetaData = connectionUtil.getDataBaseMetaData();
            ResultSet columns = databaseMetaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                columnNames.add(columns.getString(characteristic.toString()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columnNames;
    }

}
