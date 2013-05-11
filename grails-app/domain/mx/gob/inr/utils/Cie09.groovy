package mx.gob.inr.utils

class Cie09 {
	
	Integer clavediag
	String descdiag

	
	static mapping = {
		table:'cie09'
		id column:'iddiagnostico'
		version false
	}
	
	
    static constraints = {
    }
	
	String toString(){
		return  String.format("(%s) %s",clavediag ,descdiag)
	}
	
}
