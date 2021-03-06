Feature: Ontology Index Page in English
  In order to navigate the ontology a landing or index page should be available.

  Background:
    Given browser 'firefox' with language set to 'english'

  Scenario Outline: Show index page in english
    Given an ontology with name '<ontologyName>' and url '<url>'
    When the user navigates to the ontology index page
    Then an index page of the ontology should be available
    And the class '<classes>' should clickable

  Examples:
  | ontologyName    | url                                                                        | classes           |
  | wine            |  http://localhost:8080/OWL2HTML-web/select?ontology=public/wine.xml        | Winery            |
  | procurement     |  http://localhost:8080/OWL2HTML-web/select?ontology=public/pproc_1.0.0.rdf | Oferta adjudicada |
