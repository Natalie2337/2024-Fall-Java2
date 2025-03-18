module com.example.spaceinvadergame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.spaceinvadergame to javafx.fxml;
    exports com.example.spaceinvadergame;
}