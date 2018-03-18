package humanware.usuarios;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import humanware.Candidato;
import humanware.Habilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLMostrarCandidatoController implements Initializable
{

    //<editor-fold desc="Variables FXML" defaultstate="collapsed">
    @FXML
    private JFXTextField tfNombreCandidato;
    @FXML
    private JFXTextField tfTelefono;
    @FXML
    private JFXTextField tfEmail;
    @FXML
    private JFXTextField tfRetribucionMinima;
    @FXML
    private JFXRadioButton rbCompleta;
    @FXML
    private JFXRadioButton rbAmbas;
    @FXML
    private JFXRadioButton rbParcial;
    @FXML
    private JFXTextArea taTitulaciones;
    @FXML
    private JFXTextArea taHabilidades;
    //</editor-fold>
    private Candidato candidato;
    private AnchorPane mostrarPane;
    private double xOffset;
    private double yOffset;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    public void inicializarComponentes()
    {
        inicializarCandidato();
        inicializarMover();
    }
    private void inicializarMover()
    {
        mostrarPane.setOnMousePressed(event ->
        {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        mostrarPane.setOnMouseDragged(event ->
        {
            mostrarPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            mostrarPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }
    public static void mostrarCandidato(Candidato c){
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource("/humanware/usuarios/FXMLMostrarCandidato.fxml"));
        AnchorPane pane = null;
        try {
            pane = cargador.load();
        } catch (IOException ex) {
            System.err.println("Error de lectura o escritura abriendo mostrarCandidato");
        }
        FXMLMostrarCandidatoController controlador = cargador.getController();
        controlador.setCandidato(c);
        controlador.mostrarPane = pane;
        controlador.inicializarComponentes();
        Stage st = new Stage(StageStyle.UNDECORATED);
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Mostrar candidato: " + c.getNombre());
        st.setScene(new Scene(controlador.mostrarPane));
        st.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        st.setResizable(false);
        st.showAndWait();
    }

    public void setCandidato(Candidato c) {
        this.candidato = c;
    }

    private void inicializarCandidato() {
        tfNombreCandidato.setText(candidato.getNombre());
        tfEmail.setText(candidato.getEmail());
        tfRetribucionMinima.setText(Double.toString(candidato.getRetribucion()) + " COP");
        switch (candidato.getTipoJornada()) {
            case AMBAS:
                rbAmbas.setSelected(true);
                break;
            case COMPLETA:
                rbCompleta.setSelected(true);
                break;
            case PARCIAL:
                rbParcial.setSelected(true);
        }
        for (Habilidad h : candidato.getHabilidades()) {
            taHabilidades.setText(taHabilidades.getText() + h.habilidad + "/" + h.nivel + "\n");
        }
        for (String s : candidato.getTitulaciones()) {
            taTitulaciones.setText(taTitulaciones.getText() + s + "\n");
        }
    }

    public void cerrar() {
        rbAmbas.getScene().getWindow().hide();
    }
}
