package humanware.usuarios.usuario;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import humanware.Empresa;
import humanware.Habilidad;
import humanware.Listas;
import humanware.TipoJornada;
import humanware.TitulacionEmpresa;
import humanware.Vacante;
import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.ObtenerDatos;
import humanware.utilidades.Rango;
import humanware.utilidades.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class FXMLAgregarVacanteController implements Initializable
{

    //<editor-fold defaultstate="collapsed" desc="JavaFx Variables">
    @FXML
    private JFXComboBox cbEmpresas;
    @FXML
    private JFXComboBox cbTitulos;
    @FXML
    private JFXComboBox cbNivel;
    @FXML
    private JFXComboBox cbPrioridad;
    @FXML
    private AnchorPane vacantePane;
    @FXML
    private JFXRadioButton rbCompleta;
    @FXML
    private JFXRadioButton rbParcial;
    @FXML
    private JFXRadioButton rbAmbas;
    @FXML
    private JFXTextArea taTitulaciones;
    @FXML
    private Label lbError;
    @FXML
    private JFXTextField tfHabilidad;
    @FXML
    private JFXTextArea taHabilidades;
    @FXML
    private JFXTextField tfDescripcion;
    @FXML
    private JFXTextField tfMinSalario;
    @FXML
    private JFXTextField tfMaxSalario;
    //</editor-fold>

    private ToggleGroup grupo;

    private double xOffset;
    private double yOffset;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        inicializarMover();
        grupo = new ToggleGroup();
        rbCompleta.setToggleGroup(grupo);
        rbParcial.setToggleGroup(grupo);
        rbAmbas.setToggleGroup(grupo);
        cbEmpresas.setItems(Listas.nombreEmpresas);
        cbTitulos.setItems(Listas.titulos);
        cbNivel.setItems(Listas.niveles);
        cbPrioridad.setItems(Listas.niveles);
        cbTitulos.valueProperty().addListener((value, viejo, nuevo) -> {
            if (!cbTitulos.getItems().isEmpty() && nuevo != null && nuevo.equals("--No aparece en la lista--")) {
                try {
                    agregarTitulacionBaseDatos(ObtenerDatos.mostrarVentana("Ingrese el nombre de la titulación", "Agregar titulación"));
                    cbTitulos.setItems(Listas.titulos);
                } catch (IOException ex) {
                    System.err.println("Error de lectura o escritura al abrir obtener datos");
                }
            }
        });
    }

    private void inicializarMover() {
        vacantePane.setOnMousePressed(event
                -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        vacantePane.setOnMouseDragged(event
                -> {
            vacantePane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            vacantePane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }

    public void cerrar() {
        vacantePane.getScene().getWindow().hide();
    }

    public void agregarTitulo() throws IOException {
        lbError.setVisible(false);
        String opcion = (String) cbTitulos.getValue();
        if (opcion == null || cbPrioridad.getValue() == null) {
            lbError.setText("Debe llenar todos los campos");
            lbError.setVisible(true);
        } else {
            if (!opcion.equals("--No aparece en la lista--")) {
                ListaEnlazada<String> lineas = Utilidades.split(taTitulaciones.getText(), "\n");
                boolean existe = false;
                for (String linea : lineas) {
                    ListaEnlazada<String> campos = Utilidades.split(linea, "/");
                    if (campos.get(0).equals(opcion)) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    int prioridad = (int) cbPrioridad.getValue();
                    taTitulaciones.setText(taTitulaciones.getText() + "\n"
                            + opcion + "/" + prioridad);
                } else {
                    lbError.setText("El título ya está agregado");
                    lbError.setVisible(true);
                }
            }
        }
    }

    public static void agregarTitulacionBaseDatos(String titulacion) throws IOException {
        String ruta = "archivos\\database\\titulos";
        PrintWriter pw = Utilidades.openFileWrite(ruta, true);
        pw.println(titulacion);
        pw.close();
        Listas.cargarTitulos();
    }

    public void agregarHabilidad() {
        lbError.setVisible(false);
        String opcion = (String) tfHabilidad.getText();
        if (opcion == null || cbNivel.getValue() == null) {
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
                int nivel = (int) cbNivel.getValue();
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

    public void agregarVacante() throws IOException {
        lbError.setVisible(false);
        if ((!rbCompleta.isSelected() && !rbParcial.isSelected() && !rbAmbas.isSelected())
                || Utilidades.quitarEspacios(taTitulaciones.getText()).equals("")
                || Utilidades.quitarEspacios(tfDescripcion.getText()).equals("")
                || Utilidades.quitarEspacios(tfMinSalario.getText()).equals("")
                || Utilidades.quitarEspacios(tfMaxSalario.getText()).equals("")) {
            lbError.setText("Debe rellenar todos los campos obligatorios");
            lbError.setVisible(true);
        } else {
            boolean correcto = true;
            Rango salario = null;
            ListaEnlazada<Habilidad> habilidades = new ListaEnlazada<>();
            ListaEnlazada<TitulacionEmpresa> titulaciones = new ListaEnlazada<>();
            TipoJornada jornada = rbParcial.isSelected() ? TipoJornada.PARCIAL : (rbCompleta.isSelected() ? TipoJornada.COMPLETA : TipoJornada.AMBAS);
            String nombreEmpresa = (String) cbEmpresas.getValue();
            String descripcion = tfDescripcion.getText();
            ListaEnlazada<String> lineasHabilidades = Utilidades.split(taHabilidades.getText(), "\n");
            ListaEnlazada<String> lineasTitulaciones = Utilidades.split(taTitulaciones.getText(), "\n");
            try {
                salario = new Rango(Double.parseDouble(tfMinSalario.getText()), Double.parseDouble(tfMaxSalario.getText()));
            } catch (NumberFormatException e) {
                lbError.setText("El salario solo puede contener números reales");
                lbError.setVisible(true);
                correcto = false;
            }
            for (String linea : lineasHabilidades) {
                if (Utilidades.quitarEspacios(linea).equals("")) continue;
                ListaEnlazada<String> campos = Utilidades.split(linea, "/");
                habilidades.addFinal(new Habilidad(campos.get(0), Integer.parseInt(campos.get(1))));
            }
            for (String linea : lineasTitulaciones) {
                if (Utilidades.quitarEspacios(linea).equals("")) continue;
                ListaEnlazada<String> campos = Utilidades.split(linea, "/");
                titulaciones.addFinal(new TitulacionEmpresa(campos.get(0), Integer.parseInt(campos.get(1))));
            }
            if (correcto) {
                String codigo = UUID.randomUUID().toString();
                Vacante nuevaVacante = new Vacante(this.tfDescripcion.getText(), Empresa.convertirAEmpresa((String)this.cbEmpresas.getValue()), salario, jornada, titulaciones, habilidades, nombreEmpresa, descripcion, codigo);
                String linea = nuevaVacante.convertirString();
                linea += codigo;
                Listas.vacantes.addFinal(nuevaVacante);
                String ruta = "archivos\\database\\vacantes";
                PrintWriter pw = Utilidades.openFileWrite(ruta, true);
                pw.println(linea);
                pw.close();
                Utilidades.formatearArchivo(ruta);
                lbError.getScene().getWindow().hide();
                tfMaxSalario.setText("");
                tfMinSalario.setText("");
                rbAmbas.setSelected(false);
                rbCompleta.setSelected(false);
                rbParcial.setSelected(false);
            }
        }
    }
}
