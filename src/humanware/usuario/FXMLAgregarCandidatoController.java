package humanware.usuario;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import humanware.Candidato;
import humanware.Habilidad;
import humanware.Listas;
import humanware.TipoJornada;
import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.Utilidades;
import java.io.PrintWriter;
import java.net.URL;
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
        Listas.inicializarTitulos();
        grupo = new ToggleGroup();
        rbAmbas.setToggleGroup(grupo);
        rbParcial.setToggleGroup(grupo);
        rbCompleta.setToggleGroup(grupo);
        cbHabilidadNivel.setItems(Listas.niveles);
        cbTitulos.setItems(Listas.titulos);
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
        if (Utilidades.quitarEspacios(tfHabilidad.getText()).equals("") || this.cbHabilidadNivel.getValue() == null) {
            lbError.setText("Debe ingresar una habilidad");
            lbError.setVisible(true);
        } else {
            taHabilidades.setText(taHabilidades.getText()
                    + tfHabilidad.getText() + "/" + cbHabilidadNivel.getValue() + "\n");
        }
    }

    public void agregarTitulacion() {
        lbError.setVisible(false);
        if (this.cbTitulos.getValue() == null) {
            lbError.setText("Debe ingresar una titulación");
            lbError.setVisible(true);
        } else {
            taTitulaciones.setText(taTitulaciones.getText() + cbTitulos.getValue()+ "\n" );
        }
    }

    public void agregarCandidato() {
        lbError.setVisible(false);
        String telefono = Utilidades.formatearTelefono(tfTelefono.getText());
        if ((!rbParcial.isSelected() && !rbAmbas.isSelected() && !rbCompleta.isSelected())
                || Utilidades.quitarEspacios(this.tfHabilidad.getText()).equals("")
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
            String nombre = this.tfNombreCandidato.getText();
            String email = this.tfEmail.getText();
            double retribucion = Double.parseDouble(this.tfRetribucionMinima.getText());
            TipoJornada tipo = rbAmbas.isSelected() ? TipoJornada.AMBAS : rbCompleta.isSelected() ? TipoJornada.COMPLETA : TipoJornada.PARCIAL;
            ListaEnlazada<String> lineasHabilidades = Utilidades.split(taHabilidades.getText(), "\n");
            ListaEnlazada<String> lineasTitulaciones = Utilidades.split(taTitulaciones.getText(), "\n");
            ListaEnlazada<String> titulaciones = new ListaEnlazada();
            ListaEnlazada<Habilidad> habilidades = new ListaEnlazada();
            for (String linea : lineasHabilidades) {
                if (Utilidades.quitarEspacios(linea).equals("")) continue;
                ListaEnlazada<String> campos = Utilidades.split(linea, "/");
                habilidades.addFinal(new Habilidad(campos.get(0), Integer.parseInt(campos.get(1))));
            }
            for (String linea : lineasTitulaciones) {
                if (Utilidades.quitarEspacios(linea).equals("")) continue;
                titulaciones.addFinal(linea);
            }
            Candidato nuevo = new Candidato(nombre, telefono, email, titulaciones, habilidades, tipo, retribucion);
            Listas.candidatos.addFinal(nuevo);
            PrintWriter pw = Utilidades.openFileWrite("archivos\\database\\candidatos", true);
            System.out.println("pw = " + pw);
            pw.println(nuevo.convertirAString());
            pw.close();
            
            this.agregarCandidatoPane.getScene().getWindow().hide();
        }
    }

    public void cerrar() {
        agregarCandidatoPane.getScene().getWindow().hide();
    }
}
