package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 5: Árboles AVL.
 */
public class Practica5 {
    
    /* Imprime el uso del programa y lo termina. 
    private static void uso() {
        System.err.println("Uso: java -jar practica5.jar N");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1)
            uso();

        int N = -1;
        try {
            N = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            uso();
        }

        if (N < 1)
            uso();

        Random random = new Random();
        NumberFormat nf = NumberFormat.getIntegerInstance();
        long tiempoInicial, tiempoTotal;

        int[] arreglo = new int[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = random.nextInt();

        Integer[] qs = new Integer[N];
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            qs[i] = arreglo[i];
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un arreglo con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        Arreglos.quickSort(qs);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar un arreglo con %s elementos " +
                          "usando QuickSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        int b = qs[random.nextInt(N)];

        tiempoInicial = System.nanoTime();
        int idx = Arreglos.busquedaBinaria(qs, b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un arreglo " +
                          "con %s elementos usando búsqueda binaria.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Lista<Integer> ms = new Lista<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            ms.agregaFinal(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear una lista con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ms = Lista.mergeSort(ms);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar una lista con %s elementos " +
                          "usando MergeSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolBinarioOrdenado<Integer> bo = new ArbolBinarioOrdenado<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            bo.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear un árbol binario ordenado con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        bo.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un árbol " +
                          "binario ordenado con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolRojinegro<Integer> rn = new ArbolRojinegro<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            rn.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear un árbol rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        rn.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un árbol " +
                          "rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolAVL<Integer> avl = new ArbolAVL<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            avl.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear un árbol AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        avl.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un árbol " +
                          "AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
    }*/

    public static void main(String[] args){
        ArbolBinarioOrdenado<Integer> rn=new ArbolBinarioOrdenado<Integer>();
        Random random=new Random();
        int[] arreglo= new int[20];
        for(int i=0; i<20; i++)
            arreglo[i]=random.nextInt();

        for(int j=0; j<20; j++)
            rn.agrega(arreglo[j]);

        System.out.println(rn.toString());

        niveles(rn);

    }

    private static <T> void niveles(ArbolBinario<T> arbol){
        if(arbol.raiz()==null)
            return;

        VerticeArbolBinario<T> v = arbol.raiz();
        int[] alturas=new int[arbol.getElementos()+1];
        
        Cola<VerticeArbolBinario<T>> cola=new Cola<>();
        cola.mete(v);
        int i=0;

        while(!cola.esVacia()){
            VerticeArbolBinario<T> buff=cola.saca();
            alturas[i]=buff.profundidad();

            if(buff.hayIzquierdo())
                cola.mete(buff.izquierdo());
            if(buff.hayDerecho())
                cola.mete(buff.derecho());

            i++;
        }

        imprime(arbol, alturas);
    }

    private static <T> void imprime(ArbolBinario<T> arbol, int[] niveles){
        VerticeArbolBinario<T> v=arbol.raiz();
        Cola<VerticeArbolBinario<T>> cola=new Cola<>();
        cola.mete(v);
        int i=0;

        while(!cola.esVacia() && i+1<=arbol.getElementos()){
            if(i==arbol.getElementos())
                break;

            VerticeArbolBinario<T> buff=cola.saca();
            System.out.print(buff.toString()+",");

            if(niveles[i]!=niveles[i+1])
                System.out.print("\n");

            if(buff.hayIzquierdo())
                cola.mete(buff.izquierdo());

            if(buff.hayDerecho())
                cola.mete(buff.derecho());

            i++;
        }

        System.out.print("\n");
    }
    


}
