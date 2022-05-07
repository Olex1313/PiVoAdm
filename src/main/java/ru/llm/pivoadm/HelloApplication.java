package ru.llm.pivoadm;


import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.llm.pivoadm.injectors.JDBCModule;
import ru.llm.pivoadm.service.JDBCService;
import ru.llm.pivoadm.service.MetadataService;
import ru.llm.pivoadm.utils.Characteristic;


import java.io.IOException;

public class HelloApplication extends Application {

    public MetadataService metadataService;
    public JDBCService jdbcService;

    @Inject
    public HelloApplication(MetadataService metadataService, JDBCService jdbcService) {
        this.metadataService = metadataService;
        this.jdbcService = jdbcService;
    }


    @Override
    public void start(Stage stage) throws IOException {
        Injector injector = Guice.createInjector(new JDBCModule());
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args)  {
        Injector injector = Guice.createInjector(new JDBCModule());
        HelloApplication helloApplication = injector.getInstance(HelloApplication.class);
        helloApplication.metadataService.getСharacteristicByTableName("sportsmen", Characteristic.COLUMN_NAME).forEach(System.out::println);

//        launch();

    }
}