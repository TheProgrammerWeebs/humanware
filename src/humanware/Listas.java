package humanware;

import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.Rango;
import humanware.utilidades.Utilidades;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author d-ani
 */
public class Listas
{

    private Listas() {
    }

    public static ListaEnlazada<Empresa> empresas;
    public static ListaEnlazada<Vacante> vacantes;
    public static ListaEnlazada<Candidato> candidatos;
    public static ObservableList<String> nombreEmpresas;
    public static ObservableList<String> titulos;
    public static ObservableList<Integer> niveles;

    public static void cargarListas()
    {
        cargarNiveles(5);
        cargarTitulos();
        cargarEmpresas();
        cargarNombreEmpresas();
        cargarCandidatos();
        cargarVacantes();
    }
    
    private static void cargarNiveles(int limite) {
        niveles = FXCollections.observableArrayList();
        for (int i = 1; i <= limite; i++) {
            niveles.add(i);
        }
    }
    public static void cargarTitulos() {
        titulos = FXCollections.observableArrayList();
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
    private static void cargarEmpresas() {
        empresas = new ListaEnlazada<>();
        String ruta = "archivos\\database\\empresas";
        try (BufferedReader lector = Utilidades.openFileRead(ruta)) {
            while (lector.ready()) {
                String linea = lector.readLine();
                boolean encontrado = false;
                ListaEnlazada<String> campos = Utilidades.split(linea, ";");
                if (!Utilidades.quitarEspacios(linea).equals("")) {
                    Listas.empresas.addFinal(new Empresa(campos.get(0), campos.get(1)));
                }
            }
        } catch (IOException ex) {
            System.err.println("Error de lectura o escritura");
        }
    }
    private static void cargarVacantes() {
        vacantes = new ListaEnlazada<>();
        String ruta = "archivos\\database\\vacantes";
        try (BufferedReader lector = Utilidades.openFileRead(ruta)) {
            while (lector.ready()) {
                String linea = lector.readLine();
                boolean encontrado = false;
                if (!Utilidades.quitarEspacios(linea).equals("")) {
                    ListaEnlazada<String> campos = Utilidades.split(linea, ";");
                    Vacante vaca = new Vacante(campos.get(Vacante.CODIGO));
                    vaca.setDescripcion(campos.get(Vacante.DESCRIPCION));
                    vaca.setTipoJornada(TipoJornada.convertirAJornada(campos.get(Vacante.JORNADA)));
                    vaca.setNombreEmpresa(campos.get(Vacante.EMPRESA));
                    vaca.setSalario(Rango.convertirARango(campos.get(Vacante.SALARIO)));
                    vaca.setTitulaciones(TitulacionEmpresa.convertirATitulaciones(campos.get(Vacante.TITULACIONES)));
                    if (campos.size() > 5) {
                        vaca.setHabilidades(Habilidad.convertirAHabilidades(campos.get(Vacante.HABILIDADES))); //Si tiene habilidades
                    }
                    Listas.vacantes.addFinal(vaca);
                }
            }
        } catch (IOException ex) {
            System.err.println("Error de lectura o escritura");
        }
    }
    private static void cargarNombreEmpresas() {
        nombreEmpresas = FXCollections.observableArrayList();
        if (!Listas.empresas.estaVacia()) {
            for (Empresa e : Listas.empresas) {
                Listas.nombreEmpresas.add(e.getNombre());
            }
        }
    }
    private static void cargarCandidatos() {
        candidatos = new ListaEnlazada<>();
        try {
            BufferedReader buffer = Utilidades.openFileRead("archivos\\database\\candidatos");
            String linea;
            
            while (buffer.ready()) {
                linea = buffer.readLine();
                Listas.candidatos.addFinal(Candidato.parseCandidato(linea));
            }
        } catch (IOException ex) {
            System.err.println("Error al cargar candidatos");
        }
    }
    

}
