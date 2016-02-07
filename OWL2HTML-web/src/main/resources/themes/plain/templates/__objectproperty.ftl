<#import "__common.ftl" as c/>
<@c.page title=messages("__objectproperties.page.title") ontology=ontology>

<h1>${objectProperty.getLocalLabel(context.locale)}</h1>
<table>
<#list objectProperty.referencingAxioms as axiom>
     <tr>
         <td><a href="select?individual=${axiom.subject.iri?url}">${axiom.subject.getLocalLabel(context.locale)}</a></td>
         <td><a href="select?objectproperty=${axiom.iri?url}">${axiom.getLocalLabel(context.locale)}</a></td>
         <td><a href="select?individual=${axiom.object.iri?url}">${axiom.object.getLocalLabel(context.locale)}</a></td>
     </tr>
</#list>
</table>

</@c.page>