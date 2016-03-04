<%-- 
    Document   : search
    Created on : 03-Mar-2016, 08:41:54
    Author     : Quoc
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript">
    
    $(document).ready(function(){    
        //call to get search item
        var itemTemplate = null;
        $.ajax({
            type:"POST",
            url:"SearchItem",
            data :"",
            success: function(response) {
                itemTemplate = response;                
                $('.search-zone').html(itemTemplate );
            }
        });
        
        //on search clicked
        $(document).on('click', '.btn-search', function(){
            
            //get queries
            var queries = new Array();
            
            $('.search-zone .search-item').each(function(){
                var key = $(this).closestChild('input[type=text]').val();  
                
                if (key) {
                    var operator = $(this).closestChild('.select-operator').val();
                    var field = $(this).closestChild('.select-field').val();
                    
                    var query = { operator : operator, key : key, field :field };
                    queries.push(query);                       
                }
            });
            
            //send to search servlet
            $.ajax({
                type: "POST",
                url: "Search",
                data: { input: JSON.stringify(queries) },
                datatype: "application/json",
                success: function (response) {
                    $('#divResult').html(response);
                }
            });            
        });
   });
</script>

<section class="content-header">
<!--
    <h1>
        Location
        <small>Location of events</small>
    </h1>
    <div id="dlgCreateLocationDumper"></div>-->
</section>

<!--Main content--> 
<section class="content">
    <!--Your Page Content Here--> 
    <div class="box box-default">
        <div class="box-header with-border" style="border-bottom-color: lightgray">
            <div class="search-outline">
                <div class="row">
                    <div class="col col-sm-7">
                        <div class="search-zone"></div>
                        <div class="margin">
                            <a href="#" class="btn-add-item pull-right">Add more condition</a>
                        </div>

                    </div>
                    <div class="col">
                        <button type="button" class="btn btn-primary btn-search"></i> Search</button> 
                        <button type="button" class="btn btn-primary btn-clear">Clear</button>
                    </div>
                </div>
            </div>
        </div>        
            <div class="box-body">
                <div id="divResult"></div>
            </div>
            <div class ="box-footer">
                
            </div>
        </form>    
    </div>
</section> <!--/.content -->
