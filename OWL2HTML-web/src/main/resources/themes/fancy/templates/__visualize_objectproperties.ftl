<#macro graphjs graphid individual maxdepth>
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
			    border: '#FE980F',
			    highlight: {
			      background: '#FE980F',
			      border: 'black'
			    }
			  },
			  shape: 'box',
			  radius: 14
		   },
		   groups: {
		    SelectedIndividual: {
                color: {background:'#FE980F', border:'black'},
                shape: 'box',
                radius: 14
            },
            ObjectProperty: {
                color: {background:'white', border:'blue'}
            }
        }
		   
		};
		
      	
  		var container${graphid} = document.getElementById('${graphid}');
		var data${graphid} = {
			dot: "strict digraph {"+
				<#list individual.getObjectPropertiesGraph(maxdepth?number)?values as objectproperty>
					 "\"${objectproperty.subject.getLocalLabel(context.locale)?json_string}\" -> \"${objectproperty.object.getLocalLabel(context.locale)?json_string}\" [ label=\"${objectproperty.getLocalLabel(context.locale)?json_string}\"];"+
	  				 "\"${objectproperty.subject.getLocalLabel(context.locale)?json_string}\" [group=\"ObjectProperty\"];"+
				</#list>
				 "\"${individual.getLocalLabel(context.locale)?json_string}\" [group=\"SelectedIndividual\"];"+
			"}"
		};
		new vis.Network(container${graphid}, data${graphid}, options);
		
    }
</script>
</#macro>