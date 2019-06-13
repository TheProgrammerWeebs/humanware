package humanware.usuarios;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import humanware.Habilidad;
import humanware.TitulacionEmpresa;
import humanware.Vacante;
import static humanware.utilidades.Utilidades.CURRENCY;
import static humanware.utilidades.Utilidades.ES;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class FXMLMostrarController implements Initializable
{
    @FXML private JFXTextField tfDescripcionVer;
    @FXML private JFXTextField tfEmpresaVer;
    @FXML private JFXTextField tfMinSalarioVer;
    @FXML private JFXTextField tfMaxSalarioVer;
    @FXML private JFXRadioButton rbCompletaVer;
    @FXML private JFXRadioButton rbParcialVer;
    @FXML private JFXRadioButton rbAmbasVer;
    @FXML private JFXTextArea taTitulacionesVer;
    @FXML private JFXTextArea taHabilidadesVer;
    private AnchorPane mostrarPane;
    
    private Vacante vacante;
    private double xOffset;
    private double yOffset;
    private ToggleGroup grupo;
    
            
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }    
    
    public void inicializarComponentes()
    {
        inicializarMover();
        grupo = new ToggleGroup();
        rbAmbasVer.setToggleGroup(grupo);
        rbParcialVer.setToggleGroup(grupo);
        rbCompletaVer.setToggleGroup(grupo);
    }
    
    public static void mostrarVacante(Vacante v)
    {
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource("/humanware/usuarios/FXMLMostrar.fxml"));
        AnchorPane pane = null;
        try {
            pane = cargador.load();
        } catch (IOException ex) {
            System.err.println("Error al mostrar vacante");
        }
        FXMLMostrarController controlador = cargador.getController();        
        controlador.mostrarPane = pane;
        controlador.inicializarComponentes();
        controlador.setVacante(v);  
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(new Scene(pane));
        stage.setTitle("Ver vacante: " + v.getDescripcion());
        stage.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        stage.centerOnScreen();
        stage.show();
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
    
    public void setVacante(Vacante vacante)
    {
        this.vacante = vacante;
        tfDescripcionVer.setText(vacante.getDescripcion());
        tfEmpresaVer.setText(vacante.getNombreEmpresa());
        tfMinSalarioVer.setText(CURRENCY + NumberFormat.getInstance(ES).format(vacante.getSalario().min));
        tfMaxSalarioVer.setText(CURRENCY + NumberFormat.getInstance(ES).format(vacante.getSalario().max));
        switch (vacante.getTipoJornada()) {
            case PARCIAL:
                rbParcialVer.setSelected(true);
                break;
            case COMPLETA:
                rbCompletaVer.setSelected(true);
                break;
            case AMBAS:
                rbAmbasVer.setSelected(true);
                break;
        }
        for (TitulacionEmpresa titulacion : vacante.getTitulaciones())
        {
            taTitulacionesVer.setText(taTitulacionesVer.getText() + "\n" + titulacion.titulacion + "/" + titulacion.importancia);
        }
        for (Habilidad habilidad : vacante.getHabilidades())
        {
            taHabilidadesVer.setText(taHabilidadesVer.getText() + "\n" + habilidad.habilidad + "/" + habilidad.nivel);
        }
    }
    
    public void cerrar()
    {
        mostrarPane.getScene().getWindow().hide();
    }
    
}
