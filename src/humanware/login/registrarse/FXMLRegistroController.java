package humanware.login.registrarse;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import humanware.login.TipoUsuario;
import humanware.login.Usuario;
import humanware.utilidades.Utilidades;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class FXMLRegistroController implements Initializable
{

    @FXML private JFXButton btRegistrarse;
    @FXML private JFXTextField tfUsuario;
    @FXML private JFXPasswordField pfContrasenia;
    @FXML private JFXPasswordField pfConfirmar;
    @FXML private JFXRadioButton rbUsuario;
    @FXML private JFXRadioButton rbEvaluador;
    @FXML private Label lbErrorUsuario;
    @FXML private Label lbErrorContrasenia;
    @FXML private AnchorPane anchorPane;
    private double xOffset;
    private double yOffset;
    private final ToggleGroup grupo = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        rbUsuario.setToggleGroup(grupo);
        rbEvaluador.setToggleGroup(grupo);
        anchorPane.setOnMousePressed(event ->
        {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        anchorPane.setOnMouseDragged(event ->
        {
            anchorPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            anchorPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }
    public void registrarse()
    {
        lbErrorUsuario.setVisible(false);
        lbErrorContrasenia.setVisible(false);
        String usuario = tfUsuario.getText();
        String contrasenia = pfContrasenia.getText();
        String confirmar = pfConfirmar.getText();
        if (Utilidades.quitarEspacios(usuario).equals(""))
        {
            lbErrorUsuario.setText("El nombre de usuario no puede estar vacío");
            lbErrorUsuario.setVisible(true);
        } else
        {
            Usuario nuevoUsuario = new Usuario(usuario);
            if (nuevoUsuario.nombreExiste())
            {
                lbErrorUsuario.setText("Este nombre de usuario ya existe");
                lbErrorUsuario.setVisible(true);
            }
            else if (Utilidades.quitarEspacios(contrasenia).equals(""))
            {
                lbErrorContrasenia.setText("La contraseña no puede estar vacía o contener solo espacios");
                lbErrorContrasenia.setVisible(true);
            } else if (!contrasenia.equals(confirmar))
            {
                lbErrorContrasenia.setText("Las contraseñas no coinciden");
                lbErrorContrasenia.setVisible(true);
            } else if (!(rbUsuario.isSelected() || rbEvaluador.isSelected()))
            {
                lbErrorContrasenia.setText("Debe seleccionar un tipo de usuario");
                lbErrorContrasenia.setVisible(true);
            } else
            {
                TipoUsuario tipo = rbUsuario.isSelected() ? TipoUsuario.USUARIO : TipoUsuario.EVALUADOR;
                nuevoUsuario.setContrasenia(contrasenia);
                nuevoUsuario.setTipo(tipo);
                nuevoUsuario.agregarBaseDatos();
                anchorPane.getScene().getWindow().hide();
            }
        }
    }
    public void cerrar()
    {
        this.btRegistrarse.getScene().getWindow().hide();
    }
}
