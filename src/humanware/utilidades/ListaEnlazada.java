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

    /**
     * Añade un nodo con determinada información a la lista, de forma ordenada
     *
     * @param info la información que se va a añadir
     * @param comparador instancia de una clase que implemente a ComparadorNodos
     */
    public void addOrdenado(T info, ComparadorNodos comparador) {
        observableListAsociada.add(info);
        Nodo nuevo = new Nodo(info);
        for (int i = 0; i < size; i++) {
            if (comparador.compararCon(this.get(i), this.get(i + 1)) > 0) {
                System.out.println("Lista no ordenada");
                return; //La lista no está ordenada; no se añade nada
            }
        }
        if (primerNodo == null) {
            primerNodo = nuevo;
        } else {
            Nodo<T> anterior = null;
            Nodo<T> actual = primerNodo;
            while (comparador.compararCon(actual.informacion, info) < 0 && actual.link != null) {
                actual = actual.link;
                anterior = actual;
            }
            if (comparador.compararCon(info, actual.informacion) == 0) {
                JOptionPane.showMessageDialog(null, "No se aceptan datos repetidos");
            } else {
                if (comparador.compararCon(actual.informacion, info) > 0) {
                    if (comparador.compararCon(actual.informacion, info) == 0) {
                        nuevo.link = primerNodo;
                        primerNodo = nuevo;
                    } else if (anterior != null) {
                        anterior.link = nuevo;
                        nuevo.link = actual;
                    }
                } else {
                    actual.link = nuevo;
                    nuevo.link = null;
                }
            }
        }

        size++;
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
        System.out.println("primerNodo.getInformacion() = " + primerNodo.getInformacion());
        if (primerNodo.getInformacion().equals(info)) {
            primerNodo = primerNodo.link;
        } else {
            Nodo anterior = primerNodo;
            Nodo actual = anterior.link;
            System.out.println("actual = " + actual);
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
        return getNodo(index).getInformacion();
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
    public void imprimir()
    {
        for (T t : this) {
            System.out.print(t + " ");
        }
        System.out.println();
    }
}
