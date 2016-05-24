<#import "__common.ftl" as c/>
<@c.page title="${clazz.getLocalLabel(context.locale)}" ontology=ontology>
	<#import "__visualize_class_objectproperties.ftl" as vis/>

<h2 class="title text-center"><i class="fa fa-bullseye"></i> ${clazz.getLocalLabel(context.locale)} (${clazz.individualsCount})</h2>
<#if (clazz.superClasses?size > 0) >
	<#list clazz.superClasses as superclazz>
	<#if (superclazz.label != "undefined") >
		<h3><a href="select?class=${superclazz.iri?url}"><i class="fa fa-bullseye"></i> ${superclazz.getLocalLabel(context.locale)}</a></h3>
	</#if>
	</#list>
</#if>

<div class="post-meta">
    <ul>
        <#list clazz.expressions as expression>
            <li style="display:block; clear:both; margin-bottom:3px;">
                <#if expression.intersectionOf>
                    ${messages("__classexpression.intersectionOf")}&nbsp;&nbsp;
                </#if>
                <#if expression.unionOf>
                    ${messages("__classexpression.unionOf")}&nbsp;&nbsp;
                </#if>
                <#if expression.complementOf>
                    ${messages("__classexpression.complementOf")}&nbsp;&nbsp;
                </#if>
                <#if expression.someValuesFrom>
                    ${messages("__classexpression.someValuesFrom")}&nbsp;&nbsp;
                </#if>
                <#if expression.allValuesFrom>
                    ${messages("__classexpression.allValuesFrom")}&nbsp;&nbsp;
                </#if>
                <#list expression.properties as p>
                    <a href="select?objectproperty=${p.iri?url}"><i class="fa fa-long-arrow-right"></i> ${p.getLocalLabel(context.locale)}</a>&nbsp;&nbsp;
                </#list>
                <#if expression.hasFiller>
                    <#list expression.filler as filler>
                        <#if filler.class><a href="select?class=${filler.iri?url}"><i class="fa fa-bullseye"></i> ${filler.getLocalLabel(context.locale)}</a>&nbsp;&nbsp;</#if>
                        <#if filler.individual><a href="select?individual=${filler.iri?url}"><i class="fa fa-circle-o"></i> ${filler.getLocalLabel(context.locale)}</a>&nbsp;&nbsp;</#if>
                    </#list>
                </#if>
                <#list expression.operands as operandClass>
                     <a href="select?class=${operandClass.iri?url}"><i class="fa fa-bullseye"></i> ${operandClass.getLocalLabel(context.locale)}</a>&nbsp;&nbsp;
                </#list>

              </li>
        </#list>
    </ul>
</div><br />

<#if (clazz.subClasses?size > 0) >
	<div class="post-meta">
		<h4>${messages("__class.subclasses.seealso")}</h4>
		<ul>
		<#list clazz.subClasses as subclazz>
			<li><a href="select?class=${subclazz.iri?url}"><i class="fa fa-bullseye"></i> ${subclazz.getLocalLabel(context.locale)} (${subclazz.individualsCount})</a></li>
		</#list>
		</ul>	
		</p>
	</div><br />
</#if>

<#if (clazz.annotations?size > 0) >
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">${messages("__class.annotations.panel.title")}</h4>
		</div>
		<div id="annotationsPanel" class="panel-collapse expand">
			<div class="panel-body">
				<table class="table">
					<#list clazz.annotations as annotation>
						<#if (annotation.value.lang?? && context.locale.language == annotation.value.lang) >
							<tr>
				                <td style="vertical-align:top">${annotation.property}</td>
				                <td>${annotation.value.literal!}</td>
				                <td>${annotation.value.lang!}</td>
			                </tr>
		                </#if>
					</#list>
				</table>
			</div>
		</div>
	</div>
</#if>

