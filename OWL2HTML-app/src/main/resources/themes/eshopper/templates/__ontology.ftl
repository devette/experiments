<#import "__common.ftl" as c/>
<@c.page title=messages("__ontology.page.title") ontology=ontology>
<h2 class="title text-center">${messages("__ontology.panel.title")}</h2>
<p>
<!--
	<ul>
	<#list ontology.importDeclarations as importDeclaration> 
		<li>Import URI: ${importDeclaration.URI!}</li>
	</#list>
	</ul>
-->
</p>

<#if (ontology.annotations?size > 0) >	
	<div class="panel-group" id="panelAnnotations">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					${ontology.getLocalLabel(context.locale)!}
					<span class="pull-right">
						<a data-toggle="collapse" data-parent="#panelAnnotations" href="#panelAnnotationsBody"><span class="badge"><i class="fa fa-plus"></i></span></a>
					</span>
				</h4>
			</div>
			<div id="panelAnnotationsBody" class="panel-collapse collapse">
				<div class="panel-body">
					<table class="table">
						<#list ontology.annotations as annotation>
							<#if (annotation.value.lang?? && context.locale.language == annotation.value.lang) >
								<tr>
								  <td style="vertical-align:top;">${annotation.property!}</td>
			                      <td>${annotation.value.literal!}</td>
			                      <td>${annotation.value.lang!}</td>
			                    </tr>
		                    </#if>
						</#list>
					</table>
				</div>
			</div>
		</div>
	<div>
</#if>

<div class="panel panel-default">

  <table class="table">
	<tr><th><div class="post-meta"><ul><li><nobr><i class="fa fa-bullseye"></i> ${messages("__ontology.index.classes")}</nobr></li></ul></div></th>
		<th><div class="post-meta"><ul><li><nobr><i class="fa fa-long-arrow-right"></i> ${messages("__ontology.index.objectproperties")}</nobr></li></ul></div></th>
		<th><div class="post-meta"><ul><li><nobr><i class="fa fa-circle"></i> ${messages("__ontology.index.dataproperties")}</nobr></li></ul></div></th></tr>
	<tr><td style="vertical-align:top">
	<ul style="list-style-type: none; list-style-position:inside; margin:0; padding:0;">
	<#list ontology.classes as clazz>
	      <li><i class="fa fa-bullseye"></i> <a href="${clazz.filename}.html">${clazz.getLocalLabel(context.locale)} (${clazz.individualsCount})</a></li>
	</#list>
	</ul>
	</td>
	
	<td style="vertical-align:top">
		<#if (ontology.objectProperties?size > 0) >
            <ul style="list-style-type: none; list-style-position:inside; margin:0; padding:0;">
			<#list ontology.objectProperties as p>
                    <li><a href="${p.propertyLabel}.html"><nobr><i class="fa fa-long-arrow-right"></i> ${p.getLocalLabel(context.locale)}</nobr></a></li>

			</#list>
            </ul>
		</#if>
	</td>
	
	<td style="vertical-align:top">
		<#if (ontology.dataProperties?size > 0) >
            <ul style="list-style-type: none; list-style-position:inside; margin:0; padding:0;">
			<#list ontology.dataProperties as p>
                   <li><a href="${p.propertyLabel}.html"><nobr><i class="fa fa-circle"></i> ${p.getLocalLabel(context.locale)}</nobr></a></li>
			</#list>
			</ul>
		</#if>
	</td>
	
	</tr>
  </table>
</div>

<div class="panel panel-default">	
	<table class="table">
		<tr><th><span class="pull-right">			
					  <div class="post-meta"><ul><li><nobr><i class="fa fa-bullseye"></i> ${messages("__ontology.index.individual.type")}</nobr></li></ul></div>
			     </span>
			     <div class="post-meta"><ul><li><nobr><i class="fa fa-circle-o"></i> ${messages("__ontology.index.individual")}</nobr></li></ul></div>
			</th>
		</tr>
		<tr><td style="vertical-align:top">
		    <ul style="list-style-type: none; list-style-position:inside; margin:0; padding:0;">
		    	<#list ontology.individuals as individual>
		    	      <li><a href="${individual.filename}.html"><nobr><i class="fa fa-circle-o"></i> ${individual.getLocalLabel(context.locale)}</nobr></a> 
		    	   		  <span class="pull-right">			
							  <#list individual.classes as clazz> <a href="${clazz.filename}.html"><nobr><i class="fa fa-bullseye"></i> ${clazz.getLocalLabel(context.locale)}</nobr></a></#list>
					      </span>
		    	      </li>
		    	</#list>
	    	</ul>
	    	</td>
		</tr>
	</table>
</div>

</@c.page>