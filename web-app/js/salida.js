$(document).ready(function() {	
	
	$("#folio").focus()	
	
	$("#fecha").datepicker(
	{dateFormat: 'dd/mm/yy',
	 showButtonPanel: true,
	 changeMonth: true,
     changeYear: true});
	
	autoCompleteArticulo(function(){						 
		$("#disponible").val(disponibilidadArticulo($("#insumo").val(),$("#fecha").val()));
		$("#solicitado").focus()
	});
		
	autoCompleteArea(function(){
		$("#pacienteauto").focus()
	});	
	
	autoCompletePaciente(function(){
		
		if($("#procedimientoauto").val() != undefined)
			$("#procedimientoauto").focus()
		else		
			$("#entrega").focus()
	});
	
	autoCompleteProcedimiento(function(){
		$("#entrega").focus()
	})
	
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
        		fecha: {required:true,validateDate:true,dateToday:true,checkCierre:true},
        		folio: {required:true, uniqueFolio:true},
        		nosala:{number:true},
        		cveArea:{required:true},
        		entrega:{required:true},
        		recibeauto:{required:true},
        		autorizaauto:{required:true},
        		insumo: {required:true,number:true,checkInsumo:true},
                solicitado: {required:true,number:true},
                surtido: {required:true,number:true,checkExistencia:true}
        },
		messages: {
				fecha : {required:"Requerido"},
				folio:{required:"Requerido"},
				nosala:{number:"Numerico"},
				cveArea:{required:"Requerido"},
				entrega:{required:"Requerido"},
				recibeauto:{required:"Requerido"},
        		autorizaauto:{required:"Requerido"},
				insumo :{required:"Requerido",number:"Numerico"},
				solicitado : {required:"Requerido",number:"Numerico"},
				surtido:{required:"Requerido",number:"Numerico"}
		}
  });
}

