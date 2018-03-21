package humanware.utilidades;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class ListaEnlazada<T> implements Iterable<T>
{

    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    //<editor-fold defaultstate="collapsed" desc="Implementación de Iterator">
    private class IteradorLista implements Iterator<T>
    {

        int actual = 0;

        @Override
        public boolean hasNext() {
            if (actual < ListaEnlazada.this.size) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return get(actual++);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Clase Nodo">
    public class Nodo<T>
    {

        private T informacion;
        private Nodo link;

        public Nodo(T informacion) {
            this.informacion = informacion;
            this.link = null;
        }

        public T getInformacion() {
            return informacion;
        }

        public void setInformacion(T informacion) {
            this.informacion = informacion;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Interface comparador nodos">
    public interface ComparadorNodos<T>
    {

        /**
         * Compara dos nodos
         *
         * @param a el primer nodo
         * @param b el segundo nodo
         * @return un número mayor a 0, si a > b. Un número menor a 0, si a menor que b. 0, si a = b
         */
        int compararCon(T a, T b);
    }
    //</editor-fold>
    private Nodo<T> ultimoNodo;
    private Nodo<T> primerNodo;
    private int size;
    private ObservableList<T> observableListAsociada;

    public ListaEnlazada() {
        size = 0;
        primerNodo = null;
        ultimoNodo = null;
        observableListAsociada = FXCollections.observableArrayList();
    }

    public int size() {
        return size;
    }

    public void setObservableListAsociada(ObservableList<T> observableListAsociada) {
        this.observableListAsociada = observableListAsociada;
    }

    public static <T> void ordenar (ObservableList<T> lista, ComparadorNodos<T> comparador)
    {
        boolean swap = true;
        int j = 0;
        T temp;
        while(swap)
        {
            swap = false;
            j++;
            for (int i = 0; i < lista.size() - j; i++) {
                if (comparador.compararCon(lista.get(i), lista.get(i+1)) < 0)
                {
                    temp = lista.get(i);
                    lista.set(i, lista.get(i+1));
                    lista.set(i+1, temp);
                    swap = true;
                }
            }
        }
    }
    
    
    public void bubbleSort(ComparadorNodos<T> comparador)
    {
        boolean swap = true;
        ordenar(getObservableListAsociada(), comparador);
        int j = 0;
        T temp;
        while(swap)
        {
            swap = false;
            j++;
            for (int i = 0; i < size() - j; i++) {
                if (comparador.compararCon(get(i), get(i+1)) < 0)
                {
                    temp = get(i);
                    set(i, get(i+1));
                    set(i+1, temp);
                    swap = true;
                }
            }
        }
    }

    public boolean existe(T informacion) {
        for (T elemento : this) {
            if (elemento.equals(informacion)) {
                return true;
            }
        }
        return false;
    }

    public void addFinal(T informacion) {
        observableListAsociada.add(informacion);
        Nodo<T> p = new Nodo(informacion);
        if (primerNodo == null) {
            primerNodo = p;
        } else {
            ultimoNodo.link = p;
        }
        ultimoNodo = p;
        size++;
    }

    public void addPrincipio(T informacion) {
        observableListAsociada.add(informacion);
        Nodo<T> p = new Nodo(informacion);
        if (primerNodo == null) {
            ultimoNodo = p;
        } else {
            p.link = primerNodo;
        }
        primerNodo = p;
        size++;
    }

    public boolean remove(T info) {
        observableListAsociada.remove(info);
        if (primerNodo.getInformacion().equals(info) && primerNodo.informacion == null) {
            primerNodo = primerNodo.link;
        } else {
            Nodo anterior = primerNodo;
            Nodo actual = anterior.link;
            this.imprimir();
            while (!actual.informacion.getClass().equals(info)) {
                anterior = anterior.link;
                actual = actual.link;
                if (actual == null) {
                    return false;
                }
            }
            anterior.link = actual.link;
            size--;
        }
        return true;
    }

    public boolean remove(int index) {
        observableListAsociada.remove(getNodo(index).getInformacion());
        Nodo<T> actual = getNodo(index);
        if (actual == null) {
            return false;
        }
        if (index == 0) {
            primerNodo = primerNodo.link;
        } else {
            Nodo anterior = getNodo(index - 1);
            anterior.link = actual.link;
            size--;
            return true;
        }
        return false;
    }

    public Nodo<T> getNodo(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ". Size: " + size);
        }
        Nodo<T> p = primerNodo;
        for (int i = 0; i < index; i++) {
            p = p.link;
        }
        return p;
    }

    public T get(int index) {
        Nodo<T> n = getNodo(index);
        if (n == null) return null;
        return n.getInformacion();
    }

    public boolean estaVacia() {
        return primerNodo == null;
    }

    public void vaciar() {
        primerNodo = null;
        observableListAsociada.clear();
    }

    public void set(int index, T info) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites. Índice: " + index + ", Tamaño: " + size);
        }
        getNodo(index).informacion = info;
    }

    public ObservableList<T> getObservableListAsociada() {
        return observableListAsociada;
    }

    public void imprimir() {
        for (T t : this) {
            System.out.print(t + " ");
        }
        System.out.println();
    }
}
