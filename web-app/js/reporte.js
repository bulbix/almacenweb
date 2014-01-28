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
	 
	 
	 var valueOnClick = $(".jasperButton").attr("onclick")
	 $(".jasperButton").attr("onclick","blockUIForDownload();" + valueOnClick) 
	 
	 $("#formReporte").submit(function(){
		 blockUIForDownload() 
	 })
	
})

var fileDownloadCheckTimer;

function blockUIForDownload() {
    var token = new Date().getTime(); //use the current timestamp as the token value
    $('#download_token_value_id').val(token);
    $.blockUI({ message: '<h1>Generando Reporte Por Favor Espere...</h1>' });
    fileDownloadCheckTimer = window.setInterval(function () {
      var cookieValue = $.cookie('fileDownloadToken');
      if (cookieValue == token)
       finishDownload();
    }, 1000);
 }

function finishDownload() {
	 window.clearInterval(fileDownloadCheckTimer);
	 $.removeCookie('fileDownloadToken'); //clears this cookie value
	 $.unblockUI();
}

