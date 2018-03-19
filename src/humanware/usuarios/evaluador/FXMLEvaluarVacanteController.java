package humanware.usuarios.evaluador;

import humanware.Candidato;
import humanware.utilidades.ListaEnlazada;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class FXMLEvaluarVacanteController implements Initializable
{

    @FXML
    private AnchorPane paneEvaluar;
    @FXML
    private TableView<Candidato> tbCandidatos;
    @FXML
    private TableColumn<Candidato, String> tbcNombre;
    @FXML
    private TableColumn<Candidato, Integer> tbcPuntuacion;
    
    private ListaEnlazada<Candidato> aptos;
    private double xOffset;
    private double yOffset;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aptos = new ListaEnlazada<>();
    }

    public void setAptos(ListaEnlazada<Candidato> aptos) {
        this.aptos = aptos;
        inicializarComponentes();
    }

    public void cerrar() {
        for (Candidato c : aptos)
            c.setPuntuacion(0);
        paneEvaluar.getScene().getWindow().hide();
    }
    
    public void inicializarComponentes()
    {
        inicializarCandidatos();
        activarMover();
    }

    private void activarMover()
    {
        paneEvaluar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        paneEvaluar.setOnMouseDragged(event -> {
            paneEvaluar.getScene().getWindow().setX(event.getScreenX() - xOffset);
            paneEvaluar.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }
    
    private void inicializarCandidatos() {
        tbCandidatos.setItems(this.aptos.getObservableListAsociada());
        tbcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tbcPuntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
    }
}
