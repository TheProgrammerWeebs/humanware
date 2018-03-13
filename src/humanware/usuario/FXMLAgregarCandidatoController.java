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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    private JFXTextField tfNombreCandidato;
    @FXML
    private AnchorPane agregarCandidatoPane;
    //</editor-fold>
    private double xOffset;
    private double yOffset;
    private ToggleGroup grupo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarComponentes();
    }    
    public void inicializarComponentes()
    {
        inicializarMover();
        grupo = new ToggleGroup();
        rbAmbas.setToggleGroup(grupo);
        rbParcial.setToggleGroup(grupo);
        rbCompleta.setToggleGroup(grupo);
        
    }
    
    private void inicializarMover() {
        agregarCandidatoPane.setOnMousePressed(event
                -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        agregarCandidatoPane.setOnMouseDragged(event
                -> {
            agregarCandidatoPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            agregarCandidatoPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }
    
    public void agregarHabilidad()
    {
        //TODO: agregar habilidad
    }
        
    public void agregarTitulacion()
    {
       
    }
    
    public void agregarCandidato()
    {
        //TODO: agregar candidato
    }
}
