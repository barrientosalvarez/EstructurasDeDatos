package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            pila=new Pila<Vertice>();
            Vertice v=raiz;
            while(v!=null){
                pila.mete(v);
                v=v.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice v=pila.saca();
            if(v.hayDerecho()){
                Vertice buff=v.derecho;
                while(buff!=null){
                    pila.mete(buff);
                    buff=buff.izquierdo;
                }
            }
            return v.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException();
        
        Vertice v=nuevoVertice(elemento);
        if(raiz==null){
            raiz=v;
            elementos=1;
            ultimoAgregado=v;
            return;
        }
        
        agrega(raiz, elemento);
    }

    /**
     * Metodo auxiliar recursivo que agrega un nuevo elemento al arbol.
     * @param v vertice actual distinto de <code>null</code>
     * @param e el elemento de el que tendra el nuevo vertice
     */
    private void agrega(Vertice v, T e){
        if(e.compareTo(v.elemento)>0 && v.derecho==null){
            Vertice buff=nuevoVertice(e);
            v.derecho=buff;
            buff.padre=v;
            elementos++;
            ultimoAgregado=buff;
            return;
        }

        if(e.compareTo(v.elemento)<=0 && v.izquierdo==null){
            Vertice buff=nuevoVertice(e);
            v.izquierdo=buff;
            buff.padre=v;
            elementos++;
            ultimoAgregado=buff;
            return;
        }

        if(e.compareTo(v.elemento)>0)
            agrega(v.derecho, e);
        if(e.compareTo(v.elemento)<=0)
            agrega(v.izquierdo, e);
    }
    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v=vertice(busca(elemento));
        if(v==null)
            return;

        if(v==raiz && elementos==1){
            raiz=null;
            elementos=0;
            return;
        }

        if(v.izquierdo==null && v.derecho==null){
            if(v.padre.izquierdo==v){
                v.padre.izquierdo=null;
                elementos--;
                return;
            }
            if(v.padre.derecho==v){
                v.padre.derecho=null;
                elementos--;
                return;
            }
        }
        
        if((v.izquierdo!=null && v.derecho==null) || (v.izquierdo==null && v.derecho!=null)){
            eliminaVertice(v);
            elementos--;
            return;
        }

        if(v.izquierdo!= null && v.derecho!=null){
            eliminaVertice(intercambiaEliminable(v));
            elementos--;
            return;
        }
            

    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        if(vertice.izquierdo==null || vertice.derecho==null)
            return null;

        Vertice v=maximoEnSubArbol(vertice.izquierdo);
        T e=vertice.elemento;
        vertice.elemento=v.elemento;
        v.elemento=e;
        return v;
    }

    /**
     * Metodo auxiliar que nos regresara el vertice cuyo elemento es el 
     * maximo de un sub arbol
     * @param v raiz del subarbol
     * @return el maximo del subarbol
     */
    private Vertice maximoEnSubArbol(Vertice v){
        if(v.derecho==null)
            return v;
        return maximoEnSubArbol(v.derecho);
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        Vertice p=vertice.padre;
        Vertice u=null;
        
        if(vertice.izquierdo!=null && vertice.derecho!=null)
            return;

        if(vertice.derecho!=null)
            u=vertice.derecho;
        
        if(vertice.izquierdo!=null)
            u=vertice.izquierdo;

        if(vertice.padre!=null){
            if(vertice.padre.izquierdo==vertice)
                vertice.padre.izquierdo=u;
            
            if(vertice.padre.derecho==vertice)
                vertice.padre.derecho=u;
        }

        if(vertice.padre==null)
            raiz=u;

        if(u!=null)
            u.padre=p;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        if(esVacia())
            return null;

        return busca(raiz, elemento);
    }
    
    /**
     * Busca un elemento en el arbol recorriendolo in-order. Si lo encuentra,
     * regresa el vertice que lo contiene; si no, regresa <code>null</code>
     * @param v el vertice desde el cual se comenzara a buscar (raiz)
     * @return un vercice que contiene al elemento buscado si lo encuentra,
     *         <code>null</code> en otro caso
     */
    private Vertice busca(Vertice v, T e){
        if(v==null || e==null)
            return null;

        if(v.elemento.equals(e))
            return v;
        
        if(v.elemento.compareTo(e)>0 && v.izquierdo!=null)
            return busca(v.izquierdo, e);

        if(v.elemento.compareTo(e)<0 && v.derecho!=null)
            return busca(v.derecho, e);
        
        return null;
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if(vertice==null || !vertice.hayIzquierdo())
            return;

        Vertice v=vertice(vertice);
        Vertice vIzq=v.izquierdo;
        vIzq.padre=v.padre;

        if(v!=raiz){
            if(v.padre.izquierdo==v)
                v.padre.izquierdo=vIzq;
            else
                v.padre.derecho=vIzq;
        } 

        else
            raiz=vIzq;

        v.izquierdo=vIzq.derecho;
        if(vIzq.derecho!=null)
            vIzq.derecho.padre=v;

        vIzq.derecho=v;
        v.padre=vIzq;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if(vertice==null || !vertice.hayDerecho())
            return;

        Vertice v=vertice(vertice);
        Vertice vDer=v.derecho;
        vDer.padre=v.padre;

        if(v!=raiz){
            if(v.padre.izquierdo==v)
                v.padre.izquierdo=vDer;
            else
                v.padre.derecho=vDer;
        }

        else
            raiz=vDer;

        v.derecho=vDer.izquierdo;
        if(vDer.izquierdo!=null)
            vDer.izquierdo.padre=v;

        vDer.izquierdo=v;
        v.padre=vDer;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        if(esVacia())
            return;
        
        dfsPreOrder(raiz, accion);
    }
    
    /**
     * Realiza un reccorido DFP <em>pre-oreder</em> en el arbol, ejecutando la
     * accion recibida en cada elemento del arbol
     * @param v vertice raiz
     * @param a accion a realizar
     */
    private void dfsPreOrder(Vertice v, AccionVerticeArbolBinario<T> a){
        a.actua(v);
        
        if(v.izquierdo!=null)
            dfsPreOrder(v.izquierdo, a);
        if(v.derecho!=null)
            dfsPreOrder(v.derecho, a);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        if(esVacia())
            return;
        dfsInOrder(raiz, accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param v vertice raiz
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    private void dfsInOrder(Vertice v, AccionVerticeArbolBinario<T> a){
        if(v.izquierdo!=null)
            dfsInOrder(v.izquierdo, a);

        a.actua(v);

        if(v.derecho!=null)
            dfsInOrder(v.derecho, a);

    }
    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        if(esVacia())
            return;
        dfsPostOrder(raiz, accion);
    }

    private void dfsPostOrder(Vertice v, AccionVerticeArbolBinario<T> a){
        if(v.izquierdo!=null)
            dfsPostOrder(v.izquierdo, a);
        if(v.derecho!=null)
            dfsPostOrder(v.derecho, a);

        a.actua(v);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
