<#import "__common.ftl" as c/>
<@c.page title="${individual.label}" ontology=ontology>
	<#list individual.classes as classVO>
 	    <h1><a href="${classVO.filename}.html">${classVO.labelReplaceUnderscore}</a></h1>
    </#list>
	<h1>${individual.labelReplaceUnderscore}</h1>
	
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

	<#if (individual.dataProperties?size > 0) >	
	<div>
		<p>
			<table>
			    <tr><th style="width:30%">property</th><th>value</th><th style="width:10%">language</th></tr>
			<#list individual.dataProperties as p>
				<tr>
                    <td><a href="${p.propertyLabel}.html">${p.propertyLabel}</a></td>               
                    <td>${p.object.literal!}</td>
                    <td>${p.object.lang!}</td>
                </tr>
			</#list>
			</table>
		</p>
	</div>
	</#if>
	
	<#if (individual.objectProperties?size > 0) >
	<div>
		<p>
			<table>
			    <tr><th style="width:30%">subject</th><th>property</th><th>value type</th><th>value</th></tr>
			<#list individual.objectProperties as p>
				<tr>
					<td><a href="${p.subject.label}.html">${p.subject.label}</a></td>										
                    <td><a href="${p.propertyLabel}.html">${p.propertyLabel}</a></td>
                    <td><#list p.object.classes as clazz><a href="${clazz.label}.html">${clazz.labelReplaceUnderscore}</a> </#list></td>
                    <td><a href="${p.object.label}.html">${p.object.labelReplaceUnderscore}</a></td>
                </tr>
			</#list>
			</table>
		</p>
	</div>
	</#if>

	<#if (individual.referencingAxioms?size > 0) >
	<div>
		<p>
		<table>
		   <table>
			    <tr><th style="width:30%">subject type</th><th>subject</th><th>property</th><th>value</th></tr>
		   <#list individual.referencingAxioms as p>
				<tr>
				    <td><#list p.subject.classes as clazz><a href="${clazz.label}.html">${clazz.labelReplaceUnderscore}</a></#list></td>
				    <td><a href="${p.subject.label}.html">${p.subject.label}</a></td>				    
                    <td><a href="${p.propertyLabel}.html">${p.propertyLabel}</a></td>
                    <td><a href="${p.object.label}.html">${p.object.labelReplaceUnderscore}</a></td>
                </tr>
		   </#list>
		</table>
		</p>
	</div>
	</#if>
</@c.page>