$(document).ready(function() {		
	$.datepicker.setDefaults($.datepicker.regional['es']);	
})


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

function autoComplete(input,url,hidden,funcSelect, minimumTrigger){
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
		  minLength: minimumTrigger, // triggered only after minimum 2 characters have been entered.
		  autoFocus: true,
		  select: function(event, ui) { // event handler when user selects a company from the list.
			  $(hidden).val(ui.item.id); // update the hidden field.
			  
			  funcSelect();
		  }
	 });
	
}

function autoCompletePaciente(funcSelect){
	autoComplete("#pacienteauto","/almacenWeb/autoComplete/listarPaciente","#idPaciente",funcSelect,4)
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
	},4)	
}

function autoCompleteArea(funcSelect){
	autoComplete("#areaauto",url + "/listarArea","#cveArea",funcSelect,1)
}

function autoCompleteProcedimiento(funcSelect){
	autoComplete("#procedimientoauto",url + "/listarProcedimiento","#idProcedimiento",funcSelect,1)
}

function autoCompleteRecibio(funcSelect){
	autoComplete("#recibeauto", url + "/listarRecibe",null,funcSelect,4)
}

function autoCompleteAutorizo(funcSelect){
	autoComplete("#autorizaauto", url + "/listarAutoriza",null,funcSelect,4)
}

function autoCompleteSolicita(funcSelect){
	autoComplete("#solicitaauto", url + "/listarSolicita",null,funcSelect,4)
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
		mostrarMensaje(data.mensaje)
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
		mostrarMensaje(data.mensaje)
	});	
}

function controlesDetalle(){
	
	$("#btnActualizar").click(function(){
		
		var gr = jQuery("#detalle").jqGrid('getGridParam','selrow');
		
		$("#detalle").jqGrid('editGridRow',gr, {
			   editData:{idPadre:$("#idPadre").val()},
			   height:300,
			   width:500,			  
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
		mostrarMensaje(data.mensaje)
	});
	
	$(boton).hide()
}


function mostrarMensaje(mensaje, status){
	
	var image = ""
	
	if(status != undefined){
		
		switch(status){
		
		case 'ok':
			image = "<img  src='/almacenWeb/images/icons/paloma.gif' />"
			break
		case 'error':
			image = "<img  src='/almacenWeb/images/icons/error_message.jpg' />"
			break
		
		}
	}
	
	var html = "<p>" + image + mensaje + "</p>"
	
	$( "#dialog-mensaje" ).dialog({
		  title:'Mensaje',
		  autoOpen: false,
	      modal: true,
	      buttons: {
	        Ok: function() {
	          $( this ).dialog( "close" );
	        }
	      },
	      resizable: false
	})
	
	
	
	$("#dialog-mensaje" ).html(html)	       					
	$("#dialog-mensaje" ).dialog("open");	
}


function mostrarConfirmacion(mensaje, functionSi){
	
	$( "#dialog-confirm" ).dialog({
		  title:'Confirmacion',
	      resizable: false,
	      height:200,
	      modal: true,
	      buttons: {
	        "Si": function(){
	        	functionSi(); 
	        	$( this ).dialog( "close" )
	        },
	        "No": function() {
	          $( this ).dialog( "close" );
	        }
	      }
	});
	
	$("#dialog-confirm" ).html(mensaje)	       					
	$("#dialog-confirm" ).dialog("open");	
	
}

