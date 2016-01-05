<#import "__common.ftl" as c/>
<@c.page title="Object property" ontology=ontology>

<h1>${objectProperty.propertyLabel}</h1>
<table>
<#list objectProperty.referencingAxioms as axiom>
     <tr>
         <td><a href="${axiom.subject.label}.html">${axiom.subject.label}</a></td>
         <td><a href="${axiom.propertyLabel}.html">${axiom.propertyLabel}</a></td>
         <td><a href="${axiom.object.label}.html">${axiom.object.label}</a></td>
     </tr>
</#list>
</table>

</@c.page>