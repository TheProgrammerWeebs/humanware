package humanware.utilidades;

import static humanware.utilidades.Utilidades.ES;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rango
{
    public double min;
    public double max;
    
    /**
     * Crea un rango desde 0 hasta max
     * @param max 
     */
    public Rango (double max)
    {
        this(0, max);
    }
    
    /**
     * Crea un rango con números desde min hasta max
     * @param min
     * @param max 
     */
    public Rango(double min, double max)
    {
        this.min = min;
        this.max = max;
    }
    
    public static Rango convertirARango(String linea)
    {
        try {
            ListaEnlazada<String> limites = Utilidades.split(linea, ",");
            Number limite1 = NumberFormat.getInstance(ES).parse(limites.get(0));
            Number limite2 = NumberFormat.getInstance(ES).parse(limites.get(1));
            return new Rango(limite1.doubleValue(), limite1.doubleValue());
        } catch (ParseException ex) {
            System.err.println("Problema con el formato del número");
            return new Rango();
        }
    }

    public Rango() {
        
    }
    public String mostrar()
    {
        return min + "," + max;
    }
    
}
