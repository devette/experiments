<#import "__common.ftl" as c/>
<@c.page title="${individual.getLocalLabel(context.locale)}" ontology=ontology>
	<#import "__visualize_objectproperties.ftl" as vis/>
	
	<h2 class="title text-center"><nobr><i class="fa fa-circle-o"></i> ${individual.getLocalLabel(context.locale)}</nobr></h2>
	
	<div class="post-meta">
		<ul>
			<#list individual.classes as classVO>
		 	    <li><nobr><i class="fa fa-bullseye"></i><a href="select?class=${classVO.iri?url}">${classVO.getLocalLabel(context.locale)}</a></nobr></li>
		    </#list>
	    </ul>
	</div><br/>
	
	<#if (individual.annotations?size > 0) >	
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					${messages("__individual.annotations.panel.title")}
				</h4>
			</div>
			<div id="annotationsPanel" class="panel-collapse expand">
				<div class="panel-body">
					<table class="table">
						<#list individual.annotations as annotation>
							<tr>
							  <td style="vertical-align:top;">${annotation.property!}</td>
		                      <td>${annotation.value.literal!}</td>
		                      <!--<td>${annotation.value.lang!}</td>-->
		                    </tr>
						</#list>
					</table>
				</div>
			</div>
		</div>
	</#if>
	
	<#if (individual.objectProperties?size > 0 || individual.referencingAxioms?size > 0) >
		<div class="panel panel-default">
			<div class="panel-heading">
				<span class="pull-right" id="graphControl">		
					<a href="select?graphdepth=1&&individual=${individual.iri?url}#graphControl"><span class="badge"><i class="fa fa-cog"></i> 1</span></a>
					<a href="select?graphdepth=2&&individual=${individual.iri?url}#graphControl"><span class="badge"><i class="fa fa-cog"></i> 2</span></a>
					<a href="select?graphdepth=3&&individual=${individual.iri?url}#graphControl"><span class="badge"><i class="fa fa-cog"></i> 3</span></a>
					<a href="select?graphdepth=4&&individual=${individual.iri?url}#graphControl"><span class="badge"><i class="fa fa-cog"></i> 4</span></a>
					<a href="select?graphdepth=5&&individual=${individual.iri?url}#graphControl"><span class="badge"><i class="fa fa-cog"></i> 5</span></a>
					<a href="select?graphdepth=6&&individual=${individual.iri?url}#graphControl"><span class="badge"><i class="fa fa-cog"></i> 6</span></a>
				    <a data-toggle="collapse" data-parent="#graphPanel" href="#graphPanel"><span class="badge"><i class="fa fa-plus"></i></span></a>
			    </span>
				<h4 class="panel-title">
					${messages("__individual.graph.panel.title")} (${messages("__individual.graph.panel.depth")}: ${session.graphDepth?number})
				</h4>
			</div>
			<div id="graphPanel" class="panel-collapse collapse in">
				<div class="panel-body">
					<!-- visjs graph will be drawn in this div -->
					<div id="individualGraph"></div>
				</div>
			</div>
		</div>
	</#if>

	<#if (individual.dataProperties?size > 0) >	
		<br/>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">${messages("__individual.dataproperties.panel.title")}</h4>
			</div>
			<div id="dataPropertiesPanel" class="panel-collapse expand">
				<div class="panel-body">
					<table class="table">
						<#list individual.dataProperties as p>
							<tr>
			                    <td><a href="select?dataproperty=${p.iri?url}"><nobr><i class="fa fa-circle"></i> ${p.getLocalLabel(context.locale)}</nobr></a></td>               
			                    <td style="vertical-align:top;">${p.object.literal!}</td>
			                    <!--<td>${p.object.lang!}</td>-->
			                </tr>
						</#list>
					</table>
				</div>
			</div>
		</div>
	</#if>
	
	<#if (individual.objectProperties?size > 0) >
		<br/>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">${messages("__individual.objectproperties.panel.title")}</h4>
			</div>
			<div id="objectPropertiesPanel" class="panel-collapse expand">
				<div class="panel-body">
					<table class="table">
						<#list individual.objectProperties as p>
							<tr>
								<td><a href="select?individual=${p.subject.iri?url}"><nobr><i class="fa fa-circle-o"></i> ${p.subject.getLocalLabel(context.locale)}</nobr></a></td>										
			                    <td><a href="select?objectproperty=${p.iri?url}"><nobr><i class="fa fa-long-arrow-right"></i> ${p.getLocalLabel(context.locale)}</nobr></a></td>
			                    <td><a href="select?individual=${p.object.iri?url}"><nobr><i class="fa fa-circle-o"></i> ${p.object.getLocalLabel(context.locale)}</nobr></a></td>
			                    <td><#list p.object.classes as clazz><a href="select?class=${clazz.iri?url}"><nobr><i class="fa fa-bullseye"></i> ${clazz.getLocalLabel(context.locale)}</nobr></a> </#list></td>
			                </tr>
						</#list>
					</table>
				</div>
			</div>
		</div>
	</#if>

	<#if (individual.referencingAxioms?size > 0) >
		<br/>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">${messages("__individual.referencing.panel.title")}</h4>
			</div>
			<div id="referencingPropertiesPanel" class="panel-collapse expand">
				<div class="panel-body">
					<table>
					   <table class="table">
					   <#list individual.referencingAxioms as p>
							<tr>
							    <td><#list p.subject.classes as clazz><a href="select?class=${clazz.iri?url}"><nobr><i class="fa fa-bullseye"></i> ${clazz.getLocalLabel(context.locale)}</nobr></a> </#list></td>
							    <td><a href="select?individual=${p.subject.iri?url}"><nobr><i class="fa fa-circle-o"></i> ${p.subject.getLocalLabel(context.locale)}</nobr></a></td>				    
			                    <td><a href="select?objectproperty=${p.iri?url}"><nobr><i class="fa fa-long-arrow-right"></i> ${p.getLocalLabel(context.locale)}</nobr></a></td>
			                    <td><a href="select?individual=${p.object.iri?url}"><nobr><i class="fa fa-circle-o"></i> ${p.object.getLocalLabel(context.locale)}</nobr></a></td>
			                </tr>
					   </#list>
					</table>
				</div>
			</div>
		</div>
	</#if>
	
	<@vis.graphjs graphid="individualGraph" individual=individual maxdepth="${session.graphDepth}"/> 
	
	
</@c.page>