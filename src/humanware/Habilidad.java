package humanware;

import humanware.utilidades.ListaEnlazada;
import humanware.utilidades.Utilidades;

public class Habilidad
{
    public String habilidad;
    public int nivel;
    public Habilidad(String habilidad, int nivel)
    {
        this.habilidad = habilidad;
        this.nivel = nivel;
    }
    public static ListaEnlazada<Habilidad> convertirAHabilidades(String linea)
    {
        ListaEnlazada<String> habilidades = Utilidades.split(linea, ",");
        ListaEnlazada<Habilidad> habilidadesFinal = new ListaEnlazada<>();
        for (String habilidad : habilidades)
        {
            ListaEnlazada<String> campos = Utilidades.split(habilidad, "/");
            if (campos.size() > 1)
            {
                String nombre = campos.get(0);
                int nivel = Integer.parseInt(campos.get(1));
                habilidadesFinal.addFinal(new Habilidad(nombre, nivel));
            }
        }
        return habilidadesFinal;
    }
    public static String convertirString(ListaEnlazada<Habilidad> lista)
    {
        String linea = "";
        int i;
        for (i = 0; i < lista.size() - 1; i++) {
            linea += lista.get(i).habilidad + "/" + lista.get(i).nivel + ",";
        }
        return linea += lista.get(lista.size() - 1).habilidad + "/" + lista.get(lista.size() - 1).nivel;
    }
}
