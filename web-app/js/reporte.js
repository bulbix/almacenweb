$(document).ready(function() {
	 $("#fechaInicial").mask("99/99/9999");
	 $("#fechaFinal").mask("99/99/9999");
	 
	 
	 $("#fechaInicial").datepicker(
				{dateFormat: 'dd/mm/yy',
				 showButtonPanel: true,
				 changeMonth: true,
			     changeYear: true});
	 
	 $("#fechaFinal").datepicker(
				{dateFormat: 'dd/mm/yy',
				 showButtonPanel: true,
				 changeMonth: true,
			     changeYear: true});
	
})