package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento=elemento; 
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente!=null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(siguiente==null)
                throw new NoSuchElementException();

            anterior=siguiente;
            siguiente=siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior!=null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(anterior==null)
                throw new NoSuchElementException();

            siguiente=anterior;
            anterior=anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior=null;
            siguiente=cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior=rabo;
            siguiente=null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return getLongitud()==0;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException();
        
        agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException();

        Nodo buffer=new Nodo(elemento);
        if(getLongitud()<1){
            cabeza=rabo=buffer;
        }

        else{
            rabo.siguiente=buffer;
            buffer.anterior=rabo;
            rabo=buffer;
        }
        longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException();

        Nodo buffer=new Nodo(elemento);

        if(getLongitud()<1){
            cabeza=rabo=buffer;
        }

        else{
            cabeza.anterior=buffer;
            buffer.siguiente=cabeza;
            cabeza=buffer;
        }
        longitud++; 
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException();
        
        if(getLongitud()==0){
            agregaInicio(elemento);
            return;
        }

        if(i<1){
            agregaInicio(elemento);
            return;
        }

        if(i>(getLongitud()-1)){
            agregaFinal(elemento);
            return;
        }

        longitud++;
        Nodo n=new Nodo(elemento);
        Nodo s=this.getIndex(i);
        Nodo a=s.anterior;
        n.anterior=a;
        a.siguiente=n;
        n.siguiente=s;
        s.anterior=n;
    }

    /**
     * Metodo auxiliar que regresa el j-ésimo nodo de una lista
     * @param j indice
     * @return ret nodo j-ésimo
     * */
    private Nodo getIndex(int i){
        int j=0;
        Nodo ret=cabeza;
        while(j++<i){
            ret=ret.siguiente;
        }

        return ret;
    }
    

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo nodo=encuentra(cabeza, elemento);

        if(nodo==null)
            return;

        else if(cabeza==rabo){
            cabeza=rabo=null;
        }

        else if(cabeza==nodo){
            cabeza=cabeza.siguiente;
            cabeza.anterior=null;
        }

        else if(rabo==nodo){
            rabo=rabo.anterior;
            rabo.siguiente=null;
        }

        else{
            nodo.siguiente.anterior=nodo.anterior;
            nodo.anterior.siguiente=nodo.siguiente;
        }

        longitud--;
    }

    /**
     * Metodo auxiliar que regresa el primer nodo de la lista en 
     * en que está el elemento T
     * @param elemento el elemento que se buscará en la lista
     * @return n el primer nodo de la lista en el que se encuentra el 
     * elemento
     * */
    private Nodo encuentra(Nodo n, T elemento){
        if(n==null)
            return null;
        if(n.elemento.equals(elemento))
            return n;

        return encuentra(n.siguiente, elemento);
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if(getLongitud()<1)
            throw new NoSuchElementException();

        T buffer=cabeza.elemento;

        if(getLongitud()==1){
            cabeza=null;
            rabo=null;
        }

        else{
            cabeza=cabeza.siguiente;
            cabeza.anterior=null;
        }
        longitud--;
        return buffer;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(getLongitud()<1)
            throw new NoSuchElementException();

        T buffer=rabo.elemento;
        if(getLongitud()==1){
            cabeza=null;
            rabo=null;
        }

        else{
            rabo=rabo.anterior;
            rabo.siguiente=null;
        }
        longitud--;
        return buffer;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return encuentra(cabeza, elemento)!=null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> ret=new Lista<>();
        if(cabeza==null)
            return ret;

        if(cabeza.siguiente==null){
            ret.agregaInicio(cabeza.elemento);
            return ret;
        }

        Nodo n=cabeza;
        while(n!=null){
            ret.agregaInicio(n.elemento);
            n=n.siguiente;
        }

        return ret;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> ret=new Lista<>();
        if(cabeza==null)
            return ret;

        if(cabeza.siguiente==null){
            ret.agregaInicio(cabeza.elemento);
            return ret;
        }

        Nodo n=cabeza;
        while(n!=null){
            ret.agregaFinal(n.elemento);
            n=n.siguiente;
        }

        return ret;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza=null;
        rabo=null;
        longitud=0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if(getLongitud()<1)
            throw new NoSuchElementException();

        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if(getLongitud()<1)
            throw new NoSuchElementException();

        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if(i<0 || i>=getLongitud())
            throw new ExcepcionIndiceInvalido();

        return this.getIndex(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        if(cabeza==null)
            return -1;

        //if(cabeza.elemento=elemento)
          //  return 0;

        int ret=0;
        Nodo n=cabeza;
        while(n!=null){
            if(n.elemento.equals(elemento))
                return ret;

            ret++;
            n=n.siguiente;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if(getLongitud()==0)
            return "[]";
        
        String ret="[";

        Nodo n=cabeza;
        while(n!=null){
            ret+=String.format("%d, ", n.elemento);
            n=n.siguiente;
        }

        return ret.substring(0, ret.length()-2)+"]";
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if(getLongitud()!=lista.getLongitud())
            return false;

        Nodo x=cabeza;
        Nodo y=lista.cabeza;
        while(x!=null && y!=null){
            if(!x.elemento.equals(y.elemento))
                return false;

            x=x.siguiente;
            y=y.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        if(longitud<2)
            return copia();
        
        Lista<T> l1= new Lista<>();
        Lista<T> l2= new Lista<>();
        int i=0;
        Nodo n=cabeza;
        while(i<this.longitud){
            if(i<this.longitud/2){
                l1.agrega(n.elemento);
                n=n.siguiente;
            }
            else{
                l2.agrega(n.elemento);
                n=n.siguiente;
            }
            i++;
        }

        return mezcla(l1.mergeSort(comparador), l2.mergeSort(comparador), comparador);
    }
    
    /**
     * Metodo privado que recibe dos listas ya ordenadas y regresa una lista
     * ordenada que tiene todos los elementos de la lista de entrada
     * @param l1 primera lista de entrada a mezclar
     * @param l2 segunda lista de entrada a mezclar
     * @param comparador el comparador que se usara para hacer el ordenamiento
     * @return una copia de la lista, pero ordenada.
     */
    private Lista<T> mezcla(Lista<T> l1, Lista<T> l2, Comparator<T> c){
        Lista<T> ret=new Lista<>();
        Nodo n=l1.cabeza;
        Nodo m=l2.cabeza;
        while(n!=null && m!=null){
            if(c.compare(n.elemento, m.elemento)<=0){
                ret.agrega(n.elemento);
                n=n.siguiente;
            }
            else{
                ret.agrega(m.elemento);
                m=m.siguiente;
            }
        }

        Nodo u= (n==null) ? m: n;
        while(u!=null){
            ret.agrega(u.elemento);
            u=u.siguiente;
        }
        return ret;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo n=cabeza;
        while(n!=null){
            if(comparador.compare(n.elemento, elemento)==0)
                return true;
            n=n.siguiente;
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
