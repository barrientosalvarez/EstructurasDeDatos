package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        Nodo n=cabeza;
        String ret="";
        while(n!=null){
            ret+=""+n.elemento+",";
            n=n.siguiente;
        }
        return ret;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento==null)
            throw new IllegalArgumentException();
        
        Nodo buffer=new Nodo(elemento);
        if(rabo==null)
            rabo=cabeza=buffer;
        else{
            rabo.siguiente=buffer;
            rabo=buffer;
        }
    }
}
