package humanware;

import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.Utilidades;

public class TitulacionEmpresa
{
    public String titulacion;
    public int importancia;

    public TitulacionEmpresa(String titulacion, int importancia)
    {
        this.titulacion = titulacion;
        this.importancia = importancia;
    }
    public static ListaEnlazada<TitulacionEmpresa> convertirATitulaciones(String linea)
    {
        ListaEnlazada<String> titulaciones = Utilidades.split(linea, ",");
        ListaEnlazada<TitulacionEmpresa> titulacionFinal = new ListaEnlazada<>();
        for (String habilidad : titulaciones)
        {
            String nombre = Utilidades.split(habilidad, "/").get(0);
            int importancia = Integer.parseInt(Utilidades.split(habilidad, "/").get(1));
            titulacionFinal.addFinal(new TitulacionEmpresa(nombre, importancia));
        }
        return titulacionFinal;
    }
    
    public static String convertirString(ListaEnlazada<TitulacionEmpresa> lista)
    {
        String linea = "";
        int i;
        for (i = 0; i < lista.size() - 1; i++) {
            linea += lista.get(i).titulacion + "/" + lista.get(i).importancia + ",";
        }
        return linea += lista.get(lista.size() - 1).titulacion + "/" + lista.get(lista.size() - 1).importancia;
    }
}
