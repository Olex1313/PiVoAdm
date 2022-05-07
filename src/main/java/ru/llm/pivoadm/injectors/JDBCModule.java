package ru.llm.pivoadm.injectors;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.llm.pivoadm.service.JDBCService;
import ru.llm.pivoadm.service.MetadataService;
import ru.llm.pivoadm.utils.ConnectionUtil;

public class JDBCModule extends AbstractModule {

    @Provides
    ConnectionUtil provideConnectionManager() {
        return new ConnectionUtil();
    }
    @Provides
    MetadataService provideMetadataService(ConnectionUtil connectionUtil) {
        return new MetadataService(connectionUtil);
    }

    @Provides
    JDBCService provideJDBCModule(ConnectionUtil connectionUtil) {
        return new JDBCService(connectionUtil);
    }

}