<#if (clazz.objectProperties?size > 0) >
	<br/>
			
	<div class="panel panel-default">
		<div class="panel-heading">
			<span class="pull-right" id="graphControl">		
			    <a data-toggle="collapse" data-parent="#graphPanel" href="#graphPanel"><span class="badge"><i class="fa fa-plus"></i></span></a>
		    </span>
			<h4 class="panel-title">
				${messages("__class.graph.panel.title")}
			</h4>
		</div>
		<div id="graphPanel" class="panel-collapse collapse in">
			<div class="panel-body">
				<!-- visjs graph will be drawn in this div -->
				<div id="classGraph"></div>
				<table class="table">
					<#list clazz.objectProperties as objectProperty>
						<tr>
							<td><div class="post-meta">
									<ul>
										<#list objectProperty.domain as classVO>
									 	    <li><nobr><i class="fa fa-bullseye"></i><a href="select?class=${classVO.iri?url}">${classVO.getLocalLabel(context.locale)} ${classVO.extra!}</a></nobr></li>
									    </#list>
									 </ul>
							    </div>
						    <td>
							<td><a href="select?objectproperty=${objectProperty.iri?url}"><nobr><i class="fa fa-long-arrow-right"></i> ${objectProperty.getLocalLabel(context.locale)}</nobr></a></td>
						    <td>
						    	<div class="post-meta">
						    		<ul>
									    <#list objectProperty.range as classVO>
									 	    <li><nobr><i class="fa fa-bullseye"></i><a href="select?class=${classVO.iri?url}">${classVO.getLocalLabel(context.locale)}</a></nobr></li>
									    </#list>
								    </ul>
							    </div>
						    </td>
		                </tr>
					</#list>
				</table>
			</div>
		</div>
	</div>

</#if>

<#if (clazz.individualsCount > 0) >
	<#list clazz.individuals as individual>
		<div class="panel-group" id="panel${individual_index}">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="post-meta">		
						<ul>
							<li><i class="fa fa-circle-o"></i> <a href="select?individual=${individual.iri?url}">${individual.getLocalLabel(context.locale)}</a></li>
						</ul>	
						<span class="pull-right">			
							<ul>
								<#list individual.classes as classVO>
								   <#if clazz.label != classVO.label>
							 	    	<li><i class="fa fa-bullseye"></i><a href="select?class=${classVO.iri?url}">${classVO.getLocalLabel(context.locale)}</a></li>
							 	    </#if>
							    </#list>
						    </ul>
						    <a data-toggle="collapse" data-parent="#panel${individual_index}" href="#${individual.filename}"><span class="badge"><i class="fa fa-plus"></i></span></a>
					    </span>
					</div>
				</div>
				<div id="${individual.filename}" class="panel-collapse collapse">
					<div class="panel-body">
						<#if (individual.annotations?size > 0) >		
							<table class="table">
								<#list individual.annotations as annotation>
									<tr>
									  <td style="vertical-align:top">${annotation.property!}</td>
						              <td>${annotation.value.literal!}</td>
						              <td>${annotation.value.lang!}</td>
						            </tr>
								</#list>
							</table>
						</#if>
						
						<div>
							<p>
								<table class="table">
								<#list individual.objectProperties as p>
									<tr>
					                    <td><a href="select?objectproperty=${p.iri?url}"><i class="fa fa-long-arrow-right"></i> ${p.getLocalLabel(context.locale)}</a></td>
					                    <td><a href="select?individual=${p.object.iri?url}"><i class="fa fa-circle-o"></i> ${p.object.getLocalLabel(context.locale)}</a></td>	                    
					                    <td><#list p.object.classes as clazz><a href="select?class=${clazz.iri?url}"><i class="fa fa-bullseye"></i> ${clazz.getLocalLabel(context.locale)}</a> </#list></td>
					                </tr>
								</#list>
								</table>
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
	</#list>
</#if>
<br/>

	<@vis.visualizeClass graphid="classGraph" clazz=clazz /> 
	
</@c.page>