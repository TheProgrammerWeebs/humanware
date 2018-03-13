package humanware.usuario;

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
import humanware.utilidades.Rango;
import humanware.utilidades.Utilidades;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private JFXTextField tfTitulo;
    //</editor-fold>
    
    private ToggleGroup grupo;

    
    private double xOffset;
    private double yOffset;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        inicializarNombreEmpresas();
        inicializarNiveles();
        inicializarTitulos();
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
            if (nuevo.equals("--No aparece en la lista--")) {
                cbTitulos.setValue(false);
                tfTitulo.setVisible(true);
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

    private void inicializarNombreEmpresas() {
        if (!Listas.empresas.isEmpty()) {
            for (Empresa e : Listas.empresas) {
                Listas.nombreEmpresas.add(e.getNombre());
            }
        }

    }

    private void inicializarTitulos() {
        try {
            String ruta = "archivos\\database\\titulos";
            BufferedReader buffer = Utilidades.openFileRead(ruta);
            while (buffer.ready()) {
                Listas.titulos.add(buffer.readLine());
            }
            buffer.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Archivo titulaciones no encontrado");
        } catch (IOException ex) {
            System.err.println("Error de lectura o escritura al cargar titulos");
        }
    }

    private void inicializarNiveles() {
        for (int i = 1; i <= 5; i++) {
            Listas.niveles.add(i);
        }
    }

    public void cerrar() {
        vacantePane.getScene().getWindow().hide();
    }

    public void agregarTitulo() throws IOException {
        lbError.setVisible(false);
        tfTitulo.setVisible(false);
        String opcion = (String) cbTitulos.getValue();
        if (opcion == null || cbPrioridad.getValue() == null) {
            lbError.setText("Debe llenar todos los campos");
            lbError.setVisible(true);
        } else {
            if (!opcion.equals("--No aparece en la lista--")) {
                ArrayList<String> lineas = Utilidades.split(taTitulaciones.getText(), "\n");
                boolean existe = false;
                for (String linea : lineas) {
                    ArrayList<String> campos = Utilidades.split(linea, "/");
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
            } else {
                agregarTitulacionBaseDatos();
            }

        }
    }

    public void agregarTitulacionBaseDatos() throws IOException {
        cbTitulos.setVisible(true);
        tfTitulo.setVisible(false);
        if (!Utilidades.quitarEspacios(tfTitulo.getText()).equals("")) {
            String ruta = "archivos\\database\\titulos";
            PrintWriter pw = Utilidades.openFileWrite(ruta, true);
            pw.println(tfTitulo.getText());
            inicializarTitulos();
            pw.close();
        }
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
            ArrayList<String> lineas = Utilidades.split(taHabilidades.getText(), "\n");
            boolean existe = false;
            for (String linea : lineas) {
                ArrayList<String> campos = Utilidades.split(linea, "/");
                if (campos.get(0).equals(opcion)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                int nivel = (int) cbNivel.getValue();
                taHabilidades.setText(taHabilidades.getText() + "\n"
                        + opcion + "/" + nivel);
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
            ArrayList<Habilidad> habilidades = new ArrayList<>();
            ArrayList<TitulacionEmpresa> titulaciones = new ArrayList<>();
            TipoJornada jornada = rbParcial.isSelected() ? TipoJornada.PARCIAL : (rbCompleta.isSelected() ? TipoJornada.COMPLETA : TipoJornada.AMBAS);
            String nombreEmpresa = (String) cbEmpresas.getValue();
            String descripcion = tfDescripcion.getText();
            ArrayList<String> lineasHabilidades = Utilidades.split(taHabilidades.getText(), "\n");
            lineasHabilidades.remove(0);
            ArrayList<String> lineasTitulaciones = Utilidades.split(taTitulaciones.getText(), "\n");
            lineasTitulaciones.remove(0);
            try {
                salario = new Rango(Double.parseDouble(tfMinSalario.getText()), Double.parseDouble(tfMaxSalario.getText()));
            } catch (NumberFormatException e) {
                lbError.setText("El salario solo puede contener números reales");
                lbError.setVisible(true);
                correcto = false;
            }
            for (String linea : lineasHabilidades) {
                ArrayList<String> campos = Utilidades.split(linea, "/");
                habilidades.add(new Habilidad(campos.get(0), Integer.parseInt(campos.get(1))));
            }
            for (String linea : lineasTitulaciones) {
                ArrayList<String> campos = Utilidades.split(linea, "/");
                titulaciones.add(new TitulacionEmpresa(campos.get(0), Integer.parseInt(campos.get(1))));
            }
            if (correcto) {
                Vacante nuevaVacante = new Vacante(salario, jornada, titulaciones, habilidades, nombreEmpresa, descripcion);
                Listas.vacantes.add(nuevaVacante);
                String linea = nuevaVacante.convertirString();
                String ruta = "archivos\\database\\vacantes";
                PrintWriter pw = Utilidades.openFileWrite(ruta, true);
                pw.println(linea);
                pw.close();
                Utilidades.formatearArchivo(ruta);
                this.lbError.getScene().getWindow().hide();
                this.tfTitulo.setText("");
                this.tfMaxSalario.setText("");
                this.tfMinSalario.setText("");
                this.rbAmbas.setSelected(false);
                this.rbCompleta.setSelected(false);
                this.rbParcial.setSelected(false);
            }
        }
    }
}
