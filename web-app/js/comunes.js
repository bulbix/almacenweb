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

function autoCompleteProcedimiento(funcSelect){
	autoComplete("#procedimientoauto",url + "/listarProcedimiento","#idProcedimiento",funcSelect)
}

function autoCompleteRecibio(funcSelect){
	autoComplete("#recibeauto", url + "/listarRecibe",null,funcSelect)
}

function autoCompleteAutorizo(funcSelect){
	autoComplete("#autorizaauto", url + "/listarAutoriza",null,funcSelect)
}

////FUNCIONES PERSISTENCIA////////////////////

function guardar(dataDetalle){
	
    var frm = $("#formPadre");
    var dataPadre = JSON.stringify(frm.serializeObject());
    
	var request = $.ajax({
		type:'POST',		
		url:  url +'/guardar',
		async:false,
		data:{
			dataPadre: dataPadre, 
			dataDetalle: dataDetalle,			
			idPadre:$('#idPadre').val()
		},
		dataType:"json"	        
	});
	
	request.done(function(data) {
		$('#idPadre').val(data.idPadre)		
		$('#detalle').trigger("reloadGrid");
		
		$('.botonOperacion').show()
		
		$("#insumo").focus()
	});
}

function actualizar(){
	
	var frm = $("#formPadre");
	var dataPadre = JSON.stringify(frm.serializeObject());

	var request = $.ajax({
		type:'POST',		
		url: url +'/actualizar',
		async:false,
		data:{
			dataPadre: dataPadre,
			idPadre:$('#idPadre').val()
		},
		dataType:"json"	        
	});

	request.done(function(data) {
		$('#detalle').trigger("reloadGrid");
		alert(data.mensaje)
	});
	
}

function cancelar(){	 
	  
	var request = $.ajax({
		type:'POST',		
		url: url + '/cancelar',
		async:false,
		data:{				
			idPadre:$('#idPadre').val()
		},
		dataType:"json"	        
	});

	request.done(function(data) {
		$('#detalle').trigger("reloadGrid");
		$(".botonOperacion").hide()
		$(".busqueda").hide()		
		$("#formPadre :input").each(function(){
			$(this).prop('disabled', true)
		});	
		
		$("#imprimir").show()
		alert(data.mensaje)
	});	
}

function controlesDetalle(){
	
	$("#btnActualizar").click(function(){
		
		var gr = jQuery("#detalle").jqGrid('getGridParam','selrow');
		
		$("#detalle").jqGrid('editGridRow',gr, {
			   editData:{idPadre:$("#idPadre").val()},
			   height:300,
			   width:500,
			   top: 500,
			   left:0,
			   reloadAfterSubmit: true,
			   editCaption:'Editar Detalle',
			   bSubmit:'Actualizar',
			   closeAfterEdit:true,
			   viewPagerButtons:false,
			   afterSubmit: function(response,postdata){
				   console.log(response)
				   var mensaje = jQuery.parseJSON(response.responseText).mensaje				   
				   if(mensaje != 'success')
					   return [false, mensaje, ''];
				   else					   
					   return [true, '', ''];
			   }
		});
	});
	
	$("#btnBorrar").click(function(){
		
		var gr = jQuery("#detalle").jqGrid('getGridParam','selrow');
		
		$("#detalle").jqGrid('delGridRow',gr, {
			   delData:{idPadre:$("#idPadre").val()},
			   height:240,
			   width:500,
			   top: 500,
			   left:0,
			   reloadAfterSubmit: true,
			   editCaption:'Borrar Detalle',
			   bSubmit:'Borrar',
			   closeAfterEdit:true,
			   viewPagerButtons:false,
			   afterSubmit: function (response, postdata) {
				   console.log(response)
				   var mensaje = jQuery.parseJSON(response.responseText).mensaje
				   if(mensaje != 'success')
					   return [false, mensaje, ''];
				   else
					   return [true, '', ''];
				}
		});
	});
}

function limpiarRenglonDetalle(){
	
	$(".busqueda :input").each(function(){
		$(this).val('');
	});	
}

function consultarPaquete(){
	
	$("#detalle").clearGridData();
	
	$.getJSON( url + "/consultarPaquete",{tipo:$("#paqueteq").val(),fecha:$("#fecha").val()})
		.done(function( jsonArray ) {
	        for (var i = 0; i < jsonArray.length; i++) {        	
	        	$("#detalle").addRowData(jsonArray[i].cveArt, jsonArray[i]);
	        }
	})	
	
	
	$(".busqueda").hide()
	$("#guardar").show()
}

function guardarTodo(boton){	
	
	boton.disabled = true
	
    var frm = $("#formPadre");
    var dataPadre = JSON.stringify(frm.serializeObject());
    var dataArrayDetalle = JSON.stringify($("#detalle").getRowData())
    
    //alert(dataDetalle)
    
	var request = $.ajax({
		type:'POST',		
		url:  url +'/guardarTodo',
		async:false,
		data:{
			dataPadre: dataPadre, 
			dataArrayDetalle: dataArrayDetalle,			
			idPadre:$('#idPadre').val()
		},
		dataType:"json"	        
	});
	
	request.done(function(data) {
		$('#idPadre').val(data.idPadre)
		$('#detalle').trigger("reloadGrid");
		$('.botonOperacion').show()
		alert(data.mensaje)
	});
	
	$(boton).hide()
}


