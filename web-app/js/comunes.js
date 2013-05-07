$.fn.serializeObject = function()
{
   var o = {};
   var a = this.serializeArray();
   $.each(a, function() {
       if (o[this.name]) {
           if (!o[this.name].push) {
               o[this.name] = [o[this.name]];
           }
           o[this.name].push(this.value || '');
       } else {
           o[this.name] = this.value || '';
       }
   });
   return o;
};

function autoComplete(input,url,hidden,funcSelect){
	$(input).autocomplete({
		  source: function(request, response){
		   $.ajax({
		    url: url, // remote datasource
		    data: request,
		    success: function(data){
		     response(data); // set the response
		    },
		    error: function(){ // handle server errors
		     $.jGrowl("Unable to retrieve Companies", {
		      theme: 'ui-state-error ui-corner-all'   
		     });
		    }
		   });
		  },
		  minLength: 2, // triggered only after minimum 2 characters have been entered.
		  select: function(event, ui) { // event handler when user selects a company from the list.
			  $(hidden).val(ui.item.id); // update the hidden field.
			  
			  funcSelect();
		  }
	 });
	
}


function autoCompletePaciente(funcSelect){
	autoComplete("#pacienteauto","/almacenWeb/autoComplete/listarPaciente","#idPaciente",funcSelect)
}

function autoCompleteArticulo(funcSelect){	
	autoComplete("#artauto", url +"/listarArticulo","#insumo",
			function(){
		  		$.getJSON(url + "/buscarArticulo",{id:$("#insumo").val()})
		  		.done(function( json ) {
		  	     $("#desArticulo").val(json.desArticulo)
				 $("#unidad").val(json.unidad)
				 
				  $("#costo").val(json.movimientoProm)
				  $("#costo").currency({ region: 'MXN', thousands: ',', decimal: '.', decimals: 4 })
				 funcSelect()
		  	})	
	})	
}

function autoCompleteArea(funcSelect){
	autoComplete("#areaauto",url + "/listarArea","#cveArea",funcSelect)
}

function autoCompleteRecibio(funcSelect){
	autoComplete("#recibeauto", url + "/listarRecibe",null,funcSelect)
}


function autoCompleteAutorizo(funcSelect){
	autoComplete("#autorizaauto", url + "/listarAutoriza",null,funcSelect)
}
