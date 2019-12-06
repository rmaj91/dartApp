module dartAppv2 {
    opens com.rmaj91.controller to javafx.fxml;
    exports com.rmaj91;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
}