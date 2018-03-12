package humanware;

import humanware.utilidades.Utilidades;
import java.util.ArrayList;

public class Habilidad
{
    public String habilidad;
    public int nivel;
    public Habilidad(String habilidad, int nivel)
    {
        this.habilidad = habilidad;
        this.nivel = nivel;
    }
    public static ArrayList<Habilidad> convertirAHabilidades(String linea)
    {
        ArrayList<String> habilidades = Utilidades.split(linea, ",");
        ArrayList<Habilidad> habilidadesFinal = new ArrayList<>();
        for (String habilidad : habilidades)
        {
            ArrayList<String> campos = Utilidades.split(habilidad, "/");
            if (campos.size() > 1)
            {
                String nombre = campos.get(0);
                int nivel = Integer.parseInt(campos.get(1));
                habilidadesFinal.add(new Habilidad(nombre, nivel));
            }
        }
        return habilidadesFinal;
    }
    public static String convertirString(ArrayList<Habilidad> lista)
    {
        String linea = "";
        int i;
        for (i = 0; i < lista.size() - 1; i++) {
            linea += lista.get(i).habilidad + "/" + lista.get(i).nivel + ",";
        }
        return linea += lista.get(lista.size() - 1).habilidad + "/" + lista.get(lista.size() - 1).nivel;
    }
}
