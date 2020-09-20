package bo.edu.uagrm.inf310sb.arboles;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ArbolB<K extends Comparable<K>, V> extends ArbolMViasBusqueda<K, V> {
	private int maximoDeDatos;
	private int minimoDeDatos;
	private int minimoDeHijos;
	
	public ArbolB() {
		super();
		this.maximoDeDatos=2;
		this.minimoDeDatos=1;
		this.minimoDeHijos=2; 
	}
	
	public ArbolB(int orden) throws ExcepcionOrdenInvalido {
		super(orden);
		this.maximoDeDatos = super.orden-1;
		this.minimoDeDatos = this.maximoDeDatos/2;
		this.minimoDeHijos = this.minimoDeDatos+1; 
	}
	
	@Override
	public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
		if(this.esArbolVacio()) {
			this.raiz = new NodoMVias<K, V>(this.orden+1, clave, valor);
		}else {
			Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
			NodoMVias<K, V> nodoActual = this.raiz; 	
			while(!NodoMVias.esNodoVacio(nodoActual)) { 
				if(nodoActual.existeClave(clave)) {   
					throw new ExcepcionClaveYaExiste();
				}else {
					if(nodoActual.esHoja()) {
						super.insertarEnOrden(nodoActual, clave, valor);
						if(nodoActual.cantidadDeDatosNoVacios() > this.maximoDeDatos) {
							this.dividir(nodoActual, pilaDeAncestros); 
						} 
						nodoActual = NodoMVias.nodoVacio();
					}else {
						int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
						pilaDeAncestros.push(nodoActual);
						nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
					}
				}
			}
		}
	}

	private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) { //pendiente
		if(pilaDeAncestros.isEmpty()) {
			NodoMVias<K, V> nuevoHijo1 = new NodoMVias<>(this.orden+1);
			NodoMVias<K, V> nuevoHijo2 = new NodoMVias<>(this.orden+1);
			int mitadDatos=nodoActual.cantidadDeDatosNoVacios()/2;
			for(int i=0; i<mitadDatos; i++) {
				nuevoHijo1.setClave(i, nodoActual.getClave(i));
				nuevoHijo1.setValor(i, nodoActual.getValor(i));
			}
			int j=0;
			for(int i=mitadDatos+1; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
				nuevoHijo2.setClave(j, nodoActual.getClave(i));
				nuevoHijo2.setValor(j, nodoActual.getValor(i));
				j++;
			}
			this.raiz=new NodoMVias<K, V>(this.orden+1, nodoActual.getClave(mitadDatos), nodoActual.getValor(mitadDatos));
			this.raiz.setHijo(0, nuevoHijo1);
			this.raiz.setHijo(1, nuevoHijo2);
		}else {
			NodoMVias<K, V> nodoPadre = pilaDeAncestros.pop();
			NodoMVias<K, V> nuevoHijo1 = new NodoMVias<>(this.orden+1);
			NodoMVias<K, V> nuevoHijo2 = new NodoMVias<>(this.orden+1);
			int mitadDatos=nodoActual.cantidadDeDatosNoVacios()/2;
			for(int i=0; i<mitadDatos; i++) {
				nuevoHijo1.setClave(i, nodoActual.getClave(i));
				nuevoHijo1.setValor(i, nodoActual.getValor(i));
			}
			int j=0;
			for(int i=mitadDatos+1; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
				nuevoHijo2.setClave(j, nodoActual.getClave(i));
				nuevoHijo2.setValor(j, nodoActual.getValor(i));
				j++;
			}
			super.insertarEnOrden(nodoPadre, nodoActual.getClave(mitadDatos), nodoActual.getValor(mitadDatos));
			int i=0;
			while(i<nodoPadre.cantidadDeDatosNoVacios() && nodoPadre.getClave(i).compareTo(nodoActual.getClave(mitadDatos))<0) {
				i++;
			}
			if(nodoPadre.hayHijosAdelante(i)) {
				
			}
//			do {
//				nodoPadre = pilaDeAncestros.pop();
//				NodoMVias<K, V> nuevoHijo1 = new NodoMVias<>(this.orden+1);
//				NodoMVias<K, V> nuevoHijo2 = new NodoMVias<>(this.orden+1);
//				int mitadDatos=nodoActual.cantidadDeDatosNoVacios()/2;
//				for(int i=0; i<mitadDatos; i++) {
//					nuevoHijo1.setClave(i, nodoActual.getClave(i));
//					nuevoHijo1.setValor(i, nodoActual.getValor(i));
//				}
//				for(int i=mitadDatos+1; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
//					nuevoHijo2.setClave(i, nodoActual.getClave(i));
//					nuevoHijo2.setValor(i, nodoActual.getValor(i));
//				}
//			} while(!pilaDeAncestros.isEmpty() && nodoPadre.cantidadDeDatosNoVacios() == 0);
		}
	}
	
	@Override
	public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
		Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
		NodoMVias<K, V> nodoActual = this.buscarNodoDeLaClave(claveAEliminar, pilaDeAncestros);
		if(NodoMVias.esNodoVacio(nodoActual)) {
			throw new ExcepcionClaveNoExiste();
		}
		int posicionClaveAEliminar = this.porDondeBajar(nodoActual, claveAEliminar);
		V valorAEliminar = nodoActual.getValor(posicionClaveAEliminar);
		if(nodoActual.esHoja()) {
			nodoActual.eliminarDato(posicionClaveAEliminar);
			if(nodoActual.cantidadDeDatosNoVacios() < this.minimoDeDatos) {
				if(pilaDeAncestros.isEmpty()) {
					if(nodoActual.estaVacio()) {
						super.vaciar();
					}
				}else {
					this.prestarOFusionar(nodoActual, pilaDeAncestros);
				}
			}
		}else {
			pilaDeAncestros.push(nodoActual);
			NodoMVias<K, V> nodoDelPredecesor = this.buscarNodoDelPredecesor(pilaDeAncestros, nodoActual.getHijo(posicionClaveAEliminar));
			int posicionDelPredecesor = nodoDelPredecesor.cantidadDeDatosNoVacios()-1;
			K clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
			V valorPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
			nodoDelPredecesor.eliminarDato(posicionDelPredecesor);
			nodoActual.setClave(posicionClaveAEliminar, clavePredecesora);
			nodoActual.setValor(posicionClaveAEliminar, valorPredecesor);
			if(nodoDelPredecesor.cantidadDeDatosNoVacios() < this.minimoDeDatos) {
				this.prestarOFusionar(nodoDelPredecesor, pilaDeAncestros);
			}
		}
		return valorAEliminar;
	}

	private NodoMVias<K, V> buscarNodoDelPredecesor(Stack<NodoMVias<K, V>> pilaDeAncestros, NodoMVias<K, V> nodoActual) {
		while(!NodoMVias.esNodoVacio(nodoActual)) {
			
		}
		return null;
	}

	private void prestarOFusionar(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
		
	}

	private NodoMVias<K, V> buscarNodoDeLaClave(K claveABuscar, Stack<NodoMVias<K, V>> pilaDeAncestros) {
		NodoMVias<K, V> nodoActual = this.raiz; 	
		while(!NodoMVias.esNodoVacio(nodoActual)) { 
			NodoMVias<K, V> nodoAnterior = nodoActual;
			for(int i=0; i<nodoActual.cantidadDeDatosNoVacios() && nodoAnterior==nodoActual; i++) {
				K claveActual = nodoActual.getClave(i);
				if(claveABuscar.compareTo(claveActual)==0) {
					return nodoActual;
				}
				if(claveABuscar.compareTo(claveActual)<0) {
					if(!nodoActual.esHoja()) {
						pilaDeAncestros.push(nodoActual);
						nodoActual = nodoActual.getHijo(i);
					}else {
						nodoActual = NodoMVias.nodoVacio();
					}
				}
			}//fin for
			if(nodoAnterior==nodoActual) {
				if(!nodoActual.esHoja()) {
					pilaDeAncestros.push(nodoActual);
					nodoActual = nodoActual.getHijo(nodoActual.cantidadDeDatosNoVacios());
				}else {
					nodoActual = NodoMVias.nodoVacio();
				}
			}
		} //fin while
		return NodoMVias.nodoVacio();
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
				if(nodoActual.esHoja()) {
					for(int i=0; i<nodoActual.cantidadDeDatosNoVacios(); i++) {
						recorrido.add(nodoActual.getClave(i));
					}	
				}else {
					for(int i=0; i<nodoActual.cantidadDeHijosNoVacios(); i++) {
						if(!nodoActual.esDatoVacio(i)) {
							recorrido.add(nodoActual.getClave(i));
						}
						if(!nodoActual.esHijoVacio(i)) {
							colaDeNodos.offer(nodoActual.getHijo(i));
						}
					}
					if(!nodoActual.esHijoVacio(this.orden-1)) {
						colaDeNodos.offer(nodoActual.getHijo(this.orden-1));
					}	
				}
			}
		}
		return recorrido;
	}
	
	
}
