module ru.llm.pivoadm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens ru.llm.pivoadm to javafx.fxml;
    exports ru.llm.pivoadm;
}