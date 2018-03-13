package humanware.usuario;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FXMLAgregarCandidatoController implements Initializable
{
    //<editor-fold defaultstate="collapsed" desc="JavaFx Variables">
    @FXML
    private JFXComboBox cbTitulos;
    @FXML
    private JFXComboBox cbHabilidadNivel;
    @FXML
    private JFXTextField tfHabilidad;
    @FXML
    private JFXTextArea taHabilidades;
    @FXML
    private JFXTextArea taTitulaciones;
    @FXML
    private JFXRadioButton rbParcial;
    @FXML
    private JFXRadioButton rbAmbas;
    @FXML
    private JFXRadioButton rbCompleta;
    @FXML
    private JFXTextField tfRetribucionMinima;
    //</editor-fold>
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}
