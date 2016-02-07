<#import "__common.ftl" as c/>
<@c.page title="${individual.getLocalLabel(context.locale)}" ontology=ontology>
	<h1><nobr>${individual.getLocalLabel(context.locale)}</nobr></h1>

    <ul>
        <#list individual.classes as classVO>
            <li><nobr><a href="select?class=${classVO.iri?url}">${classVO.getLocalLabel(context.locale)}</a></nobr></li>
        </#list>
    </ul>
    <br/>
	
	<#if (individual.annotations?size > 0) >	
		<div>
			<p>
				<table>
				    <tr><th>annotation</th><th>value</th><th style="width:10%">language</th></tr>
				<#list individual.annotations as annotation>
                    <#if (annotation.value.lang?? && context.locale.language == annotation.value.lang) >
                        <tr>
                          <td style="vertical-align:top">${annotation.property!}</td>
                          <td>${annotation.value.literal!}</td>
                          <td>${annotation.value.lang!}</td>
                        </tr>
                    </#if>
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
                    <td><a href="select?dataproperty=${p.iri?url}">${p.getLocalLabel(context.locale)}</a></td>
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
					<td><a href="select?individual=${p.subject.iri?url}">${p.subject.getLocalLabel(context.locale)}</a></td>
                    <td><a href="select?objectproperty=${p.iri?url}">${p.getLocalLabel(context.locale)}</a></td>
                    <td><a href="select?individual=${p.object.iri?url}">${p.object.getLocalLabel(context.locale)}</a></td>
                    <td><#list p.object.classes as clazz><a href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)}</a> </#list></td>
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
				    <td><#list p.subject.classes as clazz><a href="select?class=${clazz.iri?url}">${clazz.getLocalLabel(context.locale)}</a></#list></td>
				    <td><a href="select?individual=${p.subject.iri?url}">${p.subject.getLocalLabel(context.locale)}</a></td>
                    <td><a href="select?objectproperty=${p.iri?url}">${p.getLocalLabel(context.locale)}</a></td>
                    <td><a href="select?individual=${p.object.iri?url}">${p.object.getLocalLabel(context.locale)}</a></td>
                </tr>
		   </#list>
		</table>
		</p>
	</div>
	</#if>
</@c.page>