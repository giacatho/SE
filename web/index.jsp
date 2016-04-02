<%-- 
    Document   : index
    Created on : 03-Mar-2016, 17:47:52
    Author     : Quoc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DBLP Search</title>                      

        <%@include file="template/head.jsp" %> 
        <script type="text/javascript">
            $(document).ready(function() {
                //init standford
                $.ajax({
                    type: "POST",
                    url: "InitStanford"            
                });
            })
        </script>
    </head>

    <body class="skin-blue layout-top-nav" style="font-size: 16px;">
        <div class="wrapper">
            <!-- header -->
            <%@include file="template/header.jsp"%>
            <!-- Left side column. contains the logo and sidebar -->
            <%--<%@include file="template/sidebar.jsp"%>--%>             
            
            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper" style="width: 100%; height:100%">
                <!-- Content Header (Page header) -->
                
            </div><!-- /.content-wrapper -->             
            <%@include file="template/footer.jsp" %>
        </div>
    </body>
</html>
