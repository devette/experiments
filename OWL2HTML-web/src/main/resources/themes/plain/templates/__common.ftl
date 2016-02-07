<#macro page title ontology>
<#setting url_escaping_charset="UTF-8">
<!DOCTYPE html>
    <head>
        <meta charset="utf-8">
        <title>${title?html}</title>
        <link rel="stylesheet" href="themes/${context.theme}/static/css/style.css">
    </head>
    <body>

		<div id="menu">
	         <div id="navigation">
	           <ul class="top-level">
	              	<#list ontology.classes as clazz>
	              		<#if (clazz.rootClass && (clazz.individualsCount > 0))>
	       				<li><a class="wait" href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)}  (${clazz.individualsCount})</a>
	                     	<ul class="sub-level">                     		
	                     	<#list clazz.subClasses as subClazz>
	       	               	    <li><a class="wait" href="select?class=${subClazz.iri?url}">${subClazz.getLocalLabel(context.locale)} (${subClazz.individualsCount})</a></li>
	                     	</#list>
	                     	</ul>
	                    </li>
	                   </#if>
	     		</#list>
	           </ul>
			</div>
        </div>    
        

        <div id="content">
             <#nested/>
        </div>

        <div id="footer">
             Generated:<br/> ${.now}
        </div>

    </body>
</html>
</#macro>
