package humanware.utilidades;

public interface ComparadorNodos<E>
{
    /**
     * @param a
     * @param b 
     * @return = 0, si los objetos son iguales. 
     *         < 0, si el objeto es menor a b.
     *         > 0, si el objeto es mayor a b
     */
    int compararCon(E a, E b);
}
