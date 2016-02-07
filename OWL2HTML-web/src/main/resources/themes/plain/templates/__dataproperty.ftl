<#import "__common.ftl" as c/>
<@c.page title=messages("__dataproperties.page.title") ontology=ontology>

<h1>${dataProperty.getLocalLabel(context.locale)}</h1>

<table>
   <tr><th>subject</th><th>property</th><th>value</th><th>language</th></tr>
   <#list dataProperty.referencingAxioms as p>
   <tr>
        <td><a href="select?individual=${p.subject.iri?url}">${p.subject.getLocalLabel(context.locale)}</a></td>
        <td><a href="select?dataproperty=${p.iri?url}">${p.getLocalLabel(context.locale)}</a></td>
        <td>${p.object.literal!}</td>
        <td>${p.object.lang!}</td>
    </tr>
</#list>
</table>
</@c.page>