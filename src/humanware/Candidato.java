package humanware;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Candidato
{
    private final String codigo;
    private String nombre;
    private String telefono;
    private String email;
    private ArrayList<String> titulaciones;
    private ArrayList<Habilidad> habilidades;
    private TipoJornada tipoJornada;
    private final int anioActual = LocalDateTime.now().getYear();
    private static int nCandidatos = 0;
    
    public Candidato(String nombre)
    {
        this.nombre = nombre;
        this.codigo = Integer.toString(anioActual) + Integer.toString(nCandidatos++);
    }
    
    public String getCodigo()
    {
        return codigo;
    }
    
    public Candidato(String nombre, String telefono)
    {
        this(nombre);
        this.telefono = telefono;
    }
    
    public Candidato(String nombre, String telefono, String email)
    {
        this(nombre, telefono);
        this.email = email;
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
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
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
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public TipoJornada getTipoJornada()
    {
        return tipoJornada;
    }
}
