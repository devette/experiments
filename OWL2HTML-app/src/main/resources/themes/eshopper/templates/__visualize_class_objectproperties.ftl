<#macro visualizeClass graphid clazz>
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
		    SelectedClass: {
                color: {background:'FE980F', border:'black'},
                shape: 'box',
                radius: 14
            },
            ObjectProperty: {
                color: {background:'white', border:'blue'}
            },
        }
		   
		};
		
      	
  		var container${graphid} = document.getElementById('${graphid}');
		var data${graphid} = {
			dot: 'strict digraph {'+
				<#list clazz.objectProperties as objectProperty>
					<#list objectProperty.domain as domainClassVO>
					    <#list objectProperty.range as rangeClassVO>
					    	'"${domainClassVO.getLocalLabel(context.locale)?json_string}" -> "${rangeClassVO.getLocalLabel(context.locale)?json_string}" [ label="${objectProperty.getLocalLabel(context.locale)?json_string}"];'+
					 	</#list>
				    </#list>
				</#list>
				 '"${clazz.getLocalLabel(context.locale)?json_string}" [group="SelectedClass"];'+
			'}'
		};
		new vis.Network(container${graphid}, data${graphid}, options);
		
		        
    }
</script>
</#macro>