package humanware.utilidades;

import humanware.login.ControladorUsuario;
import humanware.login.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Locale;

public class Utilidades
{
    
    public static String SEP = File.separator;
    public static Locale ES = new Locale("es", "CO");
    public static String CURRENCY = "$ ";

    public static String quitarEspacios(String linea) {
        String lineaFinal = "";
        for (int i = 0; i < linea.length(); i++) {
            String subLinea = linea.substring(i, i + 1);
            if (!subLinea.equals(" ")) {
                lineaFinal += subLinea;
            }
        }
        return lineaFinal;
    }

    public static void abrirConfiguracion(Usuario usuario) throws IOException {
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource("/humanware/utilidades/FXMLConfiguracion.fxml"));
        AnchorPane pane = (AnchorPane) cargador.load();
        ControladorUsuario controlador = cargador.getController();
        controlador.setUsuario(usuario);
        Stage st = new Stage(StageStyle.UNDECORATED);
        st.setScene(new Scene(pane));
        st.setResizable(false);
        st.setTitle("Configuración: " + usuario.getNombre());
        st.initModality(Modality.APPLICATION_MODAL);
        st.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        st.showAndWait();
    }

    public static ListaEnlazada<String> split(String linea, String separador) {
        short principio = 0;
        ListaEnlazada<String> campos = new ListaEnlazada();
        for (int i = 0; i < linea.length(); i++) {
            if (linea.substring(i, i + 1).equals(separador)) {
                campos.addFinal(linea.substring(principio, i));
                principio = (short) (i + 1);
            }
        }
        campos.addFinal(linea.substring(principio));
        return campos;
    }

    public static boolean containsCharacter(String linea, String parte) {
        boolean existe = false;
        for (int i = 0; i < linea.length(); i++) {
            if (linea.equals(parte)) {
                existe = true;
            }
        }
        return existe;
    }

    public static boolean eliminarLinea(String linea, String ruta) {
        File archivo = new File(ruta);
        File temporal = new File(archivo + ".tmp");
        boolean encontrado;
        try (BufferedReader buffer = openFileRead(ruta); PrintWriter pw = Utilidades.openFileWrite(ruta + ".tmp", true)) {
            String line;
            encontrado = false;
            while (buffer.ready()) {
                line = buffer.readLine();
                if (!line.equals(linea)) {
                    pw.println(line);
                } else {
                    encontrado = true;
                }
            }
            pw.close();
            buffer.close();
            if (!archivo.delete()) {
                System.err.println("No se pudo eliminar el archivo de entrada");
            }
            if (!temporal.renameTo(archivo)) {
                System.err.println("No se pudo renombrar el archivo temporal");
            }
            return encontrado;
        } catch (FileNotFoundException ex) {
            System.err.println("Archivo no encontrado");
        } catch (IOException ex) {
            System.err.println("Error de lectura o escritura");
        }
        return false;
    }

    public static PrintWriter openFileWrite(String ruta, boolean append) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(ruta, append));
            return pw;
        } catch (IOException ex) {
            System.err.println("Error de IO");
        }
        return null;
    }

    public static BufferedReader openFileRead(String ruta) {
        try {
            BufferedReader buffer;
            buffer = new BufferedReader(new FileReader(ruta));
            return buffer;
        } catch (FileNotFoundException ex) {
            
            System.err.println("No se encontró el archivo " + ruta);
        }
        return null;
    }

    public static void formatearArchivo(String ruta) throws FileNotFoundException, IOException {
        BufferedReader buffer = openFileRead(ruta);
        while (buffer.ready()) {
            String linea = quitarEspacios(buffer.readLine());
            if (linea.equals("")) {
                buffer.close();
                eliminarLinea(linea, ruta);
                buffer = openFileRead(ruta);
            }
        }
        buffer.close();
    }

    public static String formatearTelefono(String telefono) {
        String formateada = "";
        for (int i = 0; i < telefono.length(); i++) {
            if (esNumero(telefono.substring(i, i + 1))) {
                formateada += telefono.substring(i, i + 1);
            }
        }
        if (formateada.length() != 10) {
            return null;
        }
        formateada = formateada.substring(0, 3) + " " + formateada.substring(3, 6) + " " + formateada.substring(6, 10);
        return formateada;
    }

    public static boolean esNumero(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esCorreo(String b) {
        int pos = b.indexOf("@");
        for (int i = pos; i < b.length() && pos != -1; i++) {
            if (b.substring(i, i + 1).equals(".")) {
                return true;
            }
        }
        return false;
    }

    public static void abrirVentanaUsuario(String ruta, String titulo) throws IOException {
        FXMLLoader cargador = new FXMLLoader(humanware.HumanWare.class.getResource(ruta));
        AnchorPane usuarioPane = cargador.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(usuarioPane));
        stage.setTitle(titulo);
        stage.show();
        stage.getIcons().add(new Image(humanware.HumanWare.class.getResourceAsStream("/humanware/resources/logoFondo.png")));
        stage.centerOnScreen();
    }
}
