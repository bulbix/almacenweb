package mx.gob.inr.utils

class Partida implements Serializable {
	
	String partida
	String desPart
	String clase
	
     static mapping = {
		table 'partidas_ceye'
		id composite:['partida']
		version false
		cache usage:'read-only'
	}
	
	String toString(){
		return  String.format("(%s) %s",partida, desPart) 
	}
}
