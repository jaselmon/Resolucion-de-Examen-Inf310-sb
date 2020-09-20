package bo.edu.uagrm.inf310sb.arboles;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ArbolBinarioBusqueda<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {

	protected NodoBinario<K, V> raiz;
	
	protected NodoBinario<K, V> nuevoNodoVacio(){ // crea un nuevo nodo vacio
		return (NodoBinario<K, V>) NodoBinario.nodoVacio();
	}
	
	public NodoBinario<K, V> getRaiz(){
		return this.raiz;
	}

	@Override
	public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
		if(this.esArbolVacio()) {	
			this.raiz = new NodoBinario<K, V>(clave, valor);
		}else{	
			NodoBinario<K, V> nodoActual = this.raiz; 
			NodoBinario<K, V> nodoAnterior = this.nuevoNodoVacio(); 
			while (!NodoBinario.esNodoVacio(nodoActual)) { 
				nodoAnterior = nodoActual; 
				if(clave.compareTo(nodoActual.getClave()) > 0) { 
					nodoActual = nodoActual.getHijoDerecho();	
				}else if(clave.compareTo(nodoActual.getClave()) < 0) {
					nodoActual = nodoActual.getHijoIzquierdo(); 
				}else {										   
					throw new ExcepcionClaveYaExiste("La clave que quiere insertar "+clave+" ya existe en su arbol");
				}
			} 
			NodoBinario<K, V> nuevoNodo = new NodoBinario<K, V>(clave, valor);
			if(clave.compareTo(nodoAnterior.getClave()) > 0) {
				nodoAnterior.setHijoDerecho(nuevoNodo);
			}else {
				nodoAnterior.setHijoIzquierdo(nuevoNodo);
			}
		}
		
	}

	@Override
	public V eliminar(K clave) throws ExcepcionClaveNoExiste {
		V valorARetornar = this.buscar(clave);
		if(valorARetornar == null) {
			throw new ExcepcionClaveNoExiste();
		}
		this.raiz=eliminar(this.raiz, clave);
		return valorARetornar;
	}
	
	private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodoActual, K claveAEliminar) throws ExcepcionClaveNoExiste{
		if(NodoBinario.esNodoVacio(nodoActual)) {
			throw new ExcepcionClaveNoExiste();
		}else {
			K claveActual = nodoActual.getClave();
			if(claveAEliminar.compareTo(claveActual)>0) {
				NodoBinario<K, V> supuestoNuevoHijoDerecho = this.eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
				nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
				return nodoActual;
			}
			if(claveAEliminar.compareTo(claveActual)>0) {
				NodoBinario<K, V> supuestoNuevoHijoIzquierdo = this.eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
				nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
				return nodoActual;
			}
			
			//Se Encuentra el nodo a eliminar
			if(nodoActual.esHoja()) { //caso 1
				return (NodoBinario<K, V>) NodoBinario.nodoVacio();
			}
			if(nodoActual.esVacioSuHijoIzquierdo() && !nodoActual.esVacioSuHijoDerecho()) { //caso 2
				return nodoActual.getHijoDerecho();
			}
			if(!nodoActual.esVacioSuHijoIzquierdo() && nodoActual.esVacioSuHijoDerecho()) { //caso 2
				return nodoActual.getHijoIzquierdo();
			}
			//caso 3
			NodoBinario<K, V> nodoReemplazo = this.buscarNodoSucesor(nodoActual.getHijoDerecho()); //debe retornar el reemplazo, el ultimo descendiente por izquierda
			NodoBinario<K, V> posibleNuevoHijo = this.eliminar(nodoActual.getHijoDerecho(), nodoReemplazo.getClave());
			nodoActual.setHijoDerecho(posibleNuevoHijo);
			nodoReemplazo.setHijoIzquierdo(nodoActual.getHijoIzquierdo());
			nodoReemplazo.setHijoDerecho(nodoActual.getHijoDerecho());
			nodoActual.setHijoIzquierdo((NodoBinario<K, V>)NodoBinario.nodoVacio());
			nodoReemplazo.setHijoDerecho((NodoBinario<K, V>)NodoBinario.nodoVacio());
			return nodoReemplazo;
		}
	}

	public NodoBinario<K, V> buscarNodoSucesor(NodoBinario<K, V> nodoActual) {
		NodoBinario<K, V> nodoAnterior=nodoActual;
		while(!NodoBinario.esNodoVacio(nodoActual)) {
			nodoAnterior = nodoActual;
			nodoActual = nodoActual.getHijoIzquierdo();
		}
		return nodoAnterior;
	}

	@Override
	public V buscar(K clave) {
		NodoBinario<K, V> nodoActual = this.raiz;	//tomamos como referencia para el ciclo, la raiz
		while (!NodoBinario.esNodoVacio(nodoActual)) {	 // bucle hasta que encuentre un nodo vacio
			if(clave.compareTo(nodoActual.getClave()) > 0) {	//si la clave es mayor a la del nodo actual, se mueve a la derecha
				nodoActual = nodoActual.getHijoDerecho();	
			}else if(clave.compareTo(nodoActual.getClave()) < 0) {
				nodoActual = nodoActual.getHijoIzquierdo();	//si la clave es mayor a la del nodo actual, se mueve a la izquierda
			}else {										   
				return nodoActual.getValor();		// si encuentra, retorna el valor del nodo actual
			}
		} 
		return null;	//si el arbol es vacio, o la clave no está en el arbol, retorna nulo
	}

	@Override
	public boolean contiene(K clave) {
		return this.buscar(clave) != null;
	}

	@Override
	public int size() {
		int s=0;
		if(this.esArbolVacio()) { 	//Si el arbol es vacio, no hay nodos
			return s;
		}else {	//Si no, realizamos un recorrido por niveles
			Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
			colaDeNodos.offer(this.raiz);	
			while(!colaDeNodos.isEmpty()) {
				NodoBinario<K, V> nodoActual = colaDeNodos.poll(); 
				s++;
				if(!nodoActual.esVacioSuHijoIzquierdo()) {
					colaDeNodos.offer(nodoActual.getHijoIzquierdo());
				}
				if(!nodoActual.esVacioSuHijoDerecho()) {
					colaDeNodos.offer(nodoActual.getHijoDerecho());
				}		
			}
		}
		return s;
	}

	@Override
	public int altura() {
		return altura(this.raiz);
	}
	
	protected int altura(NodoBinario<K, V> nodoActual) {
		if(!NodoBinario.esNodoVacio(nodoActual)) {
			int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
			int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
			if(alturaPorIzquierda>alturaPorDerecha) {
				return ++alturaPorIzquierda;
			}else {
				return ++alturaPorDerecha;
			}
		}
		return 0;
	}
	
	public int alturaI() {
		int h=0;
		if(!this.esArbolVacio()) {
			Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
			colaDeNodos.offer(this.raiz); 
			int nodosDelNivel=colaDeNodos.size();
			while(!colaDeNodos.isEmpty()) {
				while(nodosDelNivel>0) {
					NodoBinario<K, V> nodoActual = colaDeNodos.poll();
					if(!nodoActual.esVacioSuHijoIzquierdo()) {
						colaDeNodos.offer(nodoActual.getHijoIzquierdo());
					}
					if(!nodoActual.esVacioSuHijoDerecho()) {
						colaDeNodos.offer(nodoActual.getHijoDerecho());
					}
					nodosDelNivel--;					
				}
				h++;
				nodosDelNivel=colaDeNodos.size();
			}
		}
		return h;
	}

	@Override
	public void vaciar() {
		this.raiz = this.nuevoNodoVacio();
	}

	@Override
	public boolean esArbolVacio() {
		return NodoBinario.esNodoVacio(this.raiz);
	}

	@Override
	public int nivel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<K> recorridoEnInOrden() {
		List<K> recorrido = new LinkedList<>();
		if(this.esArbolVacio()) { //Si el arbol es vacio, retorna la lista instanciada, pero sin elementos
			return recorrido;
		}else {
			Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
			NodoBinario<K, V> nodoActual = this.raiz;
			meterEnPilaParaInOrden(pilaDeNodos, nodoActual);
			while(!pilaDeNodos.isEmpty()) {
				nodoActual = pilaDeNodos.pop(); 
				recorrido.add(nodoActual.getClave());
				if(!nodoActual.esVacioSuHijoDerecho()) {
					nodoActual = nodoActual.getHijoDerecho();
					meterEnPilaParaInOrden(pilaDeNodos, nodoActual);
				}		
			}
		}
		return recorrido;
	}

	private void meterEnPilaParaInOrden(Stack<NodoBinario<K, V>> pilaDeNodos, NodoBinario<K, V> nodoActual) {
		while(!NodoBinario.esNodoVacio(nodoActual)) {
			pilaDeNodos.push(nodoActual);
			nodoActual = nodoActual.getHijoIzquierdo();
		}
	}
	
	public List<K> recorridoEnInOrdenR(){
		List<K> recorrido = new LinkedList<>();
		recorridoEnInOrdenRA(this.raiz, recorrido);
		return recorrido;
	}

	private void recorridoEnInOrdenRA(NodoBinario<K, V> nodoActual, List<K> recorrido) {
		if(!NodoBinario.esNodoVacio(nodoActual)) { // simular N con la altura del arbol desde la raiz (nodo actual)
			recorridoEnInOrdenRA(nodoActual.getHijoIzquierdo(), recorrido);
			recorrido.add(nodoActual.getClave());
			recorridoEnInOrdenRA(nodoActual.getHijoDerecho(), recorrido);
		}
		
	}	

	@Override
	public List<K> recorridoEnPreOrden() {
		List<K> recorrido = new LinkedList<>();
		if(this.esArbolVacio()) { //Si el arbol es vacio, retorna la lista instanciada, pero sin elementos
			return recorrido;
		}else {
			Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
			pilaDeNodos.push(this.raiz);	
			while(!pilaDeNodos.isEmpty()) {
				NodoBinario<K, V> nodoActual = pilaDeNodos.pop(); 
				recorrido.add(nodoActual.getClave());
				if(!nodoActual.esVacioSuHijoDerecho()) {
					pilaDeNodos.push(nodoActual.getHijoDerecho());
				}
				if(!nodoActual.esVacioSuHijoIzquierdo()) {
					pilaDeNodos.push(nodoActual.getHijoIzquierdo());
				}		
			}
		}
		return recorrido;
	}

	@Override
	public List<K> recorridoEnPostOrden() {
		List<K> recorrido = new LinkedList<>();
		recorridoEnPostOrden(this.raiz, recorrido);
		return recorrido;
	}

	private void recorridoEnPostOrden(NodoBinario<K, V> nodoActual, List<K> recorrido) {
		if(!NodoBinario.esNodoVacio(nodoActual)) { // simular N con la altura del arbol desde la raiz (nodo actual)
			recorridoEnPostOrden(nodoActual.getHijoIzquierdo(), recorrido);
			recorridoEnPostOrden(nodoActual.getHijoDerecho(), recorrido);
			recorrido.add(nodoActual.getClave());
		}
	}	

	@Override
	public List<K> recorridoPorNiveles() {
		List<K> recorrido = new LinkedList<>();
		if(this.esArbolVacio()) { //Si la lista es vacia, retorna la lista instanciada, pero sin elementos
			return recorrido;
		}else {
			Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
			NodoBinario<K, V> nodoActual = this.raiz;
			colaDeNodos.offer(nodoActual);	//offer inserta al igual que el add
			while(!colaDeNodos.isEmpty()) {
				nodoActual = colaDeNodos.poll(); //poll devuelve y elimina el primer elemento de la cola
				recorrido.add(nodoActual.getClave());
				if(!nodoActual.esVacioSuHijoIzquierdo()) {		//si el hijo izquierdo del nodo actual no es vacio, va a la cola
					colaDeNodos.offer(nodoActual.getHijoIzquierdo());
				}
				if(!nodoActual.esVacioSuHijoDerecho()) {	//si el hijo derecho del nodo actual no es vacio, va a la cola
					colaDeNodos.offer(nodoActual.getHijoDerecho());
				}		
			}
		}
		return recorrido;
	}
	
	public List<K> recorridoPorNivelesR(){
		List<K> recorrido = new LinkedList<>();
		recorridoPorNiveles(this.raiz, recorrido);
		return recorrido;
	}
	
	private void recorridoPorNiveles(NodoBinario<K, V> nodoActual, List<K> recorrido) {
		if(!NodoBinario.esNodoVacio(nodoActual)) { // simular N con la altura del arbol desde la raiz (nodo actual)
			if(recorrido.size()%2==0) {
				recorrido.add(nodoActual.getClave());
				recorridoPorNiveles(nodoActual.getHijoIzquierdo(), recorrido);
			}else {
				recorrido.add(nodoActual.getClave());
				recorridoPorNiveles(nodoActual.getHijoDerecho(), recorrido);
			}
		}
	}

	
	
	public boolean esMonticulo() {
		if(this.esArbolVacio()) { 
			return false;
		}else {
			Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
			NodoBinario<K, V> nodoActual = this.raiz;
			colaDeNodos.offer(nodoActual);	
			while(!colaDeNodos.isEmpty()) {
				nodoActual = colaDeNodos.poll(); 
				if(!nodoActual.esVacioSuHijoIzquierdo()) {		
					if(nodoActual.getClave().compareTo(nodoActual.getHijoIzquierdo().getClave())>0) {
						return false;
					}
				}
				if(!nodoActual.esVacioSuHijoDerecho()) {	
					if(nodoActual.getClave().compareTo(nodoActual.getHijoDerecho().getClave())>0) {
						return false;
					}
				}		
			}
		}
		return true;
	}
	
	
	
}
