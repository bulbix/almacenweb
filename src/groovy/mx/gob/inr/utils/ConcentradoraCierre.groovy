package mx.gob.inr.utils

class ConcentradoraCierre implements Comparable<ConcentradoraCierre> {
	
	Date fecha
	String tipo
	Integer cantidad
	Double costoPromedio
	Long llave
	String almacen
	
	public ConcentradoraCierre(fecha,tipo,cantidad,costoPromedio,llave,almacen){
		this.fecha = fecha
		this.tipo = tipo
		this.cantidad = cantidad
		this.costoPromedio = costoPromedio
		this.llave = llave
		this.almacen= almacen
		
	}
	
	String toString(){
		return cantidad
	}
	
	@Override
	public int compareTo(ConcentradoraCierre o) {
		int result = fecha.compareTo(o.fecha);

		if(result==0) {
			return tipo.compareTo(o.tipo);
		}
		else {
			return result;
		}
	}
	
}
