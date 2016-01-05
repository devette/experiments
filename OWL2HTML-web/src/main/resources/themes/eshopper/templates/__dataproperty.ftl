<#import "__common.ftl" as c/>
<@c.page title=messages("__dataproperties.page.title") ontology=ontology>

<h2 class="title text-center"><i class="fa fa-circle"></i> ${dataProperty.getLocalLabel(context.locale)}</h2>
<div class="panel panel-default">
	<div class="panel-heading">
		<h4 class="panel-title">${messages("__dataproperties.panel.title")}</h4>
	</div>
	<div id="dataPropertiesPanel" class="panel-collapse expand">
		<div class="panel-body">
			<table class="table">
			   <#list dataProperty.referencingAxioms as p>
				   <tr>
				        <td><i class="fa fa-circle-o"></i> <a href="select?individual=${p.subject.iri?url}">${p.subject.getLocalLabel(context.locale)}</a></td>
				        <td><a href="select?dataproperty=${p.iri?url}"><i class="fa fa-circle"></i> ${p.getLocalLabel(context.locale)}</a></td>        
				        <td>${p.object.literal!}</td>
				        <!--<td>${p.object.lang!}</td>-->
				    </tr>
				</#list>
			</table>
		</div>
	</div>
</div>


</@c.page>