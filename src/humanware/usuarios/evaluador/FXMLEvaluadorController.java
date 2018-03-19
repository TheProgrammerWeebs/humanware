package humanware.usuarios.evaluador;

import com.jfoenix.controls.JFXButton;
import humanware.Candidato;
import humanware.Habilidad;
import humanware.Listas;
import humanware.TitulacionEmpresa;
import humanware.Vacante;
import humanware.login.ControladorUsuario;
import humanware.login.Usuario;
import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.ListaEnlazada.ComparadorNodos;
import humanware.utilidades.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLEvaluadorController implements Initializable, ControladorUsuario
{

    @FXML
    TableView<Vacante> tbVacantes;
    @FXML
    TableColumn<Vacante, String> tbcDescripcion;
    @FXML
    TableColumn<Vacante, String> tbcEmpresa;
    @FXML
    JFXButton btEvaluar;
    @FXML
    AnchorPane evaluadorPane;

    private double xOffset;
    private double yOffset;
    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void inicializarVacantes() {
        tbcEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        tbcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcionPuesto"));
        tbVacantes.setItems(Listas.vacantes.getObservableListAsociada());
        tbVacantes.getSelectionModel().selectedIndexProperty().addListener((valor, viejo, nuevo) -> {
            if (nuevo != null) {
                btEvaluar.setDisable(false);
            }
        });
    }

    public void cambiarDeUsuario() {
        try {
            AnchorPane ap = FXMLLoader.load(getClass().getResource("/humanware/login/FXMLLogin.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setScene(new Scene(ap));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("HUMANWARE: Login");
            stage.centerOnScreen();
            stage.show();
            evaluadorPane.getScene().getWindow().hide();
        } catch (IOException ex) {
            System.err.println("Error al cargar ventana login");
        }
    }

    public void abrirConfiguracion() {
        try {
            Utilidades.abrirConfiguracion(usuario);
        } catch (IOException ex) {
            System.err.println("Error de lectura o escritura al abrir configuración");
        } catch (NullPointerException ex) {
            System.err.println("Usuario no ha sido inicializado");
        }
    }

    public void evaluar() {    
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource("/humanware/usuarios/evaluador/FXMLEvaluarVacante.fxml"));        
        Vacante v = tbVacantes.getSelectionModel().getSelectedItem();
        ListaEnlazada<Candidato> aptos = obtenerAptos(v);
        AnchorPane pane;
        try{pane = cargador.load();}
        catch(IOException ex){System.err.println("Error al abrir evaluar"); pane=null;ex.printStackTrace();}
        FXMLEvaluarVacanteController controlador = cargador.getController();
        controlador.setAptos(aptos);
        Stage st = new Stage(StageStyle.UNDECORATED);
        st.setScene(new Scene(pane));
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Evaluando " + v.getDescripcion());
        st.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        st.showAndWait();
        btEvaluar.setDisable(true);
        tbVacantes.getSelectionModel().select(null);
    }

    private ListaEnlazada<Candidato> obtenerAptos(Vacante v) {
        ListaEnlazada<Candidato> aptos = new ListaEnlazada<>();
        for (Candidato c : Listas.candidatos) {
            System.out.println("c = " + c);
            boolean apto = false;
            for (TitulacionEmpresa titulacion : v.getTitulaciones()) {
                for (String titulacionCandidato : c.getTitulaciones()) {
                    if (titulacion.titulacion.equals(titulacionCandidato)) {
                        c.setPuntuacion(c.getPuntuacion() + titulacion.importancia);
                        apto = true;
                    }
                }
            }
            if (apto) {
                if (v.getTipoJornada() == c.getTipoJornada()) c.setPuntuacion(c.getPuntuacion() + 1);
                for (Habilidad h : v.getHabilidades()) {
                    for(Habilidad hCandidato: c.getHabilidades())    
                        if (hCandidato.habilidad.equals(h.habilidad))
                            c.setPuntuacion(c.getPuntuacion() + h.nivel);
                }
                aptos.addOrdenado(c, (ComparadorNodos<Candidato>) (Candidato a, Candidato b) -> a.getPuntuacion() - b.getPuntuacion());
            }
        }
        return aptos;
    }

    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void inicializarComponentes() {
        inicializarVacantes();
        activarMover();
    }

    private void activarMover() {
        evaluadorPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        evaluadorPane.setOnMouseDragged(event -> {
            evaluadorPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            evaluadorPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }

    public void cerrar() {
        evaluadorPane.getScene().getWindow().hide();
    }
}
