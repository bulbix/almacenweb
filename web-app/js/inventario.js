$(document).ready(function() {
	 $("#fechaInventario").mask("99/99/9999");
	 validar()
	 asignarMarbetes()
})


function validar(){
	$("#formPadre").validate({
		
		ignore: [],
		
        rules: {
        	fechaCierre: {required:true,validateDate:true,dateToday:true,checkInventario:true}  
        },
		messages: {
			fechaCierre : {required:"Requerido"}
		}
  });
}

function asignarMarbetes(){
	
	$("#asignar").click( function() {
		
		if($("#fechaCierre").valid()){
			$.getJSON(url + "/asignarMarbetes",{marbeteInicial:$("#marbeteInicial").val(),fechaCierre:$("#fechaCierre").val()}).done(function( json ) {
				 $("#mensaje").html("Marbete Final " + json.marbeteFinal)				 
			})
		}
	});
	
}