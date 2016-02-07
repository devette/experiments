<#import "__common.ftl" as c/>
<@c.page title=messages("__ontology.page.title")  ontology=ontology>

<h1>${messages("__ontology.panel.title")}</h1>
<p>
	<ul>
	<#list ontology.importDeclarations as importDeclaration> 
		<li>Import URI: ${importDeclaration.URI!}</li>
	</#list>
	</ul>
</p>
<table>
	<tr><th>Classes</th><th>Properties</th><th>Data properties</th><th>Individuals</th></tr>
	<tr><td style="vertical-align:top">
	<ul>
	<#list ontology.classes as clazz>
	      <li><a href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)} (${clazz.individualsCount})</a></li>
	</#list>
	</ul>
	</td>
	
	<td style="vertical-align:top">
		<#if (ontology.objectProperties?size > 0) >
            <ul>
			<#list ontology.objectProperties as objectProperty>
                    <li><a href="select?objectproperty=${objectProperty.iri?url}">${objectProperty.getLocalLabel(context.locale)}</a></li>

			</#list>
            </ul>
		</#if>
	</td>
	
	<td style="vertical-align:top">
		<#if (ontology.dataProperties?size > 0) >
            <ul>
			<#list ontology.dataProperties as p>
                   <li><a href="select?dataproperty=${p.iri?url}">${p.getLocalLabel(context.locale)}</a></li>
			</#list>
			</ul>
		</#if>
	</td>

    	<td style="vertical-align:top">
    	<ul>
    	<#list ontology.individuals as individual>
    	      <li><a href="select?individual=${individual.iri?url}">${individual.getLocalLabel(context.locale)}</a>::<#list individual.classes as clazz><a href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)}</a></#list></li>
    	</#list>
    	</ul>
    	</td>
	</tr>
</table>

</@c.page>