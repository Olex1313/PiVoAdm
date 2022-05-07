module ru.llm.pivoadm {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.guice;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens ru.llm.pivoadm to javafx.fxml;

    exports ru.llm.pivoadm.utils;
    exports ru.llm.pivoadm.service;
    exports ru.llm.pivoadm;
    exports ru.llm.pivoadm.injectors;
}