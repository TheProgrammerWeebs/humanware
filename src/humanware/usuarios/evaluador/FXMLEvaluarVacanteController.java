package humanware.usuarios.evaluador;

import com.jfoenix.controls.JFXButton;
import humanware.Candidato;
import humanware.Vacante;
import humanware.usuarios.FXMLMostrarCandidatoController;
import humanware.utilidades.ListaEnlazada;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class FXMLEvaluarVacanteController implements Initializable
{
    @FXML
    private AnchorPane paneEvaluar;
    @FXML
    private JFXButton btSeleccionar;
    @FXML
    private JFXButton btDetalles;
    @FXML
    private TableView<Candidato> tbCandidatos;
    @FXML
    private TableColumn<Candidato, String> tbcNombre;
    @FXML
    private TableColumn<Candidato, Integer> tbcPuntuacion;
    private Vacante vacante;
    private double xOffset;
    private double yOffset;
    private ListaEnlazada<Candidato> aptos;
    private ListaEnlazada<Candidato> seleccionados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        seleccionados = new ListaEnlazada<>();
    }

    public void setAptos(ListaEnlazada<Candidato> aptos, Vacante vacante) {
        this.vacante = vacante;
        this.aptos = aptos;
        inicializarComponentes();
    }

    public void seleccionar() {
        int seguro = JOptionPane.showConfirmDialog(null, "¿Desea seleccionarlo?", "Pregunta", JOptionPane.YES_NO_OPTION);
        if (seguro == JOptionPane.OK_OPTION) {
            Candidato c = tbCandidatos.getSelectionModel().getSelectedItem();
            c.addVacante(vacante);
            vacante.addApto(c);
            seleccionados.addFinal(c);
            aptos.remove(c);
        }
        reiniciarBotones();
    }
   
    public void rechazar() {
        Candidato c = tbCandidatos.getSelectionModel().getSelectedItem();
        aptos.remove(c);
        reiniciarBotones();
    }
    
    public void detalles()
    {
        FXMLMostrarCandidatoController.mostrarCandidato(tbCandidatos.getSelectionModel().getSelectedItem());
        reiniciarBotones();
    }
    
    private void reiniciarBotones()
    {
        this.tbCandidatos.getSelectionModel().select(null);
        this.btDetalles.setDisable(true);
        this.btSeleccionar.setDisable(true);
    }
    
    public void cerrar() {
        reiniciarPuntuacionCandidatos();
        paneEvaluar.getScene().getWindow().hide();
    }
    
    private void reiniciarPuntuacionCandidatos()
    {
        if (!aptos.estaVacia())
            for (int i = 0; i < aptos.size(); i++) {
                System.out.println("aptos = " + aptos);
            }
            /*for (Candidato c : aptos)
            {
                System.out.println("c = " + c);
                //c.setPuntuacion(0);
            }*/
                
                
        if (!seleccionados.estaVacia())
            for (Candidato c : seleccionados)
                c.setPuntuacion(0);
    }

    public void inicializarComponentes() {
        inicializarCandidatos();
        activarMover();
    }

    private void activarMover() {
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
        tbCandidatos.setItems(aptos.getObservableListAsociada());
        tbcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tbcPuntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
        tbCandidatos.getSelectionModel().selectedItemProperty().addListener((valor, viejo, nuevo) -> {
            if (valor != null) {
                this.btSeleccionar.setDisable(false);
                this.btDetalles.setDisable(false);
            }
        });
    }
}