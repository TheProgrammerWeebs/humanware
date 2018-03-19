package humanware;

import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.Rango;
import humanware.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;

public class Vacante
{
    private boolean evaluada;
    private String codigo;
    private Empresa empresa;
    private Rango salario;
    private TipoJornada tipoJornada;
    private ListaEnlazada<TitulacionEmpresa> titulaciones;
    private ListaEnlazada<Habilidad> habilidades;
    private ListaEnlazada<Candidato> aptos;
    private final SimpleStringProperty nombreEmpresa;
    private final SimpleStringProperty descripcionPuesto;
    public static final int EMPRESA = 0;
    public static final int DESCRIPCION = 1;
    public static final int SALARIO = 2;
    public static final int JORNADA = 3;
    public static final int TITULACIONES = 4;
    public static final int HABILIDADES = 5;
    public static final int ESTA_EVALUADA = 6;

    public Vacante(String descripcion, Empresa empresa) {
        this();
        this.empresa = empresa;
    }

    public Vacante() {
        evaluada = false;
        nombreEmpresa = new SimpleStringProperty();
        descripcionPuesto = new SimpleStringProperty();
        salario = new Rango();
        empresa = new Empresa();
        titulaciones = new ListaEnlazada();
        habilidades = new ListaEnlazada();
        aptos = new ListaEnlazada();
    }

    public Vacante(Rango salario, TipoJornada tipoJornada, ListaEnlazada<TitulacionEmpresa> titulaciones, ListaEnlazada<Habilidad> habilidades, String nombreEmpresa, String descripcionPuesto) {
        this.salario = salario;
        this.tipoJornada = tipoJornada;
        this.titulaciones = titulaciones;
        this.habilidades = habilidades;
        this.nombreEmpresa = new SimpleStringProperty(nombreEmpresa);
        this.descripcionPuesto = new SimpleStringProperty(descripcionPuesto);
    }

    public SimpleStringProperty nombreEmpresaProperty() {
        return nombreEmpresa;
    }

    public SimpleStringProperty descripcionPuestoProperty() {
        return this.descripcionPuesto;
    }

    public String getDescripcion() {
        return descripcionPuesto.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcionPuesto.set(descripcion);
    }

    public String getNombreEmpresa() {
        return nombreEmpresa.get();
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.empresa.setNombre(nombreEmpresa);
        this.nombreEmpresa.set(nombreEmpresa);
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        this.nombreEmpresa.set(empresa.getNombre());
    }

    public Rango getSalario() {
        return salario;
    }

    public void setSalario(Rango salario) {
        this.salario = salario;
    }

    public TipoJornada getTipoJornada() {
        return tipoJornada;
    }

    public void setTipoJornada(TipoJornada tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    public ListaEnlazada<TitulacionEmpresa> getTitulaciones() {
        return titulaciones;
    }

    public void setTitulaciones(ListaEnlazada<TitulacionEmpresa> titulaciones) {
        this.titulaciones = titulaciones;
    }

    public ListaEnlazada<Habilidad> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(ListaEnlazada<Habilidad> habilidades) {
        this.habilidades = habilidades;
    }

    public String convertirString() {
        System.out.println("habilidades = " + habilidades);
        return nombreEmpresa.get() + ";"
                + getDescripcion() + ";"
                + salario.mostrar() + ";"
                + this.getTipoJornada() + ";"
                + TitulacionEmpresa.convertirString(titulaciones) + ";"
                + (!habilidades.estaVacia() ? Habilidad.convertirString(habilidades) : "") + ";"
                + estaEvaluada();
    }

    public static Vacante convertirVacante(String linea) {
        Vacante v = new Vacante();
        ListaEnlazada<String> campos = Utilidades.split(linea, ";");
        v.setDescripcion(campos.get(Vacante.DESCRIPCION));
        v.setNombreEmpresa(campos.get(Vacante.EMPRESA));
        v.setHabilidades(Habilidad.convertirAHabilidades(campos.get(Vacante.HABILIDADES)));
        v.setSalario(Rango.convertirARango(campos.get(Vacante.SALARIO)));
        v.setTipoJornada(TipoJornada.convertirAJornada(campos.get(Vacante.JORNADA)));
        v.setTitulaciones(TitulacionEmpresa.convertirATitulaciones(campos.get(Vacante.TITULACIONES)));
        v.setEvaluada(Boolean.parseBoolean(campos.get(Vacante.ESTA_EVALUADA)));
        return v;
    }

    public void setAptos(ListaEnlazada<Candidato> aptos) {
        this.aptos = aptos;
    }

    public ListaEnlazada<Candidato> getAptos() {
        return aptos;
    }

    public void addApto(Candidato c) {
        aptos.addFinal(c);
    }

    public void removeApto(Candidato c) {
        aptos.remove(c);
    }

    public boolean estaEvaluada() {
        return evaluada;
    }

    public void setEvaluada(boolean evaluada) {
        this.evaluada = evaluada;
    }
}
