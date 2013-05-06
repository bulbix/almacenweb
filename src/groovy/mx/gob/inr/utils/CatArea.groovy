package mx.gob.inr.utils

abstract class CatArea {

	String desArea
	
	String toString(){
		return String.format("(%s) %s", id, desArea)
	}
}
