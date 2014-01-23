package mx.gob.inr.utils.reportes

import mx.gob.inr.utils.domain.CatArea;

class ConcentradoServicio {	
	
	def storage = [:]
	def propertyMissing(String name, value) { storage[name] = value }
	def propertyMissing(String name) { storage[name] }
	
	Integer cveArt
	String descArticulo
	String unidad
	//String partida
	Integer cantidad
	CatArea area
	Long cveArea
	
	 Integer cantidad_area101=0
	 Integer cantidad_area102=0
	 Integer cantidad_area103=0
	 Integer cantidad_area104=0
	 Integer cantidad_area105=0
	 Integer cantidad_area106=0
	 Integer cantidad_area107=0
	 Integer cantidad_area108=0
	 Integer cantidad_area109=0
	 Integer cantidad_area110=0
	 Integer cantidad_area111=0
	 Integer cantidad_area112=0
	 Integer cantidad_area113=0
	 Integer cantidad_area114=0
	 Integer cantidad_area115=0
	 Integer cantidad_area201=0
	 Integer cantidad_area202=0
	 Integer cantidad_area203=0
	 Integer cantidad_area204=0
	 Integer cantidad_area205=0
	 Integer cantidad_area206=0
	 Integer cantidad_area207=0
	 Integer cantidad_area208=0
	 Integer cantidad_area209=0
	 Integer cantidad_area210=0
	 Integer cantidad_area211=0
	 Integer cantidad_area212=0
	 Integer cantidad_area213=0
	 Integer cantidad_area214=0
	 Integer cantidad_area301=0
	 Integer cantidad_area302=0
	 Integer cantidad_area303=0
	 Integer cantidad_area304=0
	 Integer cantidad_area401=0
	 Integer cantidad_area501=0
	 Integer cantidad_area502=0
	 Integer cantidad_area503=0
	 Integer cantidad_area504=0
	 Integer cantidad_area505=0
	 Integer cantidad_area506=0
	 Integer cantidad_area601=0
	 Integer cantidad_area602=0
	 Integer cantidad_area603=0
	 Integer cantidad_area604=0
	 Integer cantidad_area605=0
	 Integer cantidad_area606=0
	 Integer cantidad_area607=0
	 Integer cantidad_area608=0
	 Integer cantidad_area609=0
	 Integer cantidad_area610=0
	 Integer cantidad_area611=0
	 Integer cantidad_area701=0
	 Integer cantidad_area702=0
	 Integer cantidad_area703=0
	 Integer cantidad_area704=0
	 Integer cantidad_area705=0
	 Integer cantidad_area801=0
	 Integer cantidad_area802=0
	 Integer cantidad_area803=0
	 Integer cantidad_area804=0
	 Integer cantidad_area805=0
	 Integer cantidad_area901=0
	 Integer cantidad_area902=0
	 Integer cantidad_area903=0
	 Integer cantidad_area1000=0
	 Integer cantidad_area1001=0
	
	 Integer getSumaCantidadArea(){
		 return cantidad_area101+
		 cantidad_area102+
		 cantidad_area103+
		 cantidad_area104+
		 cantidad_area105+
		 cantidad_area106+
		 cantidad_area107+
		 cantidad_area108+
		 cantidad_area109+
		 cantidad_area110+
		 cantidad_area111+
		 cantidad_area112+
		 cantidad_area113+
		 cantidad_area114+
		 cantidad_area115+
		 cantidad_area201+
		 cantidad_area202+
		 cantidad_area203+
		 cantidad_area204+
		 cantidad_area205+
		 cantidad_area206+
		 cantidad_area207+
		 cantidad_area208+
		 cantidad_area209+
		 cantidad_area210+
		 cantidad_area211+
		 cantidad_area212+
		 cantidad_area213+
		 cantidad_area214+
		 cantidad_area301+
		 cantidad_area302+
		 cantidad_area303+
		 cantidad_area304+
		 cantidad_area401+
		 cantidad_area501+
		 cantidad_area502+
		 cantidad_area503+
		 cantidad_area504+
		 cantidad_area505+
		 cantidad_area506+
		 cantidad_area601+
		 cantidad_area602+
		 cantidad_area603+
		 cantidad_area604+
		 cantidad_area605+
		 cantidad_area606+
		 cantidad_area607+
		 cantidad_area608+
		 cantidad_area609+
		 cantidad_area610+
		 cantidad_area611+
		 cantidad_area701+
		 cantidad_area702+
		 cantidad_area703+
		 cantidad_area704+
		 cantidad_area705+
		 cantidad_area801+
		 cantidad_area802+
		 cantidad_area803+
		 cantidad_area804+
		 cantidad_area805+
		 cantidad_area901+
		 cantidad_area902+
		 cantidad_area903+
		 cantidad_area1000+
		 cantidad_area1001
		
	}
		
	
}
