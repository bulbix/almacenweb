package mx.gob.inr.utils

import javax.annotation.PostConstruct;

import grails.converters.JSON
import mx.gob.inr.materiales.*;
import mx.gob.inr.utils.domain.Entrada;

class EntradaController <E extends Entrada> extends OperacionController<E> {
		
	public EntradaController(entityEntrada){
		super(entityEntrada)
					
	}
		
}
