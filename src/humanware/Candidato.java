package humanware;

import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.Utilidades;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Candidato
{
    private double retribucion;
    private SimpleIntegerProperty puntuacion;
    private SimpleStringProperty email;
    private SimpleStringProperty nombre;
    private String telefono;
    private TipoJornada tipoJornada;
    private ListaEnlazada<String> titulaciones;
    private ListaEnlazada<Habilidad> habilidades;
    private ListaEnlazada<Vacante> vacantes;

    public static final int NOMBRE = 0;
    public static final int TELEFONO = 1;
    public static final int EMAIL = 2;
    public static final int RETRIBUCION = 3;
    public static final int JORNADA = 4;
    public static final int TITULACIONES = 5;
    public static final int HABILIDADES = 6;
    public static final int VACANTES = 7;

    public Candidato() {
        vacantes = new ListaEnlazada<>();
        nombre = new SimpleStringProperty();
        email = new SimpleStringProperty();
        puntuacion = new SimpleIntegerProperty();
    }

    public Candidato(String nombre, String telefono, String email, ListaEnlazada<String> titulaciones, ListaEnlazada<Habilidad> habilidades, TipoJornada tipoJornada, double retribucion) {
        this();
        this.nombre.set(nombre);
        this.telefono = telefono;
        this.email.set(email);
        this.titulaciones = titulaciones;
        this.habilidades = habilidades;
        this.tipoJornada = tipoJornada;
        this.retribucion = retribucion;
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }
    
    public SimpleIntegerProperty puntuacionProperty()
    {
        return puntuacion;
    }

    public String convertirAString() {
        String linea = "";
        linea += nombre.get() + ";";
        linea += Utilidades.quitarEspacios(telefono) + ";";
        linea += email.get() + ";";
        linea += retribucion + ";";
        linea += tipoJornada + ";";
        titulaciones.imprimir();
        for (int i = 0; i < titulaciones.size(); i++) 
            if (i == titulaciones.size() - 1) linea += titulaciones.get(i) + ";";
            else linea += titulaciones.get(i) + ",";
        linea += Habilidad.convertirString(habilidades) + ";";
        for (int i = 0; i < vacantes.size(); i++)
            if (i == vacantes.size() - 1) linea += vacantes.get(i);
            else linea += vacantes.get(i) + ",";
        return linea;
    }

    public int getPuntuacion() {
        return puntuacion.get();
    }

    public void setPuntuacion(int puesto) {
        this.puntuacion.set(puesto);
    }

    public double getRetribucion() {
        return retribucion;
    }

    public void imprimir() {
        System.out.println("Nombre: " + nombre.get());
        System.out.println("Email: " + email.get());
        System.out.println("Telefono: " + telefono);
        System.out.print("Titulaciones: ");
        titulaciones.imprimir();
        System.out.print("Habilidades: ");
        for (Habilidad h : habilidades) {
            System.out.print(h.habilidad + " ");
        }
        System.out.println();
        System.out.println("Retribución: " + retribucion);
        System.out.println("Tipo Jornada: " + tipoJornada);
    }

    public void setRetribucion(double retribucion) {
        this.retribucion = retribucion;
    }

    public ListaEnlazada<String> getTitulaciones() {
        return titulaciones;
    }

    public void setTitulaciones(ListaEnlazada<String> titulaciones) {
        this.titulaciones = titulaciones;
    }

    public ListaEnlazada<Habilidad> getHabilidades() {
        return habilidades;
    }

    public static Candidato parseCandidato(String linea) {
        ListaEnlazada<String> campos = Utilidades.split(linea, ";");
        ListaEnlazada<String> titulaciones = Utilidades.split(campos.get(Candidato.TITULACIONES), ",");
        ListaEnlazada<Habilidad> habilidades = Habilidad.convertirAHabilidades(campos.get(Candidato.HABILIDADES));
        ListaEnlazada<Vacante> vacantes = new ListaEnlazada<>();
        Candidato candidato = new Candidato();
        candidato.setNombre(campos.get(Candidato.NOMBRE));
        candidato.setEmail(campos.get(Candidato.EMAIL));
        candidato.setTelefono(campos.get(Candidato.TELEFONO));
        candidato.setTitulaciones(titulaciones);
        candidato.setHabilidades(habilidades);
        candidato.setTipoJornada(TipoJornada.convertirAJornada(campos.get(Candidato.JORNADA)));
        candidato.setRetribucion(Double.parseDouble(campos.get(Candidato.RETRIBUCION)));
        candidato.setVacantes(Vacante.convertirCodigosAVacantes(campos.get(Candidato.VACANTES)));
        return candidato;
    }

    public void setHabilidades(ListaEnlazada<Habilidad> habilidades) {
        this.habilidades = habilidades;
    }
    
    public void setVacantes(ListaEnlazada<Vacante> vacantes) {
        this.vacantes = vacantes;
    }

    public void addHabilidad(Habilidad h) {
        habilidades.addFinal(h);
    }

    public void removeHabilidad(Habilidad h) {
        habilidades.remove(h);
    }

    public Habilidad getHabilidad(int pos) {
        return habilidades.get(pos);
    }

    public void addTitulacion(String t) {
        titulaciones.addFinal(t);
    }
    
    public void addVacante(Vacante v)
    {
        vacantes.addFinal(v);
    }
    
    public Vacante getVacante(int index)
    {
        return vacantes.get(index);
    }

    public String getTitulacion(int pos) {
        return titulaciones.get(pos);
    }

    public void setTipoJornada(TipoJornada tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    public String getNombre() {
        return this.nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public TipoJornada getTipoJornada() {
        return tipoJornada;
    }
    
    public ListaEnlazada<Vacante> getVacantes()
    {
        return vacantes;
    }
}
