package humanware.usuarios.evaluador;

import com.jfoenix.controls.JFXButton;
import humanware.Listas;
import humanware.Vacante;
import humanware.login.ControladorUsuario;
import humanware.login.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLEvaluadorController implements Initializable, ControladorUsuario
{
    @FXML TableView<Vacante> tbVacantes;
    @FXML TableColumn<Vacante, String> tbcDescripcion;
    @FXML TableColumn<Vacante, String> tbcEmpresa;
    @FXML JFXButton btEvaluar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void inicializarVacantes()
    {
        tbcEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        tbcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcionPuesto"));
        tbVacantes.setItems(Listas.vacantes.getObservableListAsociada());
        tbVacantes.getSelectionModel().selectedIndexProperty().addListener((valor, viejo, nuevo) -> {
            if (nuevo != null) btEvaluar.setDisable(false);
        });
    }
    
    public void evaluar()
    {
        
    }

    @Override
    public void setUsuario(Usuario usuario) {
        
    }

    @Override
    public void inicializarComponentes() {
        inicializarVacantes();
    }
    
}
