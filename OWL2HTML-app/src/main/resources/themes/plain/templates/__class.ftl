<#import "__common.ftl" as c/>
<@c.page title="${clazz.labelReplaceUnderscore}" ontology=ontology>
<#if (clazz.superClass?? && clazz.superClass.label != "undefined") >
	<h1><a href="${clazz.superClass.label}.html">${clazz.superClass.label}</a></h1>
</#if>
<h1>${clazz.labelReplaceUnderscore} (${clazz.individualsCount})</h1>

<#if (clazz.subClasses?size > 0) >
	<div>
		<p>See also:
		<ul>
		<#list clazz.subClasses as subclazz>
			<li><a href="${subclazz.label}.html">${subclazz.labelReplaceUnderscore} (${subclazz.individualsCount})</a>
		</#list>
		</ul>	
		</p>
	</div>
</#if>

<#if (clazz.annotations?size > 0) >
	<div>
		<p>
			<table>
			    <tr><th>annotation</th><th>value</th><th style="width:10%">language</th></tr>
			<#list clazz.annotations as annotation>
				<tr>
	                <td style="vertical-align:top">${annotation.property}</td>
	                <td>${annotation.value.literal!}</td>
	                <td>${annotation.value.lang!}</td>
                </tr>
			</#list>
			</table>
		</p>
	</div>
</#if>

<#if (clazz.individualsCount > 0) >
	<#list clazz.individuals as individual>
		<h3><a href="${individual.filename}.html">${individual.labelReplaceUnderscore}</a></h3>
	
		<#if (individual.annotations?size > 0) >	
		<div>
			<p>
				<table>
				    <tr><th>annotation</th><th>value</th><th style="width:10%">language</th></tr>
				<#list individual.annotations as annotation>
					<tr>
					  <td style="vertical-align:top">${annotation.property!}</td>
                      <td>${annotation.value.literal!}</td>
                      <td>${annotation.value.lang!}</td>
                    </tr>
				</#list>
				</table>
			</p>
		</div>
		</#if>
		
		
		<div>
			<p>
				<table>
				<tr><th>property</th><th>type</th><th>value</th></tr>
				<#list individual.objectProperties as p>
					<tr>
	                    <td style="width:30%"><a href="${p.propertyLabel}.html">${p.propertyLabel}</a></td>
	                    <td><#list p.object.classes as clazz><a href="${clazz.label}.html">${clazz.labelReplaceUnderscore}</a> </#list></td>
	                    <td><a href="${p.object.label}.html">${p.object.label}</a></td>	                    
	                </tr>
				</#list>
				</table>
			</p>
		</div>
		
	</#list>
</#if>

</@c.page>