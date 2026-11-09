module co.edu.uniquindio.poo.parcia3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    exports co.edu.uniquindio.poo.parcia3.app;
    opens co.edu.uniquindio.poo.parcia3.viewController to javafx.fxml;

    exports co.edu.uniquindio.poo.parcia3.model;
    opens co.edu.uniquindio.poo.parcia3.model;
}