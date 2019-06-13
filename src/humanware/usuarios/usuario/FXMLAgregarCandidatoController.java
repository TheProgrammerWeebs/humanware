package humanware.usuarios.usuario;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import humanware.Candidato;
import humanware.Habilidad;
import humanware.Listas;
import humanware.TipoJornada;
import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.ObtenerDatos;
import humanware.utilidades.Utilidades;
import static humanware.utilidades.Utilidades.ES;
import static humanware.utilidades.Utilidades.SEP;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Locale;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class FXMLAgregarCandidatoController implements Initializable
{

    //<editor-fold defaultstate="collapsed" desc="JavaFx Variables">
    @FXML
    private Label lbError;
    @FXML
    private JFXComboBox cbTitulos;
    @FXML
    private JFXComboBox cbHabilidadNivel;
    @FXML
    private JFXTextField tfHabilidad;
    @FXML
    private JFXTextField tfEmail;
    @FXML
    private JFXTextField tfTelefono;
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

    public void inicializarComponentes() {
        inicializarMover();
        grupo = new ToggleGroup();
        rbAmbas.setToggleGroup(grupo);
        rbParcial.setToggleGroup(grupo);
        rbCompleta.setToggleGroup(grupo);
        cbHabilidadNivel.setItems(Listas.niveles);
        cbTitulos.setItems(Listas.titulos);
        cbTitulos.valueProperty().addListener((value, viejo, nuevo) -> {
            if (!cbTitulos.getItems().isEmpty() && nuevo != null && nuevo.equals("--No aparece en la lista--")) {
                try {
                    FXMLAgregarVacanteController.agregarTitulacionBaseDatos(ObtenerDatos.mostrarVentana("Ingrese el nombre de la titulación", "Agregar titulación"));
                    cbTitulos.setItems(Listas.titulos);
                } catch (IOException ex) {
                    System.err.println("Error de lectura o escritura al abrir obtener datos");
                }
            }
        });
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

    public void agregarHabilidad() {
        lbError.setVisible(false);
        String opcion = (String) tfHabilidad.getText();
        if (opcion == null || cbHabilidadNivel.getValue() == null) {
            lbError.setText("Debe rellenar todos los campos");
            lbError.setVisible(true);
        } else if (Utilidades.containsCharacter(opcion, ";") || Utilidades.containsCharacter(opcion, ",") || Utilidades.containsCharacter(opcion, ".")) {
            lbError.setText("La habilidad no puede signos de puntuación");
            lbError.setVisible(true);
        } else {
            ListaEnlazada<String> lineas = Utilidades.split(taHabilidades.getText(), "\n");
            boolean existe = false;
            for (String linea : lineas) {
                ListaEnlazada<String> campos = Utilidades.split(linea, "/");
                if (campos.get(0).equals(opcion)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                int nivel = (int) cbHabilidadNivel.getValue();
                taHabilidades.setText(taHabilidades.getText() + "\n"
                        + opcion + "/" + nivel);
                tfHabilidad.setText("");
                tfHabilidad.requestFocus();
            } else {
                lbError.setText("La habilidad ya está agregada");
                lbError.setVisible(true);
            }
        }
    }
    public void agregarTitulacion() {
        lbError.setVisible(false);
        boolean existe = Utilidades.split(taTitulaciones.getText(), "\n").existe((String) this.cbTitulos.getValue());
        if (this.cbTitulos.getValue() == null) {
            lbError.setText("Debe ingresar una titulación");
            lbError.setVisible(true);
            cbTitulos.getSelectionModel().select(null);
        } else if (existe) {
            lbError.setText("Ya existe");
            lbError.setVisible(true);
        } else if (!cbTitulos.getValue().equals("--No aparece en la lista--")){
            taTitulaciones.setText(taTitulaciones.getText() + cbTitulos.getValue() + "\n");
        }
    }
    public void agregarCandidato() {
        lbError.setVisible(false);
        String telefono = Utilidades.formatearTelefono(tfTelefono.getText());
        if ((!rbParcial.isSelected() && !rbAmbas.isSelected() && !rbCompleta.isSelected())
                || Utilidades.quitarEspacios(this.taHabilidades.getText()).equals("")
                || Utilidades.quitarEspacios(this.taTitulaciones.getText()).equals("")
                || Utilidades.quitarEspacios(this.tfNombreCandidato.getText()).equals("")
                || Utilidades.quitarEspacios(this.tfRetribucionMinima.getText()).equals("")) {
            lbError.setText("Debe ingresar todos los campos");
            lbError.setVisible(true);
        } else if (!Utilidades.esNumero(this.tfRetribucionMinima.getText())) {
            lbError.setText("La retribución mínima debe ser un número");
            lbError.setVisible(true);
        } else if (!Utilidades.esCorreo(this.tfEmail.getText())) {
            lbError.setText("El formato del correo es incorrecto");
            lbError.setVisible(true);
        } else if (telefono == null) {
            lbError.setText("El formato del teléfono es incorrecto");
            lbError.setVisible(true);
        } else {
            try {
                String nombre = this.tfNombreCandidato.getText();
                String email = this.tfEmail.getText();
                Number retribucion = NumberFormat.getCurrencyInstance(ES).parse(this.tfRetribucionMinima.getText());
                TipoJornada tipo = rbAmbas.isSelected() ? TipoJornada.AMBAS : rbCompleta.isSelected() ? TipoJornada.COMPLETA : TipoJornada.PARCIAL;
                ListaEnlazada<String> lineasHabilidades = Utilidades.split(taHabilidades.getText(), "\n");
                ListaEnlazada<String> lineasTitulaciones = Utilidades.split(taTitulaciones.getText(), "\n");
                ListaEnlazada<String> titulaciones = new ListaEnlazada();
                ListaEnlazada<Habilidad> habilidades = new ListaEnlazada();
                for (String linea : lineasHabilidades) {
                    if (Utilidades.quitarEspacios(linea).equals("")) {
                        continue;
                    }
                    ListaEnlazada<String> campos = Utilidades.split(linea, "/");
                    habilidades.addFinal(new Habilidad(campos.get(0), Integer.parseInt(campos.get(1))));
                }
                for (String linea : lineasTitulaciones) {
                    if (Utilidades.quitarEspacios(linea).equals("")) {
                        continue;
                    }
                    titulaciones.addFinal(linea);
                }
                Candidato nuevo = new Candidato(nombre, telefono, email, titulaciones, habilidades, tipo, retribucion.doubleValue());
                Listas.candidatos.addFinal(nuevo);
                PrintWriter pw = Utilidades.openFileWrite("archivos"+SEP+"database"+SEP+"candidatos", true);
                pw.println(nuevo.convertirAString());
                pw.close();
                this.agregarCandidatoPane.getScene().getWindow().hide();
            } catch (ParseException ex) {
                System.err.println("Error de formato");
            }
        }
    }

    public void cerrar() {
        agregarCandidatoPane.getScene().getWindow().hide();
    }
}
