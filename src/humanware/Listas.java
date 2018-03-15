package humanware;

import humanware.utilidades.Utilidades;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
    
/**
 *
 * @author d-ani
 */
public class Listas
{
    private Listas(){}
    private static ObservableList<Integer> generarNumeros(int limite)  {
        ObservableList<Integer> numeros = FXCollections.observableArrayList();
        for (int i = 1; i <= limite; i++) numeros.add(i);
        return numeros;
    }
    public static void inicializarTitulos() {
        try {
            //titulos.clear();
            titulos = FXCollections.observableArrayList();
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
    public static ObservableList<String> nombreEmpresas = FXCollections.observableArrayList();
    public static ObservableList<String> titulos = FXCollections.observableArrayList();
    public static ObservableList<Integer> niveles = generarNumeros(5);
    public static ObservableList<Empresa> empresas = FXCollections.observableArrayList();
    public static ObservableList<Vacante> vacantes = FXCollections.observableArrayList();
    public static ObservableList<Candidato> candidatos = FXCollections.observableArrayList();
}
