$(document).ready(function() {	
	$("#fecha").focus()
	
	autoCompleteArticulo(function(){
		$("#cantidad").focus()
	});
	
	autoCompleteArea(function(){
		$("#registra").focus()
	});
	
	consultarDetalle();
	detalleAdd();
	capturar();
	validar();	
	controlesDetalle()
	controlesHead()
	maskDates()
	
});

function validar(){
	$("#formPadre").validate({
		
		ignore: [],
		
        rules: {
                fecha: {required:true,validateDate:true,dateToday:true},                
                folio: {required:true, number:true, uniqueFolio:true},
                folioAlmacen:{number:true, uniqueFolioSalAlma:true},
                remision: {required: function() {
                    return $('#folioAlmacen').val() == undefined || $('#folioAlmacen').val() == '' ;
                }},
                registra:{required:true},
                supervisa:{required:true},
                recibe:{required:true},                
                insumo: {required:true, number:true,checkInsumo:true},
                cantidad: {required:true},
                precio: {required:true, number:true}
        },
		messages: {
				fecha : {required:"Requerido"},
				folio:{required:"Requerido",number:"Numerico"},
				//folioAlmacen:{numeric:"Requerido"},
				remision: {required:"Requerido"},
				registra:{required:"Requerido"},
				supervisa:{required:"Requerido"},
				recibe:{required:"Requerido"},
				insumo :{required:"Requerido", number:"Numerico"},
				cantidad:{required:"Requerido"},
				precio: {required:"Requerido", number:"Numerico"}
							
		}
  });
}

