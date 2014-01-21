package mx.gob.inr.utils.domain

abstract class CatArea {

	Long id
	String desArea
	
	String toString(){
		return String.format("(%s) %s", id, desArea)
	}
}
