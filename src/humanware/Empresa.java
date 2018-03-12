package humanware;

import java.util.ArrayList;
import humanware.utilidades.Utilidades;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;

public class Empresa
{

    private ArrayList<Vacante> vacantes;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty numeroTelefono;

    Empresa() {
        nombre = new SimpleStringProperty();
        numeroTelefono = new SimpleStringProperty();
        vacantes = new ArrayList<>();
    }

    public Empresa(String nombre, String numeroTelefono) {
        this();
        this.nombre.set(nombre);
        this.numeroTelefono.set(Utilidades.formatearTelefono(numeroTelefono));
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public final void cargarVacantes() throws FileNotFoundException, IOException {
        String ruta = "archivos\\database\\vacantes";
        BufferedReader buffer = Utilidades.openFileRead(ruta);
        while (buffer.ready()) {
            String linea = buffer.readLine();
            ArrayList<String> campos = Utilidades.split(linea, ";");
            String nombre = campos.get(Vacante.EMPRESA);
            if (!Utilidades.quitarEspacios(linea).equals("")) {
                if (nombre.equals(this.getNombre())) {
                    Vacante v = Vacante.convertirVacante(linea);
                    System.out.println("Vacante: " + v.convertirString());
                    vacantes.add(v);
                }
            }
        }
        buffer.close();
    }

    public SimpleStringProperty numeroTelefonoProperty() {
        return numeroTelefono;
    }

    public ArrayList<Vacante> getVacantes() {
        return vacantes;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getNumeroTelefono() {
        return numeroTelefono.get();
    }

    public String getTelefonoSinEspacios() {
        String sinEspacios = "";
        String telefono = getNumeroTelefono();
        for (int i = 0; i < telefono.length(); i++) {
            if (!telefono.substring(i, i + 1).equals(" ")) {
                sinEspacios += telefono.substring(i, i + 1);
            }
        }
        return sinEspacios;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono.set(Utilidades.formatearTelefono(numeroTelefono));
    }

    public Vacante getVacante(int pos) {
        return vacantes.get(pos);
    }

    public void addVacante(Vacante vacante) {
        vacantes.add(vacante);
    }

    public void eliminarVacante(Vacante vacante) {
        vacantes.remove(vacante);
    }

    public static Empresa convertirAEmpresa(String nombreEmpresa) {
        String ruta = "archivos\\database\\empresas";
        try {
            BufferedReader lector = Utilidades.openFileRead(ruta);
            while (lector.ready()) {
                String linea = lector.readLine();
                for (int i = 0; i < linea.length(); i++) {
                    if (linea.substring(i, i + 1).equals(";")) {
                        if (linea.substring(0, i).equals(nombreEmpresa)) {
                            lector.close();
                            return new Empresa(linea.substring(0, i), linea.substring(i + 1, linea.length()));
                        }
                    }
                }
                lector.close();
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Archivo empresas no encontrado");
        } catch (IOException ex) {
            System.err.println("Error de lectura o escritura");
        }
        return null;
    }
}
