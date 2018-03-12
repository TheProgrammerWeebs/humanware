package humanware.usuario;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import humanware.Habilidad;
import humanware.TitulacionEmpresa;
import humanware.Vacante;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
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
    @FXML private AnchorPane mostrarPane;
    private Vacante vacante;
    private double xOffset;
    private double yOffset;
    
    public void setVacante(Vacante vacante)
    {
        this.vacante = vacante;
        tfDescripcionVer.setText(vacante.getDescripcion());
        tfEmpresaVer.setText(vacante.getNombreEmpresa());
        tfMinSalarioVer.setText(Double.toString(vacante.getSalario().min));
        tfMaxSalarioVer.setText(Double.toString(vacante.getSalario().max));
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
            
    @Override
    public void initialize(URL url, ResourceBundle rb) 
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
    
    public void cerrar()
    {
        mostrarPane.getScene().getWindow().hide();
    }
    
}
