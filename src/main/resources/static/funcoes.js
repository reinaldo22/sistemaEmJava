function eliminar(id){
	
	swal({
		  title: "Tem certeza que quer excluir?",
		  text: "Once deleted, you will not be able to recover this imaginary file!",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		})
		.then((ok) => {
		  if (ok) {
			  $.ajax({
				 url:"/excluir/"+id,
				 success: function(res){
					 console.log(res);
				 }
			  });
		    swal("Poof! Your imaginary file has been deleted!", {
		      icon: "success",
		    }).then((ok) => {
		    	if(ok){
		    		location.href="/listar"
		    	}
		    });
		  } else {
		    swal("Your imaginary file is safe!");
		  }
		});
}