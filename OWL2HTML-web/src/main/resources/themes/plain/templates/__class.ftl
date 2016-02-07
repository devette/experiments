<#import "__common.ftl" as c/>
<@c.page title="${clazz.getLocalLabel(context.locale)}" ontology=ontology>

<h2> ${clazz.getLocalLabel(context.locale)} (${clazz.individualsCount})</h2>
<#if (clazz.superClasses?size > 0) >
	<#list clazz.superClasses as superclazz>
	<#if (superclazz.label != "undefined") >
		<h3><a href="select?class=${superclazz.iri?url}">${superclazz.getLocalLabel(context.locale)}</a></h3>
	</#if>
	</#list>
</#if>

<#if (clazz.subClasses?size > 0) >
    <h4>${messages("__class.subclasses.seealso")}</h4>
    <ul>
    <#list clazz.subClasses as subclazz>
        <li><a href="select?class=${subclazz.iri?url}">${subclazz.getLocalLabel(context.locale)} (${subclazz.individualsCount})</a></li>
    </#list>
    </ul>
    </p>
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
		<h3><a href="select?individual=${individual.iri?url}">${individual.getLocalLabel(context.locale)}</a></h3>
	
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
				<tr><th>property</th><th>value</th><th>type</th></tr>
				<#list individual.objectProperties as p>
					<tr>
	                    <td style="width:30%"><a href="select?objectproperty=${p.iri?url}">${p.getLocalLabel(context.locale)}</a></td>
	                    <td><a href="select?individual=${p.object.iri?url}">${p.object.getLocalLabel(context.locale)}</a></td>
	                    <td><#list p.object.classes as clazz><a href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)}</a> </#list></td>
	                </tr>
				</#list>
				</table>
			</p>
		</div>
		
	</#list>
</#if>

</@c.page>