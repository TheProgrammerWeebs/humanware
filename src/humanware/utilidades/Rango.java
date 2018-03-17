package humanware.utilidades;

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
        ListaEnlazada<String> limites = Utilidades.split(linea, ",");
        return new Rango(Double.parseDouble(limites.get(0)), Double.parseDouble(limites.get(1)));
    }

    public Rango() {
        
    }
    public String mostrar()
    {
        return min + "," + max;
    }
    
}
