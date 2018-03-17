package humanware.utilidades;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ObtenerDatos
{

    public static String informacion;
    public static String mostrarVentana(String mensaje, String titulo) {
        Scene scene;
        VBox pane;
        JFXTextField tfInput = new JFXTextField();
        Stage stage = new Stage();
        Label msj = new Label(mensaje);
        JFXButton btAceptar = new JFXButton("ACEPTAR");
        stage.initModality(Modality.APPLICATION_MODAL);
        btAceptar.setDefaultButton(true);
        pane = new VBox(msj, tfInput, btAceptar);
        btAceptar.setOnAction(e -> {
            if (tfInput.getText() == null || Utilidades.quitarEspacios(tfInput.getText()).equals("")) {
                System.out.println(informacion==null);
                tfInput.setText("");
                tfInput.setPromptText("Debe ingresar un dato");
                tfInput.setVisible(true);
            } else {
                informacion = tfInput.getText();
                stage.hide();
            }
        });
        pane.setSpacing(10);
        pane.setAlignment(Pos.TOP_LEFT);
        pane.setPadding(new Insets(20, 20, 20, 20));
        stage.setOnCloseRequest(e -> {
            e.consume();
            tfInput.setText("");
            tfInput.setPromptText("Debe ingresar un dato");
            tfInput.setVisible(true);
        });
        stage.setScene(new Scene(pane, 370, 130));
        stage.setTitle(titulo);
        stage.showAndWait();
        return informacion;
    }
    public static String mostrarVentana(String mensaje) {
        return mostrarVentana(mensaje, "Ingrese la información");
    }
}
