<#import "__common.ftl" as c/>
<@c.page title=messages("__objectproperties.page.title") ontology=ontology>


<h2 class="title text-center"><i class="fa fa-long-arrow-right"></i> ${objectProperty.getLocalLabel(context.locale)}</h2>

<div class="panel panel-default">
	<div class="panel-heading">
		<h4 class="panel-title">${messages("__objectproperties.panel.title")}</h4>
	</div>
	<div id="objectPropertiesPanel" class="panel-collapse expand">
		<div class="panel-body">
			<table class="table">
			<tr>
				<td><div class="post-meta">
						<ul>
							<#list objectProperty.domain as classVO>
						 	    <li><nobr><i class="fa fa-bullseye"></i><a href="select?class=${classVO.iri?url}">${classVO.getLocalLabel(context.locale)}</a></nobr></li>
						    </#list>
					    </ul>
				    </div>
				</td>
				<td><i class="fa fa-long-arrow-right"></i> ${objectProperty.getLocalLabel(context.locale)}</td>
				<td><div class="post-meta">
						<ul>
							<#list objectProperty.range as classVO>
						 	    <li><nobr><i class="fa fa-bullseye"></i><a href="select?class=${classVO.iri?url}">${classVO.getLocalLabel(context.locale)}</a></nobr></li>
						    </#list>
					    </ul>
					</div>
				</td>
			</tr>
			<#list objectProperty.referencingAxioms as axiom>
			     <tr>
			         <td><a href="select?individual=${axiom.subject.iri?url}"><i class="fa fa-circle-o"></i> ${axiom.subject.getLocalLabel(context.locale)}</a></td>
			         <td><a href="select?objectproperty=${axiom.iri?url}"><nobr><i class="fa fa-long-arrow-right"></i> ${axiom.getLocalLabel(context.locale)}</nobr></a></td>
			         <td><a href="select?individual=${axiom.object.iri?url}"><i class="fa fa-circle-o"></i> ${axiom.object.getLocalLabel(context.locale)}</a></td>
			     </tr>
			</#list>
			</table>
		</div>
	</div>
</div>

</@c.page>