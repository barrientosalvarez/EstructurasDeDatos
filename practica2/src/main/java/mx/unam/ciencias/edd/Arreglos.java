package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        quickSort(arreglo, 0, arreglo.length-1, comparador);
    }

    /**
     * Metodo que ordena un arreglo usando Quicksort
     * @param arr el arreglo a ordenar
     * @param a el indice del extremo izquierdo del arreglo
     * @param b el indice del extremo derecho del arreglo
     *
     */
    private static <T> void quickSort(T[] arr, int a, int b, Comparator<T> c){
        if(b<=a)
            return;
        int i=a+1;
        int j=b;
        
        while(i<j)
            if(c.compare(arr[i], arr[a])>0 && c.compare(arr[j], arr[a])<=0){
                intercambia(arr, i++, j--);
            }
            else if(c.compare(arr[i], arr[a])<=0)
                i++;
            else
                j--;
        if(c.compare(arr[i],arr[a])>0)
            i--;
        intercambia(arr, a, i);
        quickSort(arr, a, i-1, c);
        quickSort(arr, i+1, b, c);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Metodo auxiliar que intercambia los elementos i-esimo y m-esimo de
     * un arreglo
     * @param arreglo el arreglo en el que se intercambiaran los elementos
     * @param i el indice de uno de los elementos a intercambiar
     * @param m el indice del segundo elemento a intercambiar
     */
    private static <T> void intercambia(T[] arreglo, int i, int m){
        if(i==m)
            return;
        T buffer=arreglo[m];
        arreglo[m]=arreglo[i];
        arreglo[i]=buffer;
    }


    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for(int i=0; i<arreglo.length-1; i++){
            int m=i;
            for(int j=i+1; j<arreglo.length; j++)
                if(comparador.compare(arreglo[j],arreglo[m])<0)
                    m=j;
                    
            intercambia(arreglo, i, m);
        }

    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        return busquedaBinaria(arreglo, elemento, 0, arreglo.length-1, comparador);
    }
    /**
     * Metodo que hace una busqueda binaria en el arreglo
     * @param arr arreglo en el que se hará la búsqueda
     * @param e el elemento a buscar
     * @param a extremo izquierdo del arreglo
     * @param b extremo derecho del arreglo
     * @return el indice del elemento del arreglo, o -1 si no se encuentra.
     */
    private static <T> int busquedaBinaria(T[] arr, T e, int a, int b, Comparator<T> c){
        if(a>b)
            return -1;
        int micha=(a+b)/2;
        
        if(c.compare(arr[micha], e)==0)
            return micha;

        if(c.compare(e, arr[micha])<0)
            return busquedaBinaria(arr, e, a, micha-1, c);

        return busquedaBinaria(arr, e, micha+1, b, c);
    }


    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
