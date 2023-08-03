module com.example.transformerconverter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.alura.transformerconverter to javafx.fxml;
    exports com.alura.transformerconverter;
}