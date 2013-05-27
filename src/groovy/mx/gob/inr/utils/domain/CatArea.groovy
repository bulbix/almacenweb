package mx.gob.inr.utils.domain

abstract class CatArea {

	String desArea
	
	String toString(){
		return String.format("(%s) %s", id, desArea)
	}
}
