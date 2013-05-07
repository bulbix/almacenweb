$(document).ready(function() {	
	$("#remision").focus()
	
	autoCompleteArticulo(function(){
		$("#cantidad").focus()
	});
	
	autoCompleteArea(function(){
		$("#registra").focus()
	});
	
	jqGridDetalle();
	detalleAdd();
	capturaEntrada();
	validar();
	
	controlesDetalle()
	controlesHead()
	
});

function capturaEntrada(){
	
	$("#fechaEntrada").keypress(function(e){	
		 if(e.which == 13) {
			$("#folioEntrada").focus()		
		 }
	});
	
	$("#folioEntrada").keypress(function(e){	
		 if(e.which == 13) {
			$("#remision").focus()		
		 }
	});
	
	$("#remision").keypress(function(e){	
		 if(e.which == 13) {
			$("#registra").focus()		
		 }
	});
	
	$("#remision").change(function(){
		if(this.value != ''){
			$("#folioAlmacen").val('');
			$("#folioAlmacen").prop('disabled', true);
		}
		else{
			$("#folioAlmacen").prop('disabled', false);
		}
	});	
	
	$("#registra").keypress(function(e){	
		 if(e.which == 13) {
			$("#supervisa").focus()		
		 }
	});
	
	$("#supervisa").keypress(function(e){	
		 if(e.which == 13) {
			$("#recibe").focus()		
		 }
	});
	
	$("#recibe").keypress(function(e){	
		 if(e.which == 13) {
			$("#insumo").focus()		
		 }
	});
	
	$("#folioAlmacen").keypress(function(e){	
		 if(e.which == 13) {
			 if($("#folioAlmacen").valid()){
				 jqGridMateriales();
				
			 }		
		 }
	});
	
	$("#folioAlmacen").focus(function(){
		$("#entradadetalle").clearGridData();
		 $("#tblBusqueda").show()
	});
	
	$("#folioAlmacen").change(function(){
		
		if(this.value != ''){
			$("#remision").val('');
			$("#remision").prop('disabled', true);
		}
		else{
			$("#remision").prop('disabled', false);
		}
	});
}

function jqGridMateriales(){
	$("#entradadetalle").clearGridData();	
	$.getJSON( url + "/jqGridMaterial",{folioAlmacen:$("#folioAlmacen").val()})
	.done(function( json ) {
        for (var i = 0; i < json.rows.length; i++) {        	
        	$("#entradadetalle").addRowData(i+1, json.rows[i]);
        }
        
        //alert(json.idSalAlma)
        
        $("#idSalAlma").val(json.idSalAlma)
	})	
	 $("#registra").focus()
	 $("#tblBusqueda").hide()
}

function validar(){
	$("#formEntrada").validate({
		
		ignore: [],
		
        rules: {
                fechaEntrada: {required:true,validateDate:true},                
                insumo: {required:true, number:true,checkInsumo:true},
                precio: {required:true, number:true}
        },
		messages: {
				fechaEntrada : {required:"Requerido"},
				insumo :{required:"Requerido", number:"Numerico"},
				precio: {required:"Requerido", number:"Numerico"}
							
		}
  });
}

function maskDates(){
	 $("#fechaEntrada").mask("99/99/9999");	
	 $("#fechaCaducidad").mask("99/99/9999");
}

function limpiarRenglonEntradaDetalle(){
	
	$("#tblBusqueda :input").each(function(){
		$(this).val('');
	});	
}

function detalleAdd(){
	
	$("#insumo").keypress(function(e){	
		 if(e.which == 13) {
			$.getJSON(url + "/buscarArticulo",{id:this.value})
					.done(function( json ) {
						 $("#artauto").val(json.desArticulo)
						 $("#desArticulo").val(json.desArticulo)
						 $("#unidad").val(json.unidad)
						 $("#cantidad").focus()
			})	
		 }
	});
	
	$("#cantidad").keypress(function(e){	
		 if(e.which == 13) {
			$("#precio").focus()		
		 }
	});
	
	$("#precio").keypress(function(e){	
		 if(e.which == 13) {
			$("#noLote").focus()		
		 }
	});
	
	$("#noLote").keypress(function(e){	
		 if(e.which == 13) {
			$("#fechaCaducidad").focus()		
		 }
	});
	
	$("#fechaCaducidad").keypress(function(e){	
		 if(e.which == 13) {
			 var data = [{	cveArt:$("#insumo").val(),
				 			desArticulo:$("#desArticulo").val(),
				 			unidad:$("#unidad").val(),
				 			cantidad:$("#cantidad").val(),
				 			precioEntrada:$("#precio").val(),
				 			noLote:$("#noLote").val(),
				 			fechaCaducidad:$("#fechaCaducidad").val()}];
			 
			 guardarEntrada(JSON.stringify(data))
			 
			 $("#clavelast").html($("#insumo").val());
			 $("#deslast").html($("#desArticulo").val());
			 $("#unidadlast").html($("#unidad").val());
			 $("#cantidadlast").html($("#cantidad").val());				
			 $("#preciolast").html($("#precio").val());
			 $("#lotelast").html($("#noLote").val());
			 $("#caducidadlast").html($("#fechaCaducidad").val());
		
			 
			 $('#entradadetalle').trigger("reloadGrid");			 
			 			 
			 limpiarRenglonEntradaDetalle()
			 $("#insumo").focus()
		 }
	});
	
	
}

