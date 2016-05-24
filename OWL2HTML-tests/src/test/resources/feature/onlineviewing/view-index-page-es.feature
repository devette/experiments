Feature: Ontology Index Page in Spanish
  In order to navigate the ontology a landing or index page should be available.

  Background:
    Given browser 'firefox' with language set to 'spanish'

  Scenario Outline: Show index page in spanish
    Given an ontology with name '<ontologyName>' and url '<url>'
    When the user navigates to the ontology index page
    Then an index page of the ontology should be available
    And the class '<classes>' should clickable

  Examples:
  | ontologyName    | url                                                                        | classes           |
  | wine            |  http://localhost:8080/OWL2HTML-web/select?ontology=owl/public/wine.xml        | Winery            |
  | procurement     |  http://localhost:8080/OWL2HTML-web/select?ontology=owl/public/pproc_1.0.0.rdf | Oferta adjudicada |
