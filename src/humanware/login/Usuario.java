package humanware.login;

import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.Utilidades;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import static humanware.utilidades.Utilidades.SEP;

public class Usuario
{
    private SimpleStringProperty nombre;
    private SimpleStringProperty contrasenia;
    private TipoUsuario tipo;
    private final int TIPO = 2;
    private boolean esValido;
    private static final int NOMBRE = 0;
    private static final int CONTRASENIA = 1;
    
    public Usuario(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
        contrasenia = new SimpleStringProperty();
    }
    public Usuario(String nombre, String contrasenia) {
        this(nombre);
        this.contrasenia = new SimpleStringProperty(contrasenia);
        this.nombre.addListener(this::actualizarNombre);
        this.contrasenia.addListener(this::actualizarContrasenia);
        determinarTipo();
    }
    public Usuario(String nombre, String contrasenia, TipoUsuario tipo) {
        this(nombre, contrasenia);
        this.tipo = tipo;
    }
    
    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia.set(contrasenia);
    }
    public String getNombre() {
        return nombre.get();
    }
    public String getContrasenia() {
        return contrasenia.get();
    }
    public TipoUsuario getTipo() {
        return tipo;
    }
    public boolean esValido() {
        return esValido;
    }
    public boolean nombreExiste() {
        String ruta = SEP+"archivos"+SEP+"database"+SEP+"usuarios";
        boolean existe = false;
        try
        {
            BufferedReader buffer = Utilidades.openFileRead(ruta);
            while (buffer.ready() && !existe)
            {
                String linea = buffer.readLine();
                if (Utilidades.split(linea, ";").get(NOMBRE).equals(this.nombre.get())) //Si el nombre ya existe...
                {
                    existe = true;
                }
            }
        } catch (IOException e)
        {
            System.err.println("Error de lectura o escritura en usuario");
        }
        
        return existe;
    }
    private void determinarTipo() {
        esValido = false;
        String ruta = "archivos"+SEP+"database"+SEP+"usuarios";
        try
        {
            BufferedReader buffer = Utilidades.openFileRead(ruta);

            while (buffer.ready())
            {
                String linea = buffer.readLine();
                ListaEnlazada<String> campos = Utilidades.split(linea, ";");
                if (campos.get(NOMBRE).equals(getNombre()) && campos.get(CONTRASENIA).equals(getContrasenia()))
                {
                    esValido = true;
                    switch (campos.get(TIPO))
                    {
                        case "ADMINISTRADOR":
                            this.tipo = TipoUsuario.ADMINISTRADOR;
                            break;
                        case "USUARIO":
                            this.tipo = TipoUsuario.USUARIO;
                            break;
                        case "EVALUADOR":
                            this.tipo = TipoUsuario.EVALUADOR;
                            break;
                        default:
                            this.tipo = TipoUsuario.INDEFINIDO;
                            esValido = false;
                    }
                    buffer.close();
                    break;
                }
                campos.vaciar();
            }
        } catch (FileNotFoundException ex)
        {
            System.err.println("Archivo no encontrado");
        } catch (IOException ex)
        {
            System.err.println("Error de lectura o escritura");
        }
    }
    
    public void actualizarUsuario(int posCampo, String viejo, String nuevo) {
        try
        {
            String ruta = SEP+"archivos"+SEP+"database"+SEP+"usuarios";
            BufferedReader buffer = Utilidades.openFileRead(ruta);
            PrintWriter pw;
            while (buffer.ready())
            {
                String linea = buffer.readLine();
                ListaEnlazada<String> campos = Utilidades.split(linea, ";");
                if (campos.get(posCampo).equals(viejo))
                {
                    buffer.close();
                    Utilidades.eliminarLinea(linea, ruta);
                    campos.set(posCampo, nuevo); 
                    linea = campos.get(NOMBRE) + ";" + campos.get(CONTRASENIA) + ";" + campos.get(TIPO);
                    pw = Utilidades.openFileWrite(ruta, true);
                    pw.println(linea);
                    pw.close();
                    break;
                }
            }
        } catch (FileNotFoundException ex)
        {
            System.err.println("Archivo usuarios no encontrado");
        } catch (IOException ex)
        {
            System.err.println("Error de lectura o escritura en usuarios");
        }
    }
    public void actualizarContrasenia(ObservableValue<? extends String> valorCambio, String viejo, String nuevo) {
        actualizarUsuario(CONTRASENIA, viejo, nuevo);
    }
    public void actualizarNombre(ObservableValue<? extends String> valorCambio, String viejo, String nuevo) {
        actualizarUsuario(NOMBRE, viejo, nuevo);
    }
    public void agregarBaseDatos() {
        PrintWriter pw = null;
        try
        {
            String ruta = "archivos"+SEP+"database"+SEP+"usuarios";
            pw = Utilidades.openFileWrite(ruta, true);
            pw.println(this.nombre.get() + ";" + this.contrasenia.get() + ";" + this.tipo);
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }
        }
    }
}
