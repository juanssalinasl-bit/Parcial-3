package co.edu.uniquindio.poo.parcia3.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.parcia3.controller.PanaderiaController;

public class App extends Application {
    @Override public void start(Stage stage) throws Exception {
        PanaderiaController.inicializarDatos();
        FXMLLoader loader=new FXMLLoader(App.class.getResource("/co/edu/uniquindio/poo/parcia3/view/panaderia-view.fxml"));
        Scene scene=new Scene(loader.load(),1180,900);
        scene.getStylesheets().add(App.class.getResource("/co/edu/uniquindio/poo/parcia3/css/estilos.css").toExternalForm());
        stage.setTitle("Panadería El Trigal");
        stage.setMinWidth(1000); stage.setMinHeight(750);
        stage.setScene(scene); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