function capturar(){
	
	var fecha = $("#fecha")
	
	fecha.keypress(function(e){	
		 if(e.which == 13) {
			$("#folio").focus()		
		 }
	});
	
	fecha.focusout(function(){
		
	})
	
	
	
	$("#folio").keypress(function(e){	
		 if(e.which == 13) {
			 if($("#nosala").val() != undefined)
					$("#nosala").focus()
				else		
					$("#areaauto").focus()	
		 }
	});
	
	$("#nosala").keypress(function(e){	
		 if(e.which == 13) {
			$("#areaauto").focus()		
		 }
	});
	
	
	var area = $("#areaauto")
	
	area.keypress(function(e){	
		 if(e.which == 13) {
			 $("#pacienteauto").focus()	
		 }
	});

	area.change( function() {
		if(area.val() == "")
			$("#cveArea").val("")
	});
	
	var paciente = $("#pacienteauto")
	
	paciente.change( function() {
		if(paciente.val() == "")
			$("#idPaciente").val("")
	});
	
	paciente.keypress(function(e){	
		 if(e.which == 13) {
			 if($("#procedimientoauto").val() != undefined)
					$("#procedimientoauto").focus()
				else		
					$("#entrega").focus()	
		 }
	});
	
	var procedimiento = $("#procedimientoauto")
	
	procedimiento.keypress(function(e){	
		 if(e.which == 13) {
			 $("#entrega").focus()	
		 }
	});

	procedimiento.change( function() {
		if(procedimiento.val() == "")
			$("#idProcedimiento").val("")
	});
	
	
	$("#entrega").keypress(function(e){	
		 if(e.which == 13) {
			$("#recibeauto").focus()		
		 }
	});
	
	$("#recibeauto").keypress(function(e){	
		 if(e.which == 13) {
			$("#autorizaauto").focus()		
		 }
	});
	
	$("#autorizaauto").keypress(function(e){	
		 if(e.which == 13) {
			$("#insumo").focus()		
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
	    url: url + '/consultarDetalle',
	    datatype: 'json',
	    mtype: 'GET',
	    colNames:['Clave','Descripcion', 'U. Medida','Costo','Disponible','Solicitado','Surtido'],
	    colModel :[ 
	      {name:'cveArt', index:'cveArt', width:50, align:'center',editable:false}, 
	      {name:'desArticulo', index:'desArticulo', width:500,editable:false},	      
	      {name:'unidad', index:'unidad', width:100,editable:false,align:'center'},
	      {name:'costo', index:'costo', width:120,editable:false,align:'right',formatter: 'currency', formatoptions: { decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 4, prefix: "$", suffix:"", defaultValue: '0.00'}},
	      {name:'disponible', index:'disponible', width:90,editable:false,align:'center'},
	      {name:'solicitado', index:'solicitado', width:90,editable:true,align:'center'},	      
	      {name:'surtido', index:'surtido', width:90,editable:true,align:'center'}
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
	    sortable: false,
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
	
	if($("#idPadre").val() != ''){		
		
		if($("#estado").val()=='C' || $("#existeCierre").val()=='true' || $("#isDueno").val()=='false' ){
			$(".botonOperacion").hide()
			$(".busqueda").hide()
			
			$("#formPadre :input").each(function(){
				$(this).prop('disabled', true)
			});	
			
			$("#imprimir").show()
		}
		else{
			$(".botonOperacion").show()
		}
		
		$("#paqueteq").prop('disabled', true)
	}
		
	
	/*if($("#paqueteq").val() != undefined && $("#paqueteq").val() !=''){
		$(".busqueda").hide()
	}*/
	
	$("#guardarPaquete").click(function(){	
		if($(".cabecera").valid() && $("#folio").valid()){
			guardarTodo(this)
			$("#paqueteq").prop('disabled', true)
			$(".busqueda").show()
		}
	});
	
	$("#actualizar").click(function(){		
		if($(".cabecera").valid()){
			actualizar();
		}	
	})
	
	$("#cancelar").click(function(){
		cancelar()
	})
	
}




function detalleAdd(){
	
	$("#insumo").focus(function(){	
		limpiarRenglonDetalle()
	});
	
	$("#insumo").keypress(function(e){	
		 if(e.which == 13 && $("#insumo").valid() ) {
			$.getJSON(url + "/buscarArticulo",{id:this.value})
					.done(function( json ) {
						 $("#artauto").val(json.desArticulo)						 
						 $("#unidad").val(json.unidad)
						 $("#costo").val(json.movimientoProm)				 
						 $("#costo").currency({ region: 'MXN', thousands: ',', decimal: '.', decimals: 4 })						 
						 $("#disponible").val(disponibilidadArticulo($("#insumo").val(),$("#fecha").val()));	
						 $("#solicitado").focus()
						 
					})
					.fail(function() {
						alert("La clave " + $("#insumo").val() + " no existe")
						limpiarRenglonDetalle()
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
					 		   solicitado:$("#solicitado").val(),					 		   
					 		   surtido:$("#surtido").val()
					 		}];
				 
				 guardar(JSON.stringify(data))			
				 
				 $("#clavelast").html($("#insumo").val());
				 $("#deslast").html($("#artauto").val());
				 $("#unidadlast").html($("#unidad").val());
				 $("#costolast").html($("#costo").val());				
				 $("#solicitadolast").html($("#solicitado").val());
				 $("#surtidolast").html($("#surtido").val());
				 $('#disponiblelast').html(disponibilidadArticulo($("#insumo").val(),$("#fecha").val()))
				 
				 limpiarRenglonDetalle()
				 $("#insumo").focus()
			 }
		 }
	});	
}


/////////////////FUNCIONES PROPIAS////////////

function disponibilidadArticulo(clave, fecha){
	
	var result = '';
	
	 var request = $.ajax({
	        type: "POST",
	        url: url + '/disponibilidadArticulo',
	        async:false,
	        data: {clave:clave,fecha:fecha},
	        dataType:"json"
	 });
	 
	 
	 request.done(function(msg){
	    	 //alert(msg.disponible)
	    	 result = msg.disponible
	    	 //$("#disponible").val(msg.disponible)         
	 })
	 
	 return result
}

	