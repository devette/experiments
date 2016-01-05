<#import "__common.ftl" as c/>
<@c.page title="Index" ontology=ontology>

<h1>Index</h1>
<p>
	<ul>
	<#list ontology.importDeclarations as importDeclaration> 
		<li>Import URI: ${importDeclaration.URI!}</li>
	</#list>
	</ul>
</p>
<table>
	<tr><th>Concepts</th><th>Relations</th><th>Data properties</th><th>Implementations</th></tr>
	<tr><td style="vertical-align:top">
	<ul>
	<#list ontology.classes as clazz>
	      <li><a href="${clazz.label}.html">${clazz.labelReplaceUnderscore} (${clazz.individualsCount})</a></li>
	</#list>
	</ul>
	</td>
	
	<td style="vertical-align:top">
		<#if (ontology.objectProperties?size > 0) >
            <ul>
			<#list ontology.objectProperties as p>
                    <li><a href="${p.propertyLabel}.html">${p.propertyLabel}</a></li>

			</#list>
            </ul>
		</#if>
	</td>
	
	<td style="vertical-align:top">
		<#if (ontology.dataProperties?size > 0) >
            <ul>
			<#list ontology.dataProperties as p>
                   <li><a href="${p.propertyLabel}.html">${p.propertyLabel}</a></li>
			</#list>
			</ul>
		</#if>
	</td>

    	<td style="vertical-align:top">
    	<ul>
    	<#list ontology.individuals as individual>
    	      <li><a href="${individual.label}.html">${individual.label}</a>::<#list individual.classes as clazz><a href="${clazz.label}.html">${clazz.labelReplaceUnderscore}</a></#list></li>
    	</#list>
    	</ul>
    	</td>
	</tr>
</table>

</@c.page>