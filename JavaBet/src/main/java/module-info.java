module org.example.BotX {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens org.example.POOBet to javafx.fxml;
    exports org.example.POOBet;
}