function guardarEntrada(detalleData){
	
    var frm = $("#formEntrada");
    var entradaData = JSON.stringify(frm.serializeObject());
    
	var request = $.ajax({
		type:'POST',		
		url:  url +'/guardarEntrada',
		async:false,
		data:{
			entradaData: entradaData, 
			detalleData: detalleData,			
			idEntrada:$('#idEntrada').val()
		},
		dataType:"json"	        
	});
	
	request.done(function(data) {
		$('#idEntrada').val(data.idEntrada)
	});
}

function jqGridDetalle(){
	$("#entradadetalle").jqGrid({
	    url: url +'/consultarEntradaDetalle',
	    datatype: 'json',
	    mtype: 'GET',
	    colNames:['Clave','Descripcion', 'U. Medida','Cantidad','Precio U.','Lote','F. Caducidad'],
	    colModel :[ 
	      {name:'cveArt', index:'cveArt', width:50, align:'center',editable:true}, 
	      {name:'desArticulo', index:'desArticulo', width:500,editable:false},	      
	      {name:'unidad', index:'unidad', width:100,editable:false,align:'center'},
	      {name:'cantidad', index:'cantidad', width:50,editable:true,align:'center'},
	      {name:'precioEntrada', index:'precioEntrada', width:50,editable:true,align:'right',formatter: 'currency', formatoptions: { decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 4, prefix: "$", suffix:"", defaultValue: '0.00'}},
	      {name:'noLote', index:'noLote', width:100,editable:true},
	      {name:'fechaCaducidad', index:'fechaCaducidad', width:100,editable:true,sorttype:'date',align:'center'}
	    ],
	    postData:{idEntrada:function() { return $('#idEntrada').val() }},
	    onSelectRow: function(id){	
		},		
		afterInsertRow:function (rowid, 
				rowdata,
				rowelem){
		},
	    pager: '#pager',
	    editurl: url + "/actualizarEntradaDetalle",
	    rowNum:20,
	    rowList:[20, 40, 60 ,80],
	    //sortname: 'id',
	    //sortorder: 'asc',
	    viewrecords: true,
	    gridview: true,
	    caption: 'Entrada Detalle'			   
	})	
	
	$("#entradadetalle").jqGrid('setGridWidth', 950);
	$("#entradadetalle").jqGrid('setGridHeight', 250);
	
	$("#entradadetalle").jqGrid("navGrid", "#pager",
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
	
}


function controlesHead(){
	$("#actualizar").click(function(){
		
		if($("#fechaEntrada").valid()){
			actualizarEntrada();
		}		
			
	})
	
	$("#cancelar").click(function(){
		cancelarEntrada()
	})
	
}


function controlesDetalle(){
	
	$("#btnActualizar").click(function(){
		
		var gr = jQuery("#entradadetalle").jqGrid('getGridParam','selrow');
		
		$("#entradadetalle").jqGrid('editGridRow',gr, {
			   editData:{idEntrada:$("#idEntrada").val()},
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
		
		var gr = jQuery("#entradadetalle").jqGrid('getGridParam','selrow');
		
		$("#salidadetalle").jqGrid('delGridRow',gr, {
			   delData:{idEntrada:$("#idEntrada").val()},
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

function actualizarEntrada(){
	
	  var frm = $("#formEntrada");
	  var entradaData = JSON.stringify(frm.serializeObject());
	  
	  var request = $.ajax({
			type:'POST',		
			url: url +'/actualizarSalida',
			async:false,
			data:{
				entradaData: entradaData,
				idEntrada:$('#idEntrada').val()
			},
			dataType:"json"	        
		});
		
		request.done(function(data) {
			//$('#idSalida').val(data.idSalida)
			alert(data.mensaje)
		});
	
}

function cancelarEntrada(){	 
	  
	  var request = $.ajax({
			type:'POST',		
			url: url + '/cancelarSalida',
			async:false,
			data:{				
				idEntrada:$('#idEntrada').val()
			},
			dataType:"json"	        
		});
		
		request.done(function(data) {
			//$('#idSalida').val(data.idSalida)
			alert(data.mensaje)
		});
	
}


