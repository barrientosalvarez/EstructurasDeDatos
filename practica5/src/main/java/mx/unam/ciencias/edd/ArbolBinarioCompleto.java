package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            this.cola=new Cola<>();
            if(!esVacia())
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            if(cola==null)
                throw new java.util.NoSuchElementException();
            
            Vertice v=cola.saca();

            if(v.izquierdo!=null)
                cola.mete(v.izquierdo);

            if(v.derecho!=null)
                cola.mete(v.derecho);
            
            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException();
        
        Vertice v=new Vertice(elemento);
        elementos++;
        if(raiz==null)
            raiz=v;
        
        else{
            Cola<Vertice> cola=new Cola<>();
            cola.mete(raiz);
            while(!cola.esVacia()){
                Vertice h=cola.saca();
                if(h.izquierdo==null){
                    h.izquierdo=v;
                    v.padre=h;
                    return;
                }

                if(h.derecho==null){
                    h.derecho=v;
                    v.padre=h;
                    return;
                }
                cola.mete(h.izquierdo);
                cola.mete(h.derecho);
            }
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v=vertice(busca(elemento));
        if(raiz==null || v==null || elemento==null)
            return;

        elementos--;
        if(elementos==0){
            raiz=null;
            return;
        }

        Vertice ult=vertice(ultimoVertice());
        if(ult.padre.izquierdo==ult){
            intercambia(v, ult);
            ult.padre.izquierdo=null;
            return;
        }

        if(ult.padre.derecho==ult){
            intercambia(v, ult);
            ult.padre.derecho=null;
            return;
        }
    }

    /**
     * Metodo privado que devuelve el ultimo vertice de un arbol haciendo un 
     * recorrido BFS
     */
    private Vertice ultimoVertice(){
        Cola<Vertice> cola=new Cola<>();
        cola.mete(raiz);
        Vertice v=raiz;
        
        while(!cola.esVacia()){
            v=cola.saca();
            if(v.izquierdo!=null)
                cola.mete(v.izquierdo);
            if(v.derecho!=null)
                cola.mete(v.derecho);
        }
        return v;
    }
    
    /**
     * Metodo privado que intercambia los elementos de dos vertices 
     * de un arbol binario
     * @param x el primer vertice con a intercambiar
     * @param y el segundo vertice con a intercambiar
     */
    private void intercambia(Vertice x, Vertice y){
        T t=x.elemento;
        x.elemento=y.elemento;
        y.elemento=t;
    }
    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        return super.altura();
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if(raiz==null)
            return;

        Cola<Vertice> cola=new Cola<>();
        cola.mete(raiz);
        while(!cola.esVacia()){
            Vertice v=cola.saca();
            accion.actua(v);
            if(v.izquierdo!=null)
                cola.mete(v.izquierdo);
            if(v.derecho!=null)
                cola.mete(v.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
