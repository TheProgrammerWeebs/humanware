package humanware.usuarios.usuario;

import humanware.usuarios.FXMLMostrarController;
import humanware.login.ControladorUsuario;
import humanware.Candidato;
import humanware.Empresa;
import humanware.Vacante;
import humanware.login.Usuario;
import humanware.utilidades.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import humanware.Listas;
import humanware.usuarios.FXMLMostrarCandidatoController;
import static humanware.utilidades.Utilidades.SEP;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Label;

public class FXMLUsuarioController implements Initializable, ControladorUsuario
{

    private double xOffset;
    private double yOffset;
    private Usuario usuario;

    // <editor-fold defaultstate="collapsed" desc="Variables FXML">
    @FXML
    private TableView<Empresa> tbEmpresas;
    @FXML
    private TableColumn<Empresa, String> tbcNombreEmpresa;
    @FXML
    private TableColumn<Empresa, String> tbcTelefonoEmpresa;
    @FXML
    private TableView<Vacante> tbVacantes;
    @FXML
    private TableColumn<Vacante, String> tbcNombreVacante;
    @FXML
    private TableColumn<Vacante, String> tbcEmpresaVacante;
    @FXML
    private TableView<Candidato> tbCandidatos;
    @FXML
    private TableColumn<Candidato, String> tbcNombreCandidato;
    @FXML
    private TableColumn<Candidato, String> tbcEmailCandidato;
    @FXML
    private JFXTextField tfNombre;
    @FXML
    private JFXTextField tfTelefono;
    @FXML
    private JFXButton btEliminarVacante;
    @FXML
    private JFXButton btAgregarEmpresa;
    @FXML
    private JFXButton btVerVacante;
    @FXML
    private JFXButton btEliminarEmpresa;
    @FXML
    private JFXButton btVerCandidato;
    @FXML
    private JFXButton btEliminarCandidato;
    @FXML
    private AnchorPane usuarioPane;
    @FXML
    private Label lbErrorEmpresas;

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="lógica de la aplicación">
    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @Override
    public void inicializarComponentes() {
        usuarioPane.setOnMousePressed(event
                -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        usuarioPane.setOnMouseDragged(event
                -> {
            usuarioPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            usuarioPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
        inicializarEmpresas();
        inicializarVacantes();
        try {inicializarCandidatos();} catch(IOException ex){System.err.println("Error al inicializar candidato");}
    }

    public void cerrar() {
        System.exit(0);
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
            usuarioPane.getScene().getWindow().hide();
        } catch (IOException ex) {
            System.err.println("Error al cargar ventana login");
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="lógica de empresas">
    private void inicializarEmpresas() {
        tbEmpresas.setEditable(false);
        btAgregarEmpresa.setDefaultButton(true);
        tbcNombreEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tbcTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<>("numeroTelefono"));
        tbEmpresas.setItems(Listas.empresas.getObservableListAsociada());
        tbEmpresas.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                btEliminarEmpresa.setDisable(false);
            }
        });
    }

    public void agregarEmpresa() {
        String tel = Utilidades.formatearTelefono(tfTelefono.getText());
        lbErrorEmpresas.setVisible(false);
        lbErrorEmpresas.setWrapText(true);
        if (Utilidades.quitarEspacios(tfNombre.getText()).equals("")) {
            lbErrorEmpresas.setText("El nombre de la empresa no debe estar vacío");
            lbErrorEmpresas.setVisible(true);
        } else if (tel == null) {
            lbErrorEmpresas.setText("El teléfono no tiene 10 números");
            lbErrorEmpresas.setVisible(true);
        } else if (Utilidades.containsCharacter(tel, ";") || Utilidades.containsCharacter(tfNombre.getText(), ";")) {
            lbErrorEmpresas.setText("Ninguno de los campos debe llevar punto y coma");
            lbErrorEmpresas.setVisible(true);
        } else {
            String ruta = "archivos"+SEP+"database"+SEP+"empresas";
            try (PrintWriter pw = Utilidades.openFileWrite(ruta, true)) {
                pw.println(tfNombre.getText() + ";" + Utilidades.quitarEspacios(Utilidades.formatearTelefono(tfTelefono.getText())));
            }
            Listas.empresas.addFinal(new Empresa(tfNombre.getText(), tfTelefono.getText()));
            tfNombre.setText("");
            tfTelefono.setText("");
        }
    }

