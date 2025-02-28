# Changelog

## 1.4.0 (unreleased)

- Package change from `com.structurizr.io` to `com.structurizr.export`.

## 1.3.0 (29th December 2021)

- Fixes a bug when exporting views to PlantUML formats, when there are newline characters in element names/descriptions/technologies and relationship descriptions/technologies.
- The C4-PlantUML export now includes tags.
- Adds support for customizing PlantUML exports via view set properties (plantuml.title, plantuml.includes, etc).

## v1.2.1 (29th November 2021)

- Adds support for hyperlinked elements via the StructurizrPlantUML and C4-PlantUML exporters.
- Adds support for styling groups via an element style named `Group' (for all groups) or `Group:Name` (for the "Name" group).

### v1.2.0 (9th September 2021)

- Adds support for C4-PlantUML `SHOW_LEGEND()`.
- Identifiers in PlantUML exports are now based upon element names, rather than internal IDs (#59).

### v1.1.1 (9th June 2021)

- Adds support for "left to right direction" layouts with C4-PlantUML.

### v1.1.0 (7th June 2021)

- Adds support for "external" software system/container boundaries on dynamic views.
- Adds support for more shapes (pipe and hexagon) via the StructurizrPlantUMLExporter.
- Adds support for exporting animations (StructurizrPlantUML and C4-PlantUML only).

### v1.0.1 (29th April 2021)

- Trying to render a sequence diagram with C4-PlantUML now throws an unsupported exception, as C4-PlantUML doesn't natively support sequence diagrams.

### v1.0.0 (27th April 2021)

- Initial version, refactored from existing (and separate) PlantUML, Mermaid, DOT, WebSequenceDiagrams, and Ilograph exporters.