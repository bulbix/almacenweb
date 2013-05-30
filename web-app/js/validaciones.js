$.validator.addMethod("uniqueFolio", function(value, element) {		
	
	var result = true
	
	if($("#idPadre").val() == ''){
		result = checkValue(url + "/uniqueFolio", value) 
	}
	
	return result
	
}, "Folio ya existe");

$.validator.addMethod("uniqueFolioSalAlma", function(value, element) {		
	
	return checkValue( url + "/uniqueFolioSalAlma", value)
	
}, "Folio no existe o utilizado");

$.validator.addMethod("checkExistencia", function(value, element) {		
	
	return  parseInt(jQuery(element).val()) <= parseInt($("#disponible").val())
	
}, "Existencia Insuficiente");

$.validator.addMethod("checkCierre", function(value, element) {		
	
	return checkValue( url + "/checkCierre", value)
	
}, "Cierre Existente");


$.validator.addMethod("checkInventario", function(value, element) {		
	
	return checkValue( url + "/checkInventario", value)
	
}, "Inventario Existente");


$.validator.addMethod("checkInsumo", function(value, element) {
	
	var gridData = jQuery("#detalle").getRowData();
	
	var result = true
    
    jQuery.each(gridData, function(i, item) {
    	
    	if(item.cveArt == value )
    		result = false

    })
    
	return result
	
}, "Clave Existente");


function checkValue(url, value){
	
	var response
    $.ajax({
	        type: "POST",
	        url: url,
	        async:false,
	        data: "value="+value,
	        dataType:"html",
	     success: function(msg)
	     {        	 
	         response = ( msg == 'true' ) ? true : false;         
	     }
    })
 return response;
	
}

$.validator.addMethod("validateDate", function (value, element) {
	try {

		if (!value) {
			return true;
		}

		return !isNaN(Date.parse(value));

	}
	catch (e) {		
		return false;
	}
	}, "Fecha Incorrecta"
);


$.validator.addMethod("dateToday", function (value, element) {
	try {
			
		
		var exactDate= new Date();
        var year= exactDate.getFullYear();
        var month= exactDate.getMonth();
        var day= exactDate.getDate();
        var startDateObj= new Date(year,month,day,0,0,0,0);
        
        var endDateArray= value.split("/");
		var endDateObj= new Date(endDateArray[2],(endDateArray[1] - 1 ), endDateArray[0] ,0,0,0,0);
		var startDateMilliseconds= startDateObj.getTime();
		 var endDateMilliseconds= endDateObj.getTime();
		
		if(endDateMilliseconds > startDateMilliseconds )
			return false
		else
			return true

	}
	catch (e) {		
		return false;
	}
	}, "Fecha Mayor a Fecha Actual"
);


