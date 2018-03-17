package humanware;

public enum TipoJornada
{
    COMPLETA,
    PARCIAL,
    AMBAS;
    public static TipoJornada convertirAJornada(String tipo)
    {
        return tipo.equals("COMPLETA") ? COMPLETA : (tipo.equals("PARCIAL") ? PARCIAL : AMBAS);
    }
}
