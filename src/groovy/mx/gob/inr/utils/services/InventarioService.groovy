package mx.gob.inr.utils.services

import java.util.Date;

import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.domain.Cierre

abstract class InventarioService {
	
	public UtilService utilService
	
	protected entityCierre
	protected entityInventario
	protected String almacen
	
	public InventarioService(entityCierre, entityInventario, almacen){
		this.entityCierre = entityCierre		
		this.entityInventario = entityInventario
		this.almacen = almacen
	}
	
	def asignarMarbetes(Date fecha, Integer marbeteInicial){	
		
		def marbeteFinal = marbeteInicial		
		
		def cierreList = entityCierre.createCriteria().list(){
			articulo{				
			}			
			gt("existencia",0)
			eq("fechaCierre",fecha)
			eq("almacen",almacen)
			order("articulo","asc")			
		}.eachWithIndex(){ cierre, index ->
		
			 def inventario = entityInventario.newInstance()
			 inventario.fechInv = fecha
			 inventario.conteo1 = null
			 inventario.conteo2 = null
			 inventario.almacen = almacen
			 inventario.articulo = cierre.articulo
			 marbeteFinal  = marbeteInicial + index
			 inventario.marbete = marbeteFinal 			 
			 inventario.save([validate:false])
		}
		
		marbeteFinal				
	}
	
	
	def checkInventario(Date fecha){
		
		return entityInventario.findWhere(fechInv:fecha)!= null
	}
		

}
