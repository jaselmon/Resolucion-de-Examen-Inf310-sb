package bo.edu.uagrm.inf310sb.arboles;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArbolMViasBusqueda<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {
	protected NodoMVias<K, V> raiz;
	protected int orden;
	
	public ArbolMViasBusqueda() {
		this.orden=3;
	}
	
	public ArbolMViasBusqueda(int orden) throws ExcepcionOrdenInvalido {
		if(orden<3) {
			throw new ExcepcionOrdenInvalido();
		}else {
			this.orden=orden;
		}
	}
	
	protected NodoMVias<K, V> nuevoNodoVacio(){ // crea un nuevo nodo vacio
		return (NodoMVias<K, V>) NodoMVias.nodoVacio();
	}
	
	
	@Override
	public V buscar(K clave) {
		NodoMVias<K, V> nodoActual = this.raiz;
		if(!this.esArbolVacio()) {
			while(!NodoMVias.esNodoVacio(nodoActual)) {
				NodoMVias<K, V> nodoAnterior = nodoActual;
				for(int i=0; i<nodoActual.cantidadDeDatosNoVacios() && nodoActual==nodoAnterior; i++) {
					if(clave.compareTo(nodoActual.getClave(i)) == 0) {
						return nodoActual.getValor(i);
					}
					if(clave.compareTo(nodoActual.getClave(i)) < 0) {
						if(nodoActual.esHijoVacio(i)) {
							return (V) NodoMVias.datoVacio();
						}else {
							nodoActual = nodoActual.getHijo(i);
						}
					}
				}
				if(nodoActual == nodoAnterior) {
					nodoActual = nodoActual.getHijo(orden-1);
				}
			}
		}
		return (V) NodoMVias.datoVacio();
	}

	@Override
	public boolean contiene(K clave) {
		return this.buscar(clave) != NodoMVias.nodoVacio();
	}

	@Override
	public int size() {
		return this.size(this.raiz);
	}

	private int size(NodoMVias<K, V> nodoActual) {
		int s=0;
		if(!NodoMVias.esNodoVacio(nodoActual)) {
			for(int i=0; i<this.orden; i++) {
				s+=size(nodoActual.getHijo(i));
			}
			s++;
		}
		return s;
	}

	@Override
	public int altura() {
		return this.altura(this.raiz);
	}

	private int altura(NodoMVias<K, V> nodoActual) {
		int h=0;
		if(!NodoMVias.esNodoVacio(nodoActual)) {
			for(int i=0; i<this.orden; i++) {
				int hHijo = altura(nodoActual.getHijo(i));
				if(hHijo>h) {
					h=hHijo;
				}
			}
		}
		return h+1;
	}

	@Override
	public void vaciar() {
		this.raiz = this.nuevoNodoVacio();
	}

	@Override
	public boolean esArbolVacio() {
		return NodoMVias.esNodoVacio(this.raiz);
	}

	@Override
	public int nivel() {
		int nivel=-1;
		if(this.esArbolVacio()) { 
			return nivel;
		}else {
			Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
			NodoMVias<K, V> nodoActual = this.raiz;
			colaDeNodos.offer(nodoActual);	
			int nodosNivel=colaDeNodos.size();
			while(!colaDeNodos.isEmpty()) {
				while(nodosNivel>0) {
					nodoActual = colaDeNodos.poll(); 
					for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
						if(!nodoActual.esHijoVacio(i)) {
							colaDeNodos.offer(nodoActual.getHijo(i));
						}
					}
					if(!nodoActual.esHijoVacio(this.orden-1)) {
						colaDeNodos.offer(nodoActual.getHijo(this.orden-1));
					}
					nodosNivel--;
				}
				nivel++;
				nodosNivel=colaDeNodos.size();
			}
		}
		return nivel;
	}

	@Override
	public List<K> recorridoEnInOrden() {
		List<K> recorrido = new LinkedList<>();
		recorridoEnInOrdenA(this.raiz, recorrido);
		return recorrido;
	}

	private void recorridoEnInOrdenA(NodoMVias<K, V> nodoActual, List<K> recorrido) {
		if(!NodoMVias.esNodoVacio(nodoActual)) { 
			for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
				recorridoEnInOrdenA(nodoActual.getHijo(i), recorrido);
				recorrido.add(nodoActual.getClave(i));
			}
			recorridoEnInOrdenA(nodoActual.getHijo(this.orden-1), recorrido);
		}
	}	

	@Override
	public List<K> recorridoEnPreOrden() {
		List<K> recorrido = new LinkedList<>();
		recorridoEnPreOrdenA(this.raiz, recorrido);
		return recorrido;
	}

	private void recorridoEnPreOrdenA(NodoMVias<K, V> nodoActual, List<K> recorrido) {
		if(!NodoMVias.esNodoVacio(nodoActual)) { 
			for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
				recorrido.add(nodoActual.getClave(i));
				recorridoEnPreOrdenA(nodoActual.getHijo(i), recorrido);
			}
			recorridoEnPreOrdenA(nodoActual.getHijo(this.orden-1), recorrido);
		}
	}	

	@Override
	public List<K> recorridoEnPostOrden() {
		List<K> recorrido = new LinkedList<>();
		recorridoEnPostOrdenA(this.raiz, recorrido);
		return recorrido;
	}

	private void recorridoEnPostOrdenA(NodoMVias<K, V> nodoActual, List<K> recorrido) {
		if(!NodoMVias.esNodoVacio(nodoActual)) { 
			recorridoEnPostOrdenA(nodoActual.getHijo(0), recorrido);
			for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
				recorridoEnPostOrdenA(nodoActual.getHijo(i+1), recorrido);
				recorrido.add(nodoActual.getClave(i));
			}
		}
	}	

	@Override
	public List<K> recorridoPorNiveles() {
		List<K> recorrido = new LinkedList<>();
		if(this.esArbolVacio()) { 
			return recorrido;
		}else {
			Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
			NodoMVias<K, V> nodoActual = this.raiz;
			colaDeNodos.offer(nodoActual);	
			while(!colaDeNodos.isEmpty()) {
				nodoActual = colaDeNodos.poll();
				for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
					recorrido.add(nodoActual.getClave(i));
					if(!nodoActual.esHijoVacio(i)) {
						colaDeNodos.offer(nodoActual.getHijo(i));
					}
				}
				if(!nodoActual.esHijoVacio(this.orden-1)) {
					colaDeNodos.offer(nodoActual.getHijo(this.orden-1));
				}	
			}
		}
		return recorrido;
	}
	
  public int hojasAPartirDelNivel(int nivel) {
		return hojasAPartirDelNivel(this.raiz, nivel, 0);
	}

	private int hojasAPartirDelNivel(NodoMVias<K, V> nodoActual, int nivel, int nivelActual) {
		int nodosHoja=0;
		if(!NodoMVias.esNodoVacio(nodoActual)) {
			if(nivelActual>=nivel) {
				if(nodoActual.esHoja()) {
					nodosHoja++;
				}
			}
			for(int i=0; i<this.orden; i++) {
				nodosHoja+=this.hojasAPartirDelNivel(nodoActual.getHijo(i), nivel, nivelActual+1);
			}
		}
		return nodosHoja;
	}
	
	
	@Override
	public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
		if(this.esArbolVacio()) {
			this.raiz = new NodoMVias<K, V>(this.orden, clave, valor);
		}else {
			NodoMVias<K, V> nodoActual = this.raiz; 	//	
			while(!NodoMVias.esNodoVacio(nodoActual)) { 
				if(nodoActual.existeClave(clave)) {   
					throw new ExcepcionClaveYaExiste();
				}else {
					if(nodoActual.esHoja()) {
						if(nodoActual.estaLleno()) {
							int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
							NodoMVias<K, V> nuevoHijo  = new NodoMVias<>(this.orden, clave, valor);
							nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
						}else {
							this.insertarEnOrden(nodoActual, clave, valor);
						}
						nodoActual = NodoMVias.nodoVacio();
					}else {
						int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
						if(nodoActual.esHijoVacio(posicionPorDondeBajar)) {
							NodoMVias<K, V> nuevoHijo  = new NodoMVias<>(this.orden, clave, valor);
							nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
							nodoActual = NodoMVias.nodoVacio();
						}else {
							nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
						}
					}
				}
			}
		}
	}

	protected void insertarEnOrden(NodoMVias<K, V> nodoActual, K clave, V valor) {
		int pos=0;
		while(pos<nodoActual.cantidadDeDatosNoVacios() && clave.compareTo(nodoActual.getClave(pos))>0) {
			pos++;
		}
		for(int i=nodoActual.cantidadDeDatosNoVacios()-1; i>=pos; i--) {
			nodoActual.setClave(i+1, nodoActual.getClave(i));
			nodoActual.setValor(i+1, nodoActual.getValor(i));
		}
		nodoActual.setClave(pos, clave);
		nodoActual.setValor(pos, valor);
	}

	protected int porDondeBajar(NodoMVias<K, V> nodoActual, K clave) {
		int pos=0;
		while(pos<nodoActual.cantidadDeDatosNoVacios() && nodoActual.getClave(pos).compareTo(clave)<0) {
			pos++;	
		}
		return pos;
	}
	
	@Override
	public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
		V valorARetornar = this.buscar(claveAEliminar);
		if(valorARetornar == null) {
			throw new ExcepcionClaveNoExiste();
		}
		this.raiz = this.eliminar(this.raiz, claveAEliminar);
		return valorARetornar;
	}

	private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
		for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
			if(claveAEliminar.compareTo(nodoActual.getClave(i))==0) {
				if(nodoActual.esHoja()) {
					nodoActual.eliminarDato(i);
					if(nodoActual.estaVacio()) {
						return NodoMVias.nodoVacio();
					}else {
						return nodoActual;
					}
				}else {
					K claveReemplazo;
					if(nodoActual.hayHijosAdelante(i)) {
						claveReemplazo = this.buscarSucesorInOrden(nodoActual, claveAEliminar);
					}else {
						claveReemplazo = this.buscarPredecesorInOrden(nodoActual, claveAEliminar);
					}
					V valorReemplazo = this.buscar(claveReemplazo);
					nodoActual = this.eliminar(nodoActual, claveReemplazo);
					nodoActual.setClave(i, claveReemplazo);
					nodoActual.setValor(i, valorReemplazo);
					return nodoActual;
				}
			}
			if(claveAEliminar.compareTo(nodoActual.getClave(i))<0) {
				NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
				nodoActual.setHijo(i, supuestoNuevoHijo);
				return nodoActual;
			}
		}
		NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(this.orden-1), claveAEliminar);
		nodoActual.setHijo(this.orden-1, supuestoNuevoHijo);
		return nodoActual;
	}

	private K buscarPredecesorInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {
		int i=0;
		while(i<	nodoActual.cantidadDeDatosNoVacios() && claveAEliminar.compareTo(nodoActual.getClave(i))>0) {
			i++;
		}
		if(nodoActual.esHijoVacio(i)) {
			if(i>0) {
				return nodoActual.getClave(i-1);
			}else {
				return (K) NodoMVias.datoVacio();
			}
		}else {
			NodoMVias<K, V> hijoEnPosicion = nodoActual.getHijo(i); 
			return hijoEnPosicion.getClave(hijoEnPosicion.cantidadDeDatosNoVacios()-1);
		}
	}

	private K buscarSucesorInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {
		int i=0;
		while(i<	nodoActual.cantidadDeDatosNoVacios() && claveAEliminar.compareTo(nodoActual.getClave(i))>=0) {
			i++;
		}
		if(i<nodoActual.cantidadDeDatosNoVacios() && nodoActual.esHijoVacio(i)) {
			return nodoActual.getClave(i);
		}else {
			return nodoActual.getHijo(i).getClave(0);
		}
	}
	
	public int nodosConDatosVacios() {
		return this.nodosConDatosVacios(this.raiz);
	}

	private int nodosConDatosVacios(NodoMVias<K, V> nodoActual) {
		int nodos=0;
		if(!NodoMVias.esNodoVacio(nodoActual)) {
			if(!nodoActual.estaLleno()) {
				nodos++;
			}
			for(int i=0; i<this.orden; i++) {
				nodos+=this.nodosConDatosVacios(nodoActual.getHijo(i));
			}
		}
		return nodos;
	}
	
	public boolean estaBalanceado() {		
		if(this.esArbolVacio()) {
			return false;
		}else {
			int maximoDeDatos = this.orden-1;
			int minimoDeDatos = maximoDeDatos/2;
			int minimoDeHijos = minimoDeDatos+1;
			Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
			NodoMVias<K, V> nodoActual = this.raiz;
			colaDeNodos.offer(nodoActual);	
			while(!colaDeNodos.isEmpty()) {
				nodoActual = colaDeNodos.poll(); 
				if(nodoActual == this.raiz) {
					if(nodoActual.esHoja()) {
						if(nodoActual.cantidadDeDatosNoVacios() > maximoDeDatos) {
							return false;
						}
					}else{
						 if(nodoActual.cantidadDeHijosNoVacios() < 2 || nodoActual.cantidadDeDatosNoVacios() > maximoDeDatos) {
							 return false;
						 }
					}
				}else {
					if(nodoActual.esHoja()) {
						if(nodoActual.cantidadDeDatosNoVacios() < minimoDeDatos || nodoActual.cantidadDeDatosNoVacios() > maximoDeDatos) {
							return false;
						}
					}else {
						if(nodoActual.cantidadDeDatosNoVacios() < minimoDeDatos || nodoActual.cantidadDeDatosNoVacios() > maximoDeDatos  
						   || nodoActual.cantidadDeHijosNoVacios() < minimoDeHijos) {
							return false;
						}
					}
				}
				for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
					if(!nodoActual.esHijoVacio(i)) {
						colaDeNodos.offer(nodoActual.getHijo(i));
					}
				}
				if(!nodoActual.esHijoVacio(this.orden-1)) {
					colaDeNodos.offer(nodoActual.getHijo(this.orden-1));
				}	
			}
		}
		return true;
	}
	
	
	private NodoMVias<K, V> buscarNodoDeLaClave(K clave){
		if(this.buscar(clave) != NodoMVias.datoVacio()) {
			Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
			NodoMVias<K, V> nodoActual = this.raiz;
			colaDeNodos.offer(nodoActual);	
			while(!colaDeNodos.isEmpty()) {
				nodoActual = colaDeNodos.poll();
				if(nodoActual.existeClave(clave)) {
					return nodoActual;
				}
				for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
					if(!nodoActual.esHijoVacio(i)) {
						colaDeNodos.offer(nodoActual.getHijo(i));
					}
				}
				if(!nodoActual.esHijoVacio(this.orden-1)) {
					colaDeNodos.offer(nodoActual.getHijo(this.orden-1));
				}	
			}
		}
		return NodoMVias.nodoVacio();
	}
	
	//Tarea 4
		//3. Implementar un metodo para un Arbol de MVias que retorne cuantos nodos del Arbol son padres fuera del nivel n
		public int nodosPadreFueraDelNivel(int nivel) {
			return this.nodosPadreFueraDelNivel(this.raiz, nivel, 0);
		}

		private int nodosPadreFueraDelNivel(NodoMVias<K, V> nodoActual, int nivel, int nivelActual) {
			int nodosPadre=0;
			if(!NodoMVias.esNodoVacio(nodoActual)) {
				if(nivelActual != nivel) {
					if(!nodoActual.esHoja()) {
						nodosPadre = nodosPadre + 1;
					}
				}
				for(int i=0; i<this.orden; i++) {
					nodosPadre = nodosPadre + this.nodosPadreFueraDelNivel(nodoActual.getHijo(i), nivel, nivelActual+1);
				}
			}
			return nodosPadre;
		}
	

}
