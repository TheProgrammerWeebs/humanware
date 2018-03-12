package humanware.utilidades;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import humanware.login.ControladorUsuario;
import humanware.login.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class FXMLConfiguracionController implements Initializable, ControladorUsuario
{
    @FXML private JFXButton btEditar;
    @FXML private JFXButton btCambiar;
    @FXML private JFXTextField tfUsuario;
    @FXML private JFXPasswordField pfActual;
    @FXML private JFXPasswordField pfNueva;
    @FXML private JFXPasswordField pfNuevaConfirmar;
    @FXML private Label lbConfiguracion;
    @FXML private Label lbError;
    @FXML private Label lbErrorUsuario;
    @FXML AnchorPane configuracionPane;
    private Usuario usuario;
    private double xOffset;
    private double yOffset;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        configuracionPane.setOnMousePressed(event ->
        {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        configuracionPane.setOnMouseDragged(event ->
        {
            configuracionPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            configuracionPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });   
    }
    public void editarNombre()
    {
        lbErrorUsuario.setVisible(false);
        if (btEditar.getText().equals("EDITAR"))
        {
            btEditar.setText("ACEPTAR");
            tfUsuario.setEditable(true);
        }
        else if (btEditar.getText().equals("ACEPTAR"))
        {
            if (Utilidades.quitarEspacios(tfUsuario.getText()).equals(""))
            {
                lbErrorUsuario.setText("El nombre de usuario no puede estar vacío o ser solo espacios");
                lbErrorUsuario.setVisible(true);
            }else
            {
                usuario.setNombre(tfUsuario.getText());
                tfUsuario.setEditable(false);
                btEditar.setText("EDITAR");
            }
        }
    }
    
    public void cambiarContrasenia()
    {
        lbError.setVisible(false);

        if (btCambiar.getText().equals("CAMBIAR"))
        {
            btCambiar.setText("ACEPTAR");
            pfActual.setText("");
            pfActual.setEditable(true);            
            pfNueva.setEditable(true);
            pfNuevaConfirmar.setEditable(true);
        }else if (btCambiar.getText().equals("ACEPTAR"))
        {
            if (!pfActual.getText().equals(usuario.getContrasenia())){
                lbError.setText("La contraseña actual no es correcta");
                lbError.setVisible(true);
            }
            else if (Utilidades.quitarEspacios(pfNueva.getText()).equals("")) 
            {
                lbError.setText("La contraseña nueva no puede estar vacía o contener solo espacios");
                lbError.setVisible(true);
            }
            else if (!pfNueva.getText().equals(pfNuevaConfirmar.getText()))
            {
                lbError.setText("Las contraseñas no coinciden");
                lbError.setVisible(true);
            } 
            else
            {
                usuario.setContrasenia(pfNueva.getText());
                btEditar.setText("CAMBIAR");
                pfActual.setEditable(false);
                pfNueva.setEditable(false);
                pfNuevaConfirmar.setEditable(false);
            }
        }
    }
    @Override
    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
        tfUsuario.setText(usuario.getNombre());
        pfActual.setText(usuario.getContrasenia());
        lbConfiguracion.setText("Configuración: " + usuario.getNombre());
    }
    
    public void cerrar()
    {
        tfUsuario.getScene().getWindow().hide();
    }
    
}
