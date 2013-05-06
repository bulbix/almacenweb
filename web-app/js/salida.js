$(document).ready(function() {	

	//alert(url)
	$("#areaauto").focus()
	
	autoCompleteArticulo(function(){
						 
		 disponibilidadArticulo($("#insumo").val(),$("#fechaSalida").val());
		 $("#solicitado").focus()
	});
	autoCompleteArea(function(){
		$("#pacienteauto").focus()
	});
	autoCompletePaciente(function(){
		$("#entrega").focus()
	});
	
	autoCompleteRecibio(function(){
		$("#autorizaauto").focus()	
	})
	
	autoCompleteAutorizo(function(){
		$("#insumo").focus()		
	})
	
	
	jqGridDetalle();
	detalleAdd();
	capturaSalida();
	validar();
	
	controlesDetalle()
	controlesHead()
});


function validar(){
	$("#formSalida").validate({
		
		ignore: [],
		
        rules: {
        		fechaSalida: {required:true,validateDate:true,dateToday:true},
        		//folioSalida: {required:true, uniqueFolioSalida:true},
        		cveArea:{required:true},
        		recibeauto:{required:true},
        		autorizaauto:{required:true},
        		insumo: {required:true,number:true,checkInsumo:true},
                solicitado: {required:true,number:true},
                surtido: {required:true,number:true,checkExistencia:true}
        },
		messages: {
				fechaSalida : {required:"Requerido"},
				//folioSalida:{required:"Requerido"},
				cveArea:{required:"Requerido"},
				recibeauto:{required:"Requerido"},
        		autorizaauto:{required:"Requerido"},
				insumo :{required:"Requerido",number:"Numerico"},
				solicitado : {required:"Requerido",number:"Numerico"},
				surtido:{required:"Requerido",number:"Numerico"}
		}
  });
}

function capturaSalida(){
	
	var area = $("#areaauto")

	area.change( function() {
		if(area.val() == "")
			$("#cveArea").val("")
	});
	
	var paciente = $("#pacienteauto")
	
	paciente.change( function() {
		if(paciente.val() == "")
			$("#idPaciente").val("")
	});
	
	
	$("#entrega").keypress(function(e){	
		 if(e.which == 13) {
			$("#recibeaauto").focus()		
		 }
	});
	
	$("#recibeaauto").keypress(function(e){	
		 if(e.which == 13) {
			$("#autorizaauto").focus()		
		 }
	});
	
	$("#autorizaauto").keypress(function(e){	
		 if(e.which == 13) {
			$("#insumo").focus()		
		 }
	});
	

}


function jqGridDetalle(){
	
	//alert($("#idSalida").val())
	
	$("#salidadetalle").jqGrid({
	    url: url + '/consultarSalidaDetalle',
	    datatype: 'json',
	    mtype: 'GET',
	    colNames:['Clave','Descripcion', 'U. Medida','Costo','Disponible','Solicitado','Surtido'],
	    colModel :[ 
	      {name:'cveArt', index:'cveArt', width:50, align:'center',editable:false}, 
	      {name:'desArticulo', index:'desArticulo', width:500,editable:false},	      
	      {name:'unidad', index:'unidad', width:100,editable:false,align:'center'},
	      {name:'costo', index:'costo', width:60,editable:false,align:'right',formatter: 'currency', formatoptions: { decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 4, prefix: "$", suffix:"", defaultValue: '0.00'}},
	      {name:'disponible', index:'disponible', width:60,editable:false,align:'center'},
	      {name:'solicitado', index:'solicitado', width:60,editable:true,align:'center'},	      
	      {name:'surtido', index:'surtido', width:60,editable:true,align:'center'}
	    ],
	    postData:{idSalida: function() { return $('#idSalida').val() }},
	    onSelectRow: function(id){	
		},		
		afterInsertRow:function (rowid, 
				rowdata,
				rowelem){
		},
	    pager: '#pager',
	    editurl: url + "/actualizarSalidaDetalle",
	    rowNum:20,
	    rowList:[20, 40, 60 ,80],
	    //sortname: 'id',
	    //sortorder: 'asc',
	    viewrecords: true,
	    gridview: true,
	    caption: 'Salida Detalle'//,
	    //multiselect: true
	})	
	
	$("#salidadetalle").jqGrid('setGridWidth', 950);
	$("#salidadetalle").jqGrid('setGridHeight', 250);
	
	$("#salidadetalle").jqGrid("navGrid", "#pager",
	{
		add: false,
		edit: false,
		del: false,
		search: true,
		refresh: false
	},	
	{//edit
	},
	{},//add
	{//del
		savekey: [true, 13],
		width:500,
		closeOnEscape: true,
		reloadAfterSubmit:true,
		closeAfterSubmit:true,
		afterSubmit: function (data) {
			alert('prueb')
			return [true,'',''];
		}
	},	
	{},
	{});
	
    //$('#salidadetalle').jqGrid('inlineNav',"#pager");
	
}

function controlesHead(){
	$("#actualizar").click(function(){
		
		if( $("#cveArea").valid() && $("#fechaSalida").valid() 
		    && $("#recibeauto").valid() && $("#autorizaauto").valid() ){
			actualizarSalida();
		}		
			
	})
	
	$("#cancelar").click(function(){
		cancelarSalida()
	})
	
}