    public void eliminarEmpresa() throws IOException {
        String ruta = "archivos"+SEP+"database"+SEP+"empresas";
        Empresa e = tbEmpresas.getSelectionModel().getSelectedItem();
        e.cargarVacantes();
        for (Vacante v : e.getVacantes()) 
            Utilidades.eliminarLinea(v.convertirString(), "archivos"+SEP+"database"+SEP+"vacantes");
        String empresa = e.getNombre() + ";" + e.getTelefonoSinEspacios();
        Utilidades.eliminarLinea(empresa, ruta);
        Listas.empresas.remove(e);
        btEliminarEmpresa.setDisable(true);
        tbEmpresas.getSelectionModel().select(null);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="lógica de vacantes">
    private void inicializarVacantes() { 
        tbVacantes.setEditable(false);
        tbcNombreVacante.setCellValueFactory(new PropertyValueFactory<>("descripcionPuesto"));
        tbcEmpresaVacante.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        tbVacantes.setItems(Listas.vacantes.getObservableListAsociada());
        tbVacantes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                btEliminarVacante.setDisable(false);
                btVerVacante.setDisable(false);
            }
        });
    }
    
    public void agregarVacante() {
        try {
            Utilidades.abrirVentanaUsuario("/humanware/usuarios/usuario/FXMLAgregarVacante.fxml", "Agregar vacante");
        } catch (IOException ex) {
            System.err.println("Error de lectuta o escritura al abrir agregar vacante");
        }
    }

    public void eliminarVacante() {
        Vacante v = tbVacantes.getSelectionModel().getSelectedItem();
        Listas.vacantes.remove(v);
        Utilidades.eliminarLinea(v.convertirStringCodigo(), "archivos"+SEP+"database"+SEP+"vacantes");
        btEliminarVacante.setDisable(true);
        tbVacantes.getSelectionModel().select(null);
    }

   
    public void verVacante() throws IOException {
        
        FXMLMostrarController.mostrarVacante(tbVacantes.getSelectionModel().getSelectedItem());
        tbVacantes.getSelectionModel().select(null);
        btVerVacante.setDisable(true);
        this.btEliminarVacante.setDisable(true);
    }

    // </editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="lógica de candidatos">
    private void inicializarCandidatos() throws IOException {
        tbCandidatos.setEditable(false);
        this.tbcNombreCandidato.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.tbcEmailCandidato.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.tbCandidatos.setItems(Listas.candidatos.getObservableListAsociada());
        tbCandidatos.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                btEliminarCandidato.setDisable(false);
                btVerCandidato.setDisable(false);
            }
        });
    }
    public void abrirAgregarCandidato() throws IOException {
        Utilidades.abrirVentanaUsuario("/humanware/usuarios/usuario/FXMLAgregarCandidato.fxml", "Agregar candidato");
    }
    public void eliminarCandidatos()
    {
        Candidato c = tbCandidatos.getSelectionModel().getSelectedItem();
        Listas.candidatos.remove(c);
        String linea = c.convertirAString();
        Utilidades.eliminarLinea(linea, "archivos"+SEP+"database"+SEP+"candidatos");
        tbCandidatos.getSelectionModel().select(null);
    }
    
    public void mostrarCandidato()
    {
        FXMLMostrarCandidatoController.mostrarCandidato(tbCandidatos.getSelectionModel().getSelectedItem());
        btVerCandidato.setDisable(true);
        btEliminarCandidato.setDisable(true);
        tbCandidatos.getSelectionModel().select(null);
    }

    //</editor-fold>
}
