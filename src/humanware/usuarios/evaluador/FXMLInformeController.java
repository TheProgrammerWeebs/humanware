package humanware.usuarios.evaluador;

import com.jfoenix.controls.JFXButton;
import humanware.Candidato;
import humanware.Listas;
import humanware.Vacante;
import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.ListaEnlazada.ComparadorNodos;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLInformeController implements Initializable
{
    @FXML TableView<Candidato> tbAceptados;
    @FXML TableColumn<Candidato, Integer> tbcPuntuacion;
    @FXML TableColumn<Candidato, String> tbcNombre;
    @FXML JFXButton btVerCandidato;
    @FXML AnchorPane informePane;
    @FXML Label lbTitulo;
    private double xOffset;
    private double yOffset;
    
    private ListaEnlazada<Candidato> aceptados;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activarMover();
    }
    
    private void activarMover()
    {
        informePane.setOnMousePressed(event
                -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        informePane.setOnMouseDragged(event
                -> {
            informePane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            informePane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }
    
    public void setAceptados(ListaEnlazada<Candidato> aceptados) {
        this.aceptados = aceptados;
        
        inicializarCandidatos();
    }
    private void inicializarCandidatos() {
        tbAceptados.setItems(aceptados.getObservableListAsociada());
        for(Candidato aaa : aceptados.getObservableListAsociada())
        {
            System.out.print(aaa.getPuntuacion() + " ");
        }
        tbcPuntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
        tbcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tbAceptados.getSelectionModel().selectedIndexProperty().addListener((valor, viejo, nuevo) -> {
            if (nuevo != null) btVerCandidato.setDisable(false);
        });
    }
    
    public void verCandidato()
    {
        humanware.usuarios.FXMLMostrarCandidatoController.mostrarCandidato(tbAceptados.getSelectionModel().getSelectedItem());
        tbAceptados.getSelectionModel().select(null);
        btVerCandidato.setDisable(true);
    }
    
    public void setLbTitulo(String titulo)
    {
        lbTitulo.setText(lbTitulo.getText() + " " + titulo);
    }
    
    public void cerrar()
    {
        this.tbAceptados.getScene().getWindow().hide();
    }
    
    public static void verInforme(Vacante v)
    {
        ListaEnlazada<Candidato> aceptados = obtenerCandidatos(v);
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource("/humanware/usuarios/evaluador/FXMLInforme.fxml"));
        AnchorPane pane = null;
        try{pane = cargador.load();}catch(IOException ex){System.err.println("Error al abrir informe");}
        FXMLInformeController controlador = cargador.getController();
        controlador.setAceptados(aceptados);
        controlador.setLbTitulo(v.getDescripcion());
        Stage st = new Stage(StageStyle.UNDECORATED);
        st.setScene(new Scene(pane));
        st.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        st.setResizable(false);
        st.setTitle("Informe: " + v.getDescripcion());
        st.initModality(Modality.APPLICATION_MODAL);
        st.centerOnScreen();
        st.showAndWait();
    }
    
    public static ListaEnlazada<Candidato> obtenerCandidatos(Vacante v)
    {
        ListaEnlazada<Candidato> aceptados = new ListaEnlazada<>();
        for (Candidato c : Listas.candidatos){
            if (c.getVacantes().get(0) != null)
            {
                boolean existe = false;
                for(Vacante vaca : c.getVacantes())
                    if (vaca.getDescripcion().equals(v.getDescripcion())) existe = true;
                if (existe)
                {
                    c.calcularPuntuacion(v);
                    aceptados.addFinal(c);
                    aceptados.bubbleSort((ComparadorNodos<Candidato>)(Candidato a, Candidato b) -> a.getPuntuacion() - b.getPuntuacion());
                }
            }
                
        }
        return aceptados;
    }
}
