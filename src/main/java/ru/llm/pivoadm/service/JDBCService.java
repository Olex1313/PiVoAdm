package ru.llm.pivoadm.service;

import com.google.inject.Inject;
import ru.llm.pivoadm.utils.ConnectionUtil;

public class JDBCService {
    public ConnectionUtil connectionUtil;

    @Inject
    public JDBCService(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }
}
