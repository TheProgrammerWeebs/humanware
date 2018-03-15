package humanware;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Candidato
{
    private final String codigo;
    private SimpleStringProperty email;
    private SimpleStringProperty nombre;
    private String telefono;
    private double retribucion;
    private ArrayList<String> titulaciones;
    private ArrayList<Habilidad> habilidades;
    private TipoJornada tipoJornada;
    private final int anioActual = LocalDateTime.now().getYear();
    private static int nCandidatos = 0;

    public Candidato()
    {
        nombre = new SimpleStringProperty();
        email = new SimpleStringProperty();
        this.codigo = Integer.toString(anioActual) + Integer.toString(nCandidatos++);
    }
    public Candidato(String nombre, String telefono, String email, ArrayList<String> titulaciones, ArrayList<Habilidad> habilidades, TipoJornada tipoJornada, double retribucion) {
        this();
        this.nombre.set(nombre);
        this.telefono = telefono;
        this.email.set(email);
        this.titulaciones = titulaciones;
        this.habilidades = habilidades;
        this.tipoJornada = tipoJornada;
        this.retribucion = retribucion;
    }
    public SimpleStringProperty nombreProperty()
    {
        return nombre;
    }
    public SimpleStringProperty emailProperty()
    {
        return email;
    }
    public String convertirAString()
    {
        String linea = "";
        linea += nombre + ";";
        linea += email + ";";
        linea += retribucion + ";";
        linea += tipoJornada.toString() + ";";
        for (int i = 0; i < titulaciones.size() - 1; i++) {
            linea += titulaciones.get(i) + ",";
        }
        linea += titulaciones.get(titulaciones.size() - 1) + ";";
        for (int i = 0; i < habilidades.size() - 1; i++) {
            linea += habilidades.get(i).habilidad  + "/" + habilidades.get(i).nivel + ",";
        }
        linea += habilidades.get(habilidades.size() - 1).habilidad  + "/" + habilidades.get(habilidades.size() - 1).nivel;
        return linea;
    }
    public String getCodigo()
    {
        return codigo;
    }
    
    public void addHabilidad(Habilidad h)
    {
        habilidades.add(h);
    }
    public void removeHabilidad(Habilidad h)
    {
        habilidades.remove(h);
    }
    public Habilidad getHabilidad(int pos)
    {
        return habilidades.get(pos);
    }
    public void addTitulacion(String t)
    {
        titulaciones.add(t);
    }
    public String getTitulacion(int pos)
    {
        return titulaciones.get(pos);
    }
    public void setTipoJornada(TipoJornada tipoJornada)
    {
        this.tipoJornada = tipoJornada;
    }
    
    public String getNombre()
    {
        return this.nombre.get();
    }

    public void setNombre(String nombre)
    {
        this.nombre.set(nombre);
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getEmail()
    {
        return email.get();
    }

    public void setEmail(String email)
    {
        this.email.set(email);
    }

    public TipoJornada getTipoJornada()
    {
        return tipoJornada;
    }
}
