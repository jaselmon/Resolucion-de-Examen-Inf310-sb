package bo.edu.uagrm.inf310sb.UI;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import bo.edu.uagrm.inf310sb.arboles.AVL;
import bo.edu.uagrm.inf310sb.arboles.ArbolB;
import bo.edu.uagrm.inf310sb.arboles.ArbolBinarioBusqueda;
import bo.edu.uagrm.inf310sb.arboles.ArbolMViasBusqueda;
import bo.edu.uagrm.inf310sb.arboles.ExcepcionClaveNoExiste;
import bo.edu.uagrm.inf310sb.arboles.ExcepcionClaveYaExiste;
import bo.edu.uagrm.inf310sb.arboles.ExcepcionOrdenInvalido;
import bo.edu.uagrm.inf310sb.arboles.IArbolBusqueda;
import bo.edu.uagrm.inf310sb.arboles.NodoBinario;
import bo.edu.uagrm.inf310sb.arboles.NodoMVias;

public class TestArbol {
	
	public static void main(String[] args) throws ExcepcionClaveYaExiste, ExcepcionOrdenInvalido, ExcepcionClaveNoExiste{
		IArbolBusqueda<Integer, String> arbolDePrueba ;
		
		arbolDePrueba = new ArbolBinarioBusqueda<>();
		
		try {
			
			arbolDePrueba.insertar(42, "Cuarenta y Dos");
			arbolDePrueba.insertar(52, "Cincuenta y Dos");
			arbolDePrueba.insertar(62, "Sesenta y Dos");
//			arbolDePrueba.insertar(67, "Sesenta y Siete");
//			arbolDePrueba.insertar(48, "Cuarenta y Ocho");

		} catch (ExcepcionClaveYaExiste e) {
			System.out.println(e);
		}
		
		//Tarea 4
		
		// 1. Implementar un método que retorne verdadero si un árbol binario es un montículo, falso en caso contrario. En un montículo el dato que almacena un nodo cualquiera  
		//     siempre es menor que los datos de sus descendientes
		System.out.println("1. Verificar si el arbol es monticulo: "+((ArbolBinarioBusqueda)arbolDePrueba).esMonticulo());
				
		arbolDePrueba = new AVL<>();
		//2. Implementar un método insertar iterativo para dicho árbol. No puede utilizar los metodos insertar o leiminar ya existentes. Tal como establece el árbol AVL solo se debe 
		//	ver la necesidad de balancear por el camino que siguió el algoritmo para insertar hasta volver a la raíz
		try {
			((AVL)arbolDePrueba).insertarIterativo(3, "Tres");
			((AVL)arbolDePrueba).insertarIterativo(2, "Dos");
			((AVL)arbolDePrueba).insertarIterativo(1, "Uno");
			((AVL)arbolDePrueba).insertarIterativo(4, "Cuatro");
			((AVL)arbolDePrueba).insertarIterativo(5, "Cinco");
			((AVL)arbolDePrueba).insertarIterativo(6, "Seis");
			((AVL)arbolDePrueba).insertarIterativo(7, "Siete");
			((AVL)arbolDePrueba).insertarIterativo(16, "Dieciseis");
			((AVL)arbolDePrueba).insertarIterativo(15, "Quince");
			((AVL)arbolDePrueba).insertarIterativo(14, "Catorce");
			((AVL)arbolDePrueba).insertarIterativo(13, "Trece");
			((AVL)arbolDePrueba).insertarIterativo(12, "Doce");
		}catch (ExcepcionClaveYaExiste e) {
			System.out.println(e);
		}
		System.out.println(arbolDePrueba.recorridoPorNiveles());
		
		arbolDePrueba = new ArbolMViasBusqueda<>(4);
		//3. Implementar un método para un árbol de m-vías que retorne cuantos nodos del árbol son padres fuera del nivel n
		try {
			
			arbolDePrueba.insertar(43, "Cuarenta y Tres");
			arbolDePrueba.insertar(47, "Cuarenta y Siete");
			arbolDePrueba.insertar(60, "Sesenta");
			arbolDePrueba.insertar(53, "Cincuenta y Tres");
			arbolDePrueba.insertar(68, "Sesenta y Ocho");
			arbolDePrueba.insertar(45, "Cuarenta y Cinco");
			arbolDePrueba.insertar(50, "Cincuenta");
			arbolDePrueba.insertar(52, "Cincuenta y Dos");
			arbolDePrueba.insertar(58, "Cincuenta y Ocho");
			arbolDePrueba.insertar(48, "Cuarenta y Ocho");
		} catch (ExcepcionClaveYaExiste e) {
			System.out.println(e);
		}
		
		System.out.println("3. Cantidad de nodos padres = "+ ((ArbolMViasBusqueda)arbolDePrueba).nodosPadreFueraDelNivel(0));
		
		
	}
		
	

}
