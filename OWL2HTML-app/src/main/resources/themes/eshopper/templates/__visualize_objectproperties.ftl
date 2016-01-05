<!-- this macro recursively generates the json data for a dataproperty chart -->
<#macro graphObjectProperties objectproperty depth maxdepth>
  <#if (depth?number < maxdepth?number) >
	  '"${objectproperty.subject.getLocalLabel(context.locale)?json_string}" -> "${objectproperty.object.getLocalLabel(context.locale)?json_string}" [ label="${objectproperty.getLocalLabel(context.locale)?json_string}"];'+
	  '"${objectproperty.subject.getLocalLabel(context.locale)?json_string}" [group="ObjectProperty"];'+
	  <#list objectproperty.object.objectProperties as childProperty>
	  		<@graphObjectProperties objectproperty=childProperty depth=depth+1 maxdepth=maxdepth/>
	  </#list>
  </#if>
</#macro>

<!-- this macro recursively generates the json data for a dataproperty chart -->
<#macro graphReferencingAxioms referencingaxiom depth maxdepth>
  <#if (depth?number < maxdepth?number) >
	  '"${referencingaxiom.subject.getLocalLabel(context.locale)?json_string}" -> "${referencingaxiom.object.getLocalLabel(context.locale)?json_string}" [ label="${referencingaxiom.getLocalLabel(context.locale)?json_string}"];'+
 	  '"${referencingaxiom.subject.getLocalLabel(context.locale)?json_string}" [group="ReferencingAxiom"];'+
	  <#list referencingaxiom.subject.referencingAxioms as childProperty>
	  		<@graphReferencingAxioms referencingaxiom=childProperty depth=depth+1 maxdepth=maxdepth/>
	  </#list>
  </#if>
</#macro>

<#macro graphjs graphid individual depth maxdepth>
<script language="JavaScript">
    function bodyLoad() {
        var options = {
			width:  '100%',
		    height: '500px',
		    edges: {
			  color: '#FE980F',
			  width: 1,
			  labelAlignment: 'horizontal',
			  arrows: {
			      to:     {scaleFactor:0.5}
			  },
			  style: 'dash-line'
		    },
		    nodes: {
			  color: {
			    background: 'white',
			    border: 'FE980F',
			    highlight: {
			      background: 'FE980F',
			      border: 'black'
			    }
			  },
			  shape: 'box',
			  radius: 14
		   },
		   groups: {
		    SelectedIndividual: {
                color: {background:'FE980F', border:'black'},
                shape: 'box',
                radius: 14
            },
            ObjectProperty: {
                color: {background:'white', border:'blue'}
            },
            ReferencingAxiom: {
                color: {background:'white', border:'green'}
            }
        }
		   
		};
		
      	
  		var container${graphid} = document.getElementById('${graphid}');
		var data${graphid} = {
			dot: 'strict digraph {'+
				<#list individual.objectProperties as objProp>
					<@graphObjectProperties objectproperty=objProp depth=depth maxdepth=maxdepth/> 
				</#list>
				<#list individual.referencingAxioms as refAxiom>
					<@graphReferencingAxioms referencingaxiom=refAxiom depth=depth maxdepth=maxdepth/> 
				</#list>
				 '"${individual.getLocalLabel(context.locale)?json_string}" [group="SelectedIndividual"];'+
			'}'
		};
		new vis.Network(container${graphid}, data${graphid}, options);
		
		        
    }
</script>
</#macro>