function capturar(){
	
	$("#fecha").keypress(function(e){	
		 if(e.which == 13) {
			$("#folio").focus()		
		 }
	});
	
	$("#folio").keypress(function(e){	
		 if(e.which == 13) {
			$("#remision").focus()		
		 }
	});
	
	/*$("#remision").focus(function(){		
		$(".busqueda").show()
	});*/
	
	$("#remision").keypress(function(e){	
		 if(e.which == 13) {
			 if($("#areaauto").val() != undefined)
					$("#areaauto").focus()
				else		
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
	
	var area = $("#areaauto")

	area.change( function() {
		if(area.val() == "")
			$("#cveArea").val("")
	});
	
	area.keypress(function(e){	
		 if(e.which == 13) {
			 $("#registra").focus()	
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
	
	
	$("#folioAlmacen").focus(function(){
		$("#detalle").clearGridData();
		$("#guardar").hide()
		$(".busqueda").hide()
	});
	
	$("#folioAlmacen").keypress(function(e){	
		 if(e.which == 13) {
			 consultarDetalleMaterial();	
		 }
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
	
	
	$("#paqueteq").change(function(){
		
		if($("#paqueteq").val() == ''){
			$("#guardarPaquete").hide()
			$(".busqueda").show()
		}
		else{
			$("#guardarPaquete").show()
			$(".busqueda").hide()
		}
		
		consultarPaquete()				
	});
	
}

function consultarDetalle(){
	$("#detalle").jqGrid({
	    url: url +'/consultarDetalle',
	    datatype: 'json',
	    mtype: 'GET',
	    colNames:['Clave','Descripcion', 'U. Medida','Cantidad','Precio U.','Lote','F. Caducidad'],
	    colModel :[ 
	      {name:'cveArt', index:'cveArt', width:50, align:'center',editable:false}, 
	      {name:'desArticulo', index:'desArticulo', width:500,editable:false},	      
	      {name:'unidad', index:'unidad', width:100,editable:false,align:'center'},
	      {name:'cantidad', index:'cantidad', width:50,editable:true,align:'center'},
	      {name:'precioEntrada', index:'precioEntrada', width:50,editable:true,align:'right',formatter: 'currency', formatoptions: { decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 4, prefix: "$", suffix:"", defaultValue: '0.00'}},
	      {name:'noLote', index:'noLote', width:100,editable:true},
	      {name:'fechaCaducidad', index:'fechaCaducidad', width:100,editable:true,sorttype:'date',align:'center'}
	    ],
	    postData:{idPadre:function() { return $('#idPadre').val() }},
	    onSelectRow: function(id){	
		},		
		afterInsertRow:function (rowid, 
				rowdata,
				rowelem){
		},
	    pager: '#pager',
	    editurl: url + "/actualizarDetalle",
	    rowNum:20,
	    rowList:[20, 40, 60 ,80],
	    //sortname: 'id',
	    //sortorder: 'asc',
	    viewrecords: true,
	    gridview: true,
	    caption: 'Entrada Detalle'			   
	})	
	
	$("#detalle").jqGrid('setGridWidth', 950);
	$("#detalle").jqGrid('setGridHeight', 250);
	
	$("#detalle").jqGrid("navGrid", "#pager",
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
	
	if($("#idPadre").val() != ''){
		$("#paqueteq").prop('disabled', true)
	}
	
	if($("#paqueteq").val() != undefined && $("#paqueteq").val() !=''){
		$(".busqueda").hide()
	}

	//Es una actualizacion con folio almacen
	if($("#folioAlmacen").val() != undefined && $("#folioAlmacen").val() != ''){
		$(".busqueda").hide()
		$("#remision").prop('disabled', true);
		$("#folioAlmacen").prop('disabled', true);	
	}
	
	if($("#idPadre").val() != '' && $("#remision").val() != ''){
		$("#folioAlmacen").prop('disabled', true);
	}	
	
	$("#guardar").click(function(){	
		if($("#fecha").valid() && $("#folio").valid() && 
			$("#folioAlmacen").valid() && $("#registra").valid() && $("#supervisa").valid() && $("#recibe").valid() ){
			guardarTodo()
		}
	});	
	
	$("#actualizar").click(function(){
		
		if($("#fecha").valid() && $("#registra").valid() 
		&& $("#supervisa").valid() && $("#recibe").valid() ){
			actualizar();
		}	
	})
	
	$("#guardarPaquete").click(function(){	
		if($("#fecha").valid() && $("#folio").valid() && $("#remision").valid() 
				&& $("#registra").valid() && $("#supervisa").valid() && $("#recibe").valid() ){
			guardarTodo()
		}
	});
	
	
	$("#cancelar").click(function(){
		cancelar()
	})
	
}

function detalleAdd(){
	
	$("#insumo").keypress(function(e){	
		 if(e.which == 13 && $("#insumo").valid() ) {
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
			 
			$.getJSON(url + "/convertidora",{clave:$("#insumo").val(),cantidad:this.value})
				.done(function( json ) {
					 $("#precio").val(json.precio)
					 $("#convertido").val(json.cantidad)
					 $("#ualmacen").val(json.ualma)
					 $("#calmacen").val(json.calma)
					 $("#uceye").val(json.uceye)
					 $("#cceye").val(json.cceye)
					 $("#cociente").val(json.cociente)
					 
					 $("#convertido").focus()
			})
			 
					
		 }
	});
	
	$("#convertido").keypress(function(e){	
		 if(e.which == 13) {
			 agregar()			 
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
			agregar()
		 }
	});
}

function agregar(){
	
	 if($("#formPadre").valid()){
		 
		 var cantidad = $("#cantidad").val()
		 
		 if($("#convertido").val() != null)
			 cantidad = $("#convertido").val()
		 
		 
		 
		 var data = [{	cveArt:$("#insumo").val(),
			 			desArticulo:$("#desArticulo").val(),
			 			unidad:$("#unidad").val(),
			 			cantidad:cantidad,
			 			precioEntrada:$("#precio").val(),
			 			noLote:$("#noLote").val(),
			 			fechaCaducidad:$("#fechaCaducidad").val()}];
		 
		 guardar(JSON.stringify(data))
		 
		 $("#clavelast").html($("#insumo").val());
		 $("#deslast").html($("#desArticulo").val());
		 $("#unidadlast").html($("#unidad").val());
		 
		 $("#cantidadlast").html(cantidad);
		 
		 $("#preciolast").html($("#precio").val());
		 $("#lotelast").html($("#noLote").val());
		 $("#caducidadlast").html($("#fechaCaducidad").val());
	
		 
		 $('#detalle').trigger("reloadGrid");			 
		 			 
		 limpiarRenglonDetalle()
		 $("#folioAlmacen").prop('disabled', true);
		 $("#insumo").focus()
	 }
	
}


/////////////////FUNCIONES PROPIAS////////////

function maskDates(){
	 //$("#fecha").mask("99/99/9999");	
	 $("#fechaCaducidad").mask("99/99/9999");
}

function consultarDetalleMaterial(){
	
	$("#detalle").clearGridData();	
	
	$.getJSON( url + "/consultarDetalleMaterial",{folioAlmacen:$("#folioAlmacen").val()})
		.done(function( json ) {
	        for (var i = 0; i < json.rows.length; i++) {        	
	        	$("#detalle").addRowData(json.rows[i].cveArt, json.rows[i]);
	        }
	        
        $("#idSalAlma").val(json.idSalAlma)
	})	
	
	$("#registra").focus()
	$(".busqueda").hide()
	$("#guardar").show()
}






















