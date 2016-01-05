<#macro page title ontology>
<!DOCTYPE html>
    <head>
        <meta charset="utf-8">
        <title>${title?html}</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>

		<div id="menu">
	         <div id="navigation">
	           <ul class="top-level">
	              	<#list ontology.classes as clazz>
	              		<#if (clazz.rootClass && (clazz.individualsCount > 0))>
	       				<li><a class="wait" href="${clazz.label}.html">${clazz.labelReplaceUnderscore} (${clazz.individualsCount})</a>
	                     	<ul class="sub-level">                     		
	                     	<#list clazz.subClasses as subClazz>
	       	               	    <li><a class="wait" href="${subClazz.label}.html">${subClazz.labelReplaceUnderscore} (${subClazz.individualsCount})</a></li>
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
