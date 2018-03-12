package humanware.login;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.*;
import humanware.utilidades.Utilidades;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javax.swing.JOptionPane;

public class FXMLoginController implements Initializable
{

    @FXML private JFXTextField tfUser;
    @FXML private JFXPasswordField pfPassword;
    @FXML private JFXButton btIngresar;
    @FXML private JFXCheckBox cbRemember;
    @FXML private Label lbIncorrecto;
    @FXML private AnchorPane loginPane;
    
    private double xOffset;
    private double yOffset;
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        cargarCookies();
        btIngresar.setDefaultButton(true);
        loginPane.setOnMousePressed(event ->
        {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        loginPane.setOnMouseDragged(event ->
        {
            loginPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            loginPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }

    public void cerrar()
    {
        System.exit(0);
    }
    public void ingresar()
    {
        if (tfUser.getText().equals("") && pfPassword.getText().equals(""))
        {
            lbIncorrecto.setText("Debe ingresar algo en los campos");
            lbIncorrecto.setVisible(true);
        } else
        {
            lbIncorrecto.setVisible(false);
            lbIncorrecto.setText("Usuario o contraseña incorrectos");
            Usuario usuario = new Usuario(tfUser.getText(), pfPassword.getText());            
            if (usuario.esValido())
            {
                String ruta = "";
                try
                {
                    actualizarCookies();
                    switch (usuario.getTipo())
                    {
                        case ADMINISTRADOR:
                            JOptionPane.showMessageDialog(null, "Aún no hemos implementado esa opción :) lanzaré una excepción");
                            break;
                        case USUARIO:
                            ruta = "/humanware/usuario/FXMLUsuario.fxml";
                            break;
                        case EVALUADOR:
                            JOptionPane.showMessageDialog(null, "Aún no hemos implementado esa opción :) lanzaré una excepción");
                            break;
                    }
                    abrirVentanaUsuario(ruta, usuario, true);
                    loginPane.getScene().getWindow().hide();
                } catch (IOException ex)
                {
                    System.err.println("Error de lectura o escritura en login");
                    ex.printStackTrace();
                }
            } else
            {
                lbIncorrecto.setVisible(true);
            }
        }
    }
    
    public void registrarse()
    {
        try
        {
            abrirVentana("/humanware/login/registrarse/FXMLRegistro.fxml", false);
        } catch (IOException ex)
        {
            System.err.println("Error de lectura o escritura intentando abrir registro");
        }
    }
    
    private void abrirVentanaUsuario(String ruta, Usuario usuario, boolean cerrarActual) throws IOException
    {
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource(ruta));
        AnchorPane usuarioPane = cargador.load();
        ControladorUsuario controlador = cargador.getController();
        controlador.setUsuario(usuario);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(usuarioPane));
        stage.show();
        stage.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        stage.centerOnScreen();
        if (cerrarActual) tfUser.getScene().getWindow().hide();
    }
    
    private void abrirVentana(String ruta, boolean cerrarActual) throws IOException
    {
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource(ruta));
        AnchorPane usuarioPane = cargador.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(usuarioPane));
        if (cerrarActual)
            tfUser.getScene().getWindow().hide();
        else stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        stage.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        stage.centerOnScreen();
        
    }
   
    private void actualizarCookies() throws IOException
    {
        String ruta = "archivos\\database\\cookies";
        PrintWriter pw;
        pw = Utilidades.openFileWrite(ruta);
        if (cbRemember.isSelected())
        {
            pw.println("true"); //en la primera línea se guarda el estado del checkbox
            pw.println(tfUser.getText()); //en la segunda el nombre de usuario
        } else
        {
            pw.println("false");
        }
        pw.close();
    }

    private void cargarCookies()
    {
        BufferedReader lector;
        try
        {
            String ruta = "archivos\\database\\cookies";
            lector = Utilidades.openFileRead(ruta);
            if (lector.readLine().equals("true")) //la primera línea dice si el checkbox recuérdame está activo
            {
                tfUser.setText(lector.readLine()); //la segunda línea tiene el nombre del usuario
                cbRemember.setSelected(true);
            }
        } catch (FileNotFoundException ex)
        {
            System.err.println("Archivo no encontrado");
        } catch (IOException ex)
        {
            System.err.println("Error de lectura o escritura");
        }
    }
}
