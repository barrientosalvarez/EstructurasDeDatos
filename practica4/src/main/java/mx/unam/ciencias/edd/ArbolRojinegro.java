package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color=Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            return (color== Color.ROJO ? "R" : "N")+"{"+elemento.toString()+"}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return(color==vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        return turnRB(vertice).color;
    }

    /**
     * Metodo que hace una audicion de un VerticeArbolBinario a un
     * VerticeRojinegro
     * @param v VerticeArbolBinario al que se le hara la audicion
     * @return el vertice v pero audicionado a VerticeRojinegro
     */
    private VerticeRojinegro turnRB(VerticeArbolBinario<T> v){
        return (VerticeRojinegro)v;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro v=turnRB(ultimoAgregado);
        v.color=Color.ROJO;
        rebalancearAgregado(v);
    }

    /**
     * Metodo recursivo que nos permitira rebalancear un ArbolRojinegro
     * @param v vertice rojinegro de color rojo sobre el cual se va a rebalancear
     */
    private void rebalancearAgregado(VerticeRojinegro v){
        //1
        if(!v.hayPadre()){
            v.color=Color.NEGRO;
            return;
        }
        
        //2
        VerticeRojinegro p=turnRB(v.padre);
        if(!esRojo(p))
            return;

        //3
        VerticeRojinegro a=turnRB(p.padre);
        VerticeRojinegro t, u;

        if(esIzquierdo(p))
            t=turnRB(a.derecho);
        else
            t=turnRB(a.izquierdo);

        if(t!=null && esRojo(t)){
            t.color=Color.NEGRO;
            p.color=Color.NEGRO;
            a.color=Color.ROJO;
            rebalancearAgregado(a);
            return;
        }

        //4
        if(esDerecho(v) && esIzquierdo(p) || esIzquierdo(v) && esDerecho(p)){
            if(esIzquierdo(p))
                super.giraIzquierda(p);
            else
                super.giraDerecha(p);

            u=p;
            p=v;
            v=u;
        }

        //5
        p.color=Color.NEGRO;
        a.color=Color.ROJO;
        if(esIzquierdo(v))
            super.giraDerecha(a);
        else
            super.giraIzquierda(a);
    }

    /**
     * Metodo que nos dice si un vertice es de color rojo
     * @param v vertice que sabremos si es rojo o no
     * @return true si es rojo, false si es negro
     */
    private boolean esRojo(VerticeRojinegro v){
        return v!=null && v.color==Color.ROJO;
    }

    /**
     * Metodo que nos dice si un vertice es de color negro
     * @param v vertice que sabremos si es negro o no
     * @return true si es negro, false si es rojo
     */
    private boolean esNegro(VerticeRojinegro v){
        return v==null || v.color==Color.NEGRO;
    }

    //Soy el hijo del papá

    /**
     * Metodo privado que nos dice si un vertice es hijo derecho
     * de su padre.
     * @param v el vertice que sabremos si es hijo derecho de su padre
     * @return true si es asi, false si no
     */
    private boolean esDerecho(Vertice v){
        return !v.hayPadre() ? false :  v.padre.derecho==v;
    }

    /**
     * Metodo privado que nos dice si un vertice es hijo izquierdo
     * de su padre.
     * @param v el vertice que sabremos si es hijo izquierdo de su padre
     * @return true si es asi, false si no
     */
    private boolean esIzquierdo(Vertice v){
        return !v.hayPadre() ? false : v.padre.izquierdo==v;
    }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v=turnRB(super.busca(elemento));
        if(v==null)
            return;
            
	    elementos--;
        if(v.izquierdo!=null && v.derecho!=null){
            v=turnRB(intercambiaEliminable(v));
        }
	
        VerticeRojinegro buff, h; 
        if(v.izquierdo==null && v.derecho==null){
            buff=turnRB(nuevoVertice(null));
            buff.color=Color.NEGRO;
            buff.padre=v;
            v.izquierdo=buff;
        }
        
        
        h=v.izquierdo!=null ? turnRB(v.izquierdo) : turnRB(v.derecho);
        eliminaVertice(v);

        if(esRojo(h)){
            h.color=Color.NEGRO;
            return;
        }

        if(esNegro(h) && esNegro(v))
            rebalancearEliminado(h);

        eliminaVertice(h);
    }

    /**
     * Metodo privado que rebalancea un arbol rojinegro
     * @param v vertice rojinegro de color negro sobre el cual se 
     * va a rebalancear un arbol rojinegro 
     */
    private void rebalancearEliminado(VerticeRojinegro v){
        //1
        if(v.padre==null)
            return;

        //2
        VerticeRojinegro p=turnRB(v.padre);
        VerticeRojinegro h=obtenHermano(v);
        if(esRojo(h)){
            h.color=Color.NEGRO;
            p.color=Color.ROJO;
            if(esIzquierdo(v)){
                super.giraIzquierda(p);
                h=turnRB(v.padre.derecho);
            }
            else{
                super.giraDerecha(p);
                h=turnRB(v.padre.izquierdo);
            }
        }
        
        //3
        VerticeRojinegro hIzq=turnRB(h.izquierdo);
        VerticeRojinegro hDer=turnRB(h.derecho);
        if(esNegro(p) && esNegro(h) && esNegro(hIzq) && esNegro(hDer)){
            h.color=Color.ROJO;
            rebalancearEliminado(p);
            return;
        }

        //4
        if(esRojo(p) && esNegro(h) && esNegro(hIzq) && esNegro(hDer)){
            h.color=Color.ROJO;
            p.color=Color.NEGRO;
            return;
        }

        //5
        if((esIzquierdo(v) && esRojo(hIzq) && esNegro(hDer)  ) || (esDerecho(v) && esNegro(hIzq) && esRojo(hDer) )){
            h.color=Color.ROJO;
            if(esRojo(hIzq)){
                hIzq.color=Color.NEGRO;
            }
            else
                hDer.color=Color.NEGRO;
            
            if(esIzquierdo(v)){
                super.giraDerecha(h);
                h=turnRB(v.padre.derecho);
            }
            else{
                super.giraIzquierda(h);
                h=turnRB(v.padre.izquierdo);
            }
        }

        //6
        hIzq=turnRB(h.izquierdo);
        hDer=turnRB(h.derecho);

        h.color=p.color;
        p.color=Color.NEGRO;

        if(esIzquierdo(v)){
            hDer.color=Color.NEGRO;
            super.giraIzquierda(p);
        }
        else{
            hIzq.color=Color.NEGRO;
            super.giraDerecha(p);
        }
    }

    /**
     * Metodo privado que nos regresa el vertice hermano de un vertice v
     * @param v el vertice del cual obtendremos el hermano
     * @return un VerticeRojinegro que es hermano de v
     */
    private VerticeRojinegro obtenHermano(VerticeRojinegro v){
    	if(esIzquierdo(v))
            return turnRB(v.padre.derecho);
        
        return turnRB(v.padre.izquierdo);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