function controlesDetalle(){
	
	$("#btnActualizar").click(function(){
		//alert('prueba')
		var gr = jQuery("#salidadetalle").jqGrid('getGridParam','selrow');
		
		///alert(gr)
		
		$("#salidadetalle").jqGrid('editGridRow',gr, {
			   editData:{idSalida:$("#idSalida").val()},
			   height:240,
			   reloadAfterSubmit: true,
			   editCaption:'Editar Detalle',
			   bSubmit:'Actualizar',
			   width:500,
			   //url:'someurl.php',
			   closeAfterEdit:true,
			   viewPagerButtons:false,
			   afterComplete: function(data){
				   //alert(data)
			   }
		});
	});
	
	$("#btnBorrar").click(function(){
		
		var gr = jQuery("#salidadetalle").jqGrid('getGridParam','selrow');
		
		$("#salidadetalle").jqGrid('delGridRow',gr, {
			   delData:{idSalida:$("#idSalida").val()},
			   height:240,
			   reloadAfterSubmit: true,
			   editCaption:'Borrar Detalle',
			   bSubmit:'Borrar',
			   width:500,
			   //url:'someurl.php',
			   closeAfterEdit:true,
			   viewPagerButtons:false,
			   afterComplete: function(data){
				   //alert(data)
			   }
		});
	});
}


function limpiarRenglonSalidaDetalle(){
	
	$("#tblBusqueda :input").each(function(){
		$(this).val('');
	});	
}


function detalleAdd(){
	
	$("#insumo").keypress(function(e){	
		 if(e.which == 13 && $("#insumo").valid() ) {
			$.getJSON("/almacenWeb/util/buscarArticulo",{id:this.value})
					.done(function( json ) {
						 $("#artauto").val(json.desArticulo)
						 $("#desArticulo").val(json.desArticulo)
						 $("#unidad").val(json.unidad)
						 $("#costo").val(json.movimientoProm)
						 $("#costoDisplay").val(json.movimientoProm)
						 $("#costoDisplay").currency({ region: 'MXN', thousands: ',', decimal: '.', decimals: 4 })						 
						 disponibilidadArticulo($("#insumo").val(),$("#fechaSalida").val());	
						 $("#solicitado").focus()
						 
			})
		
			
		 }
	});
	
	$("#solicitado").keypress(function(e){	
		 if(e.which == 13 && $("#solicitado").valid()) {
			$("#surtido").focus()		
		 }
	});
	
	$("#surtido").keypress(function(e){	
		 if(e.which == 13) {
			 
			 if($("#formSalida").valid()){			 
				 var data = [{ cveArt:$("#insumo").val(),
					 		   desArticulo:$("#desArticulo").val(),
					 		   unidad:$("#unidad").val(),
					 		   costo:$("#costo").val(),
					 		   disponible:$("#disponible").val(),
					 		   solicitado:$("#solicitado").val(),					 		   
					 		   surtido:$("#surtido").val()
					 		}];
				 
				 guardarSalida(JSON.stringify(data))			 
				
				 
				 $("#clavelast").html($("#insumo").val());
				 $("#deslast").html($("#desArticulo").val());
				 $("#unidadlast").html($("#unidad").val());
				 $("#costolast").html($("#costo").val());				
				 $("#solicitadolast").html($("#solicitado").val());
				 $("#surtidolast").html($("#surtido").val());			 
			
				 
				 $('#salidadetalle').trigger("reloadGrid");
				 //jQuery("#salidadetalle").addRowData($("#insumo").val(), data);			 
				 
				 limpiarRenglonSalidaDetalle()
				 $("#insumo").focus()
			 }
		 }
	});	
}


function guardarSalida(detalleData){
	
    var frm = $("#formSalida");
    var salidaData = JSON.stringify(frm.serializeObject());
    
	var request = $.ajax({
		type:'POST',		
		url:  url +'/guardarSalida',
		async:false,
		data:{
			salidaData: salidaData, 
			detalleData: detalleData,			
			idSalida:$('#idSalida').val()
		},
		dataType:"json"	        
	});
	
	request.done(function(data) {
		//alert(data.idSalida)
		$('#idSalida').val(data.idSalida)		
		$("#disponiblelast").html(data.disponible)
	});
}

function actualizarSalida(){
	
	  var frm = $("#formSalida");
	  var salidaData = JSON.stringify(frm.serializeObject());
	  
	  var request = $.ajax({
			type:'POST',		
			url: url +'/actualizarSalida',
			async:false,
			data:{
				salidaData: salidaData,
				idSalida:$('#idSalida').val()
			},
			dataType:"json"	        
		});
		
		request.done(function(data) {
			//$('#idSalida').val(data.idSalida)
			alert(data.mensaje)
		});
	
}

function cancelarSalida(){	 
	  
	  var request = $.ajax({
			type:'POST',		
			url: url + '/cancelarSalida',
			async:false,
			data:{				
				idSalida:$('#idSalida').val()
			},
			dataType:"json"	        
		});
		
		request.done(function(data) {
			//$('#idSalida').val(data.idSalida)
			alert(data.mensaje)
		});
	
}

function disponibilidadArticulo(clave, fecha){
	
	 $.ajax({
	        type: "POST",
	        url: url + '/disponibilidadArticulo',
	        async:false,
	        data: {clave:clave,fecha:fecha},
	        dataType:"json",
	     success: function(msg)
	     {        	 
	    	 $("#disponible").val(msg.disponible)         
	     }
	 })
}



