module com.example.whackamole {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.whackamole to javafx.fxml;
    exports com.example.whackamole;
}