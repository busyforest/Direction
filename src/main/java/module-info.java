module src.direction {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens src.direction to javafx.fxml;
    exports src.direction;
}