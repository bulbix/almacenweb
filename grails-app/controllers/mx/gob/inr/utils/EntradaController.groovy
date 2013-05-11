package mx.gob.inr.utils

import javax.annotation.PostConstruct;

import grails.converters.JSON
import mx.gob.inr.materiales.*;

class EntradaController <E extends Entrada> extends OperacionController<E> {
		
	public EntradaController(entityEntrada,almacen){
		super(entityEntrada,almacen)
					
	}
		
}
