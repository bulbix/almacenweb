$(document).ready(function() {	
	$("#remision").focus()
	nuevo();
	autoCompleteArticulo(function(){
		$("#cantidad").focus()
	});
	jqGridDetalle();
	detalleAdd();
	validar();
	guardarEntrada();
	maskDates();
	capturaEntrada();
	
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
	$.getJSON("/almacenWeb/entrada/jqGridMaterial",{folioAlmacen:$("#folioAlmacen").val()})
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
        rules: {
                fechaEntrada: {required:true,validateDate:true},
                folioEntrada: {required:true, uniqueFolioEntrada:true},
                folioAlmacen: {uniqueFolioSalAlma:true}
        },
		messages: {
				fechaEntrada : {required:"Fecha Requerida"},
				folioEntrada:{required:"Folio Requerido"}				
		}
  });
}


function maskDates(){
	 $("#fechaEntrada").mask("99/99/9999");	
	 $("#fechaCaducidad").mask("99/99/9999");
}

function guardarEntradaDetalle(){
	
}

function guardarEntrada(){
	$("#guardar").click(function(e){
		
		if($("#formEntrada").valid()){
		
			var gridData = jQuery("#entradadetalle").getRowData();
		    var postData = JSON.stringify(gridData);		    
			
		    var frm = $("#formEntrada");
		    var entradaData = JSON.stringify(frm.serializeObject()); 
		    
		    
			var request = $.ajax({
				type:'POST',
				url: '/almacenWeb/entrada/guardarEntrada',
				data:{
					entradaData: entradaData, 
					gridEntradaDetalle: postData
			         
				},
				dataType:"json"	        
			});		
			
			
			request.always(function(data) {
				//alert(data.status)
				$('#mensaje').html('<h6>Entrada guardada</h6>')
				//$("#entradadetalle").trigger("reloadGrid"); 
			});
		}
		
	});
}

function nuevo(){
	
	$("#nuevo").click(function(){
		$("#remision").focus()
		$("input[type='text']").val('')
		
	})
}

function limpiarRenglonEntradaDetalle(){
	
	$("#tblBusqueda :input").each(function(){
		$(this).val('');
	});	
}

function detalleAdd(){
	
	$("#insumo").keypress(function(e){	
		 if(e.which == 13) {
			$.getJSON("/almacenWeb/util/buscarArticulo",{id:this.value})
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
			 var data = [{cveArt:$("#insumo").val(),desArticulo:$("#desArticulo").val(),unidad:$("#unidad").val(),
		            cantidad:$("#cantidad").val(),precioEntrada:$("#precio").val(),
		            noLote:$("#noLote").val(),fechaCaducidad:$("#fechaCaducidad").val()}];
			 
			 var RowId = $("#entradadetalle").jqGrid('getGridParam', 'reccount');
			 ++RowId
			 jQuery("#entradadetalle").addRowData(RowId, data, "first");			 
			 limpiarRenglonEntradaDetalle()
			 $("#insumo").focus()
		 }
	});
	
	$("#bDelete").click(function(){ 

	    // Get the currently selected row
	    toDelete = $("#entradadetalle").jqGrid('getGridParam','selrow');

	    // You'll get a pop-up confirmation dialog, and if you say yes,
	    // it will call "delete.php" on your server.
	    $("#entradadetalle").jqGrid(
	        'delGridRow',
	        toDelete/*,
	          { url: 'delete.php', 
	            reloadAfterSubmit:false}*/
	    );
	});
	
}

function jqGridDetalle(){
	$("#entradadetalle").jqGrid({
	    url:'/almacenWeb/entrada/consultarEntradaDetalle',
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
	    postData:{idEntrada:$("#idEntrada").val()},
	    onSelectRow: function(id){	
		},		
		afterInsertRow:function (rowid, 
				rowdata,
				rowelem){
		},
	    pager: '#pager',
	    rowNum:20,
	    rowList:[20, 40, 60 ,80],
	    sortname: 'id',
	    sortorder: 'asc',
	    viewrecords: true,
	    gridview: true,
	    caption: 'Entrada Detalle'			   
	})	
	
	$("#entradadetalle").jqGrid('setGridWidth', 950);
	$("#entradadetalle").jqGrid('setGridHeight', 250);
	
	$("#entradadetalle").jqGrid("navGrid", "#pager", {add: false, edit: false, del: true, search: false, refresh: false});
     $('#entradadetalle').jqGrid('inlineNav',"#pager");
	
}
