package mx.unam.ciencias.edd;

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
        if(getLongitud()<1)
            cabeza=rabo=buffer;

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
        if(getLongitud()<1)
             cabeza=rabo=buffer;
        

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
     * Metodo auxiliar que regresa el j-esimo nodo de una lista
     * @param j indice
     * @return ret nodo j-esimo*/

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
    @Override public void elimina(T elemento){
        Nodo nodo=encuentra(cabeza,elemento);
        
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
     * Metodo auxiliar que regresa el primer nodo de la lista
     * en el que está el elemento T.
     * @param elemento el elemento que se buscará en la lista
     * @return n el primer nodo de la lista en el que se encuentra elemento 
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
    public Lista<T> reversa(){
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
        if(i<0 || i>= getLongitud())
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

        if(cabeza.elemento==elemento)
            return 0;

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
        if(getLongitud()==1 && cabeza.elemento!=null){
            ret+=String.format("%d", cabeza.elemento);
            ret+="]";
            return ret;     
        }
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
}
