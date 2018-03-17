package humanware;

import javafx.application.Application;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HumanWare extends Application
{
    private double xOffset;
    private double yOffset;
    
    @Override
    public void start(Stage stage)
    {
        inicializarComponentes(stage);
    }
    
    public void inicializarComponentes(Stage stage)
    {
        try
        {
            AnchorPane pane = FXMLLoader.load(humanware.HumanWare.class.getResource("/humanware/login/FXMLLogin.fxml"));
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("HUMANWARE: Login");
            stage.centerOnScreen();
            stage.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
            stage.show();
        } catch (IOException e)
        {
            System.err.println("Error de lectura o escritura en clase principal");
        }
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}