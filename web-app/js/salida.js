$(document).ready(function() {	
	
	$("#areaauto").focus()
	
	autoCompleteArticulo(function(){						 
		 disponibilidadArticulo($("#insumo").val(),$("#fecha").val());
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
	
	consultarDetalle();
	detalleAdd();
	capturar();
	validar();	
	controlesDetalle()
	controlesHead()
});

function validar(){
	$("#formPadre").validate({
		
		ignore: [],
		
        rules: {
        		fecha: {required:true,validateDate:true,dateToday:true},
        		folio: {required:true, uniqueFolio:true},
        		cveArea:{required:true},
        		recibeauto:{required:true},
        		autorizaauto:{required:true},
        		insumo: {required:true,number:true,checkInsumo:true},
                solicitado: {required:true,number:true},
                surtido: {required:true,number:true,checkExistencia:true}
        },
		messages: {
				fecha : {required:"Requerido"},
				folio:{required:"Requerido"},
				cveArea:{required:"Requerido"},
				recibeauto:{required:"Requerido"},
        		autorizaauto:{required:"Requerido"},
				insumo :{required:"Requerido",number:"Numerico"},
				solicitado : {required:"Requerido",number:"Numerico"},
				surtido:{required:"Requerido",number:"Numerico"}
		}
  });
}

function capturar(){
	
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

function consultarDetalle(){
	
	//alert($("#idPadre").val())
	
	$("#detalle").jqGrid({
	    url: url + '/consultarDetalle',
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
	    postData:{idPadre: function() { return $('#idPadre').val() }},
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
	    caption: 'Salida Detalle'//,
	    //multiselect: true
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
	
    //$('#detalle').jqGrid('inlineNav',"#pager");
	
}

function controlesHead(){
	$("#actualizar").click(function(){
		
		if( $("#cveArea").valid() && $("#fecha").valid() 
		    && $("#recibeauto").valid() && $("#autorizaauto").valid() ){
			actualizar();
		}		
			
	})
	
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
						 $("#costo").val(json.movimientoProm)
						 $("#costoDisplay").val(json.movimientoProm)
						 $("#costoDisplay").currency({ region: 'MXN', thousands: ',', decimal: '.', decimals: 4 })						 
						 disponibilidadArticulo($("#insumo").val(),$("#fecha").val());	
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
			 
			 if($("#formPadre").valid()){			 
				 var data = [{ cveArt:$("#insumo").val(),
					 		   desArticulo:$("#desArticulo").val(),
					 		   unidad:$("#unidad").val(),
					 		   costo:$("#costo").val(),
					 		   disponible:$("#disponible").val(),
					 		   solicitado:$("#solicitado").val(),					 		   
					 		   surtido:$("#surtido").val()
					 		}];
				 
				 guardar(JSON.stringify(data))			 
				
				 
				 $("#clavelast").html($("#insumo").val());
				 $("#deslast").html($("#desArticulo").val());
				 $("#unidadlast").html($("#unidad").val());
				 $("#costolast").html($("#costo").val());				
				 $("#solicitadolast").html($("#solicitado").val());
				 $("#surtidolast").html($("#surtido").val());			 
			
				 
				 $('#detalle').trigger("reloadGrid");		 
				 
				 limpiarRenglonDetalle()
				 $("#insumo").focus()
			 }
		 }
	});	
}


/////////////////FUNCIONES PROPIAS////////////

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

	