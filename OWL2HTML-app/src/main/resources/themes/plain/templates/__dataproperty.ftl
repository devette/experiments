<#import "__common.ftl" as c/>
<@c.page title="Data property" ontology=ontology>

<h1>${dataProperty.propertyLabel}</h1>

<table>
   <tr><th>subject</th><th>property</th><th>value</th><th>language</th></tr>
   <#list dataProperty.referencingAxioms as p>
   <tr>
        <td><a href="${p.subject.label}.html">${p.subject.label}</a></td>
        <td><a href="${p.propertyLabel}.html">${p.propertyLabel}</a></td>        
        <td>${p.object.literal!}</td>
        <td>${p.object.lang!}</td>
    </tr>
</#list>
</table>
</@c.page>