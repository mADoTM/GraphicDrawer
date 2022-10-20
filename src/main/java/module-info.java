module ru.vsu.cs.dolzhenko_m_s.graphicdrawer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ru.vsu.cs.dolzhenko_m_s.graphicdrawer to javafx.fxml;
    exports ru.vsu.cs.dolzhenko_m_s.graphicdrawer;
}