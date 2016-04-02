<%-- 
    Document   : header
    Created on : 2-Mar-2016, 09:40:33
    Author     : Quoc
--%>
<script type="text/javascript">
    $(document).ready(function(){   
        showSearchPage();       
    });
    
    function showSearchPage(){
        var url = "search.jsp";
        
        //loading state while wait ajax
        var divLoading = '<div class="overlay-absolute"><i class="loading-img"></i></div>';        
        $('.content-wrapper').append(divLoading);

        //show page
        $.ajax({
            type: "POST",
            url: url,
            data: "",
            success: function (data) {
                //show content to main panel                
                setTimeout(function(){ $('.content-wrapper').html(data);}, 150);
            },
            error: function (e, response, status) {
                alert('Can not load search page!');
            }
        });
    }
</script>

<header class="main-header">
	<nav class="navbar navbar-static-top">
		<div class="container-fluid">
			<div class="navbar-header" >
				<a href="index.jsp" class="navbar-brand" style="vertical-align: middle; padding-top: 9px;">
					<span class="logo-lg" style="font-weight: bold; line-height: 30px;">Search DBLP</span>
				</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="navbar-collapse">      
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<!--<input type="text" class="form-control" id="navbar-search-input" placeholder="Search" style="width:300px">-->
					</div>
				</form> 
			</div><!-- /.navbar-collapse -->
		</div><!-- /.container-fluid -->
	</nav>
</header>