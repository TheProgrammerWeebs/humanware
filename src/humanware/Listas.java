package humanware;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author d-ani
 */
public class Listas
{
    private Listas(){}
    private static ObservableList<Integer> generarNumeros(int limite)
    {
        ObservableList<Integer> numeros = FXCollections.observableArrayList();
        for (int i = 1; i <= limite; i++) numeros.add(i);
        return numeros;
    }
    public static ObservableList<String> nombreEmpresas = FXCollections.observableArrayList();
    public static ObservableList<String> titulos = FXCollections.observableArrayList();
    public static ObservableList<Integer> niveles = generarNumeros(5);
    public static ObservableList<Empresa> empresas = FXCollections.observableArrayList();
    public static ObservableList<Vacante> vacantes = FXCollections.observableArrayList();
    public static ObservableList<Candidato> candidatos = FXCollections.observableArrayList();
}
