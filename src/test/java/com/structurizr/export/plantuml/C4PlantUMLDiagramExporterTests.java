package com.structurizr.export.plantuml;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class C4PlantUMLDiagramExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/structurizr-36141-workspace.json"));
        C4PlantUMLExporter exporter = new C4PlantUMLExporter();

        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(7, diagrams.size());

        Diagram diagram = diagrams.stream().filter(d -> d.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/36141-SystemLandscape.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("SystemContext")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/36141-SystemContext.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/36141-Containers.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/36141-Components.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/36141-SignIn.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("DevelopmentDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/36141-DevelopmentDeployment.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("LiveDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/36141-LiveDeployment.puml"));
        assertEquals(expected, diagram.getDefinition());

        try {
            // and the sequence diagram version ... which isn't supported
            workspace.getViews().getConfiguration().addProperty(exporter.PLANTUML_SEQUENCE_DIAGRAMS_PROPERTY, "true");
            exporter.export(workspace);
        } catch (UnsupportedOperationException uoe) {
            assertEquals("Sequence diagrams are not supported by C4-PlantUML", uoe.getMessage());
        }
    }

    @Test
    public void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/structurizr-54915-workspace.json"));
        ThemeUtils.loadThemes(workspace);
        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/54915-AmazonWebServicesDeployment.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_GroupsExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/groups.json"));
        ThemeUtils.loadThemes(workspace);

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(3, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/groups-SystemLandscape.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/groups-Containers.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/c4plantuml/groups-Components.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_renderContainerDiagramWithExternalContainers() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");

        container1.uses(container2, "Uses");

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem1, "Containers", "");
        containerView.add(container1);
        containerView.add(container2);

        containerView.setExternalSoftwareSystemBoundariesVisible(true);
        Diagram diagram = new C4PlantUMLExporter().export(containerView);
        assertEquals("@startuml\n" +
                "title Software System 1 - Containers\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml\n" +
                "\n" +
                "System_Boundary(\"SoftwareSystem1_boundary\", \"Software System 1\") {\n" +
                "  Container(SoftwareSystem1.Container1, \"Container 1\", \"\", $tags=\"Element+Container\")\n" +
                "}\n" +
                "\n" +
                "System_Boundary(\"SoftwareSystem2_boundary\", \"Software System 2\") {\n" +
                "  Container(SoftwareSystem2.Container2, \"Container 2\", \"\", $tags=\"Element+Container\")\n" +
                "}\n" +
                "\n" +
                "Rel_D(SoftwareSystem1.Container1, SoftwareSystem2.Container2, \"Uses\", $tags=\"Relationship\")\n" +
                "\n" +
                "SHOW_LEGEND()\n" +
                "@enduml", diagram.getDefinition());


        containerView.setExternalSoftwareSystemBoundariesVisible(false);
        diagram = new C4PlantUMLExporter().export(containerView);
        assertEquals("@startuml\n" +
                "title Software System 1 - Containers\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml\n" +
                "\n" +
                "System_Boundary(\"SoftwareSystem1_boundary\", \"Software System 1\") {\n" +
                "  Container(SoftwareSystem1.Container1, \"Container 1\", \"\", $tags=\"Element+Container\")\n" +
                "}\n" +
                "\n" +
                "Container(SoftwareSystem2.Container2, \"Container 2\", \"\", $tags=\"Element+Container\")\n" +
                "\n" +
                "Rel_D(SoftwareSystem1.Container1, SoftwareSystem2.Container2, \"Uses\", $tags=\"Relationship\")\n" +
                "\n" +
                "SHOW_LEGEND()\n" +
                "@enduml", diagram.getDefinition());
    }

    @Test
    public void test_renderComponentDiagramWithExternalComponents() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component2 = container2.addComponent("Component 2");

        component1.uses(component2, "Uses");

        ComponentView componentView = workspace.getViews().createComponentView(container1, "Components", "");
        componentView.add(component1);
        componentView.add(component2);

        componentView.setExternalSoftwareSystemBoundariesVisible(true);
        Diagram diagram = new C4PlantUMLExporter().export(componentView);
        assertEquals("@startuml\n" +
                "title Software System 1 - Container 1 - Components\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml\n" +
                "\n" +
                "Container_Boundary(\"SoftwareSystem1.Container1_boundary\", \"Container 1\") {\n" +
                "  Component(SoftwareSystem1.Container1.Component1, \"Component 1\", \"\", $tags=\"Element+Component\")\n" +
                "}\n" +
                "\n" +
                "Container_Boundary(\"SoftwareSystem2.Container2_boundary\", \"Container 2\") {\n" +
                "  Component(SoftwareSystem2.Container2.Component2, \"Component 2\", \"\", $tags=\"Element+Component\")\n" +
                "}\n" +
                "\n" +
                "Rel_D(SoftwareSystem1.Container1.Component1, SoftwareSystem2.Container2.Component2, \"Uses\", $tags=\"Relationship\")\n" +
                "\n" +
                "SHOW_LEGEND()\n" +
                "@enduml", diagram.getDefinition());

        componentView.setExternalSoftwareSystemBoundariesVisible(false);
        diagram = new C4PlantUMLExporter().export(componentView);
        assertEquals("@startuml\n" +
                "title Software System 1 - Container 1 - Components\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml\n" +
                "\n" +
                "Container_Boundary(\"SoftwareSystem1.Container1_boundary\", \"Container 1\") {\n" +
                "  Component(SoftwareSystem1.Container1.Component1, \"Component 1\", \"\", $tags=\"Element+Component\")\n" +
                "}\n" +
                "\n" +
                "Component(SoftwareSystem2.Container2.Component2, \"Component 2\", \"\", $tags=\"Element+Component\")\n" +
                "\n" +
                "Rel_D(SoftwareSystem1.Container1.Component1, SoftwareSystem2.Container2.Component2, \"Uses\", $tags=\"Relationship\")\n" +
                "\n" +
                "SHOW_LEGEND()\n" +
                "@enduml", diagram.getDefinition());
    }

    @Test
    public void test_renderDiagramWithElementUrls() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        softwareSystem.setUrl("https://structurizr.com");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("@startuml\n" +
                "title System Landscape\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml\n" +
                "\n" +
                "System(SoftwareSystem, \"Software System\", \"\", $tags=\"Element+Software System\")[[https://structurizr.com]]\n" +
                "\n" +
                "\n" +
                "SHOW_LEGEND()\n" +
                "@enduml", diagram.getDefinition());
    }

    @Test
    public void test_renderDiagramWithIncludes() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Software System");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        view.getViewSet().getConfiguration().addProperty(C4PlantUMLExporter.PLANTUML_INCLUDES_PROPERTY, "styles.puml");

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("@startuml\n" +
                "title System Landscape\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml\n" +
                "!include styles.puml\n" +
                "\n" +
                "System(SoftwareSystem, \"Software System\", \"\", $tags=\"Element+Software System\")\n" +
                "\n" +
                "\n" +
                "SHOW_LEGEND()\n" +
                "@enduml", diagram.getDefinition());
    }

    @Test
    public void test_renderDiagramWithNewLineCharacterInElementName() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Software\nSystem");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("@startuml\n" +
                "title System Landscape\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml\n" +
                "!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml\n" +
                "\n" +
                "System(SoftwareSystem, \"Software\\nSystem\", \"\", $tags=\"Element+Software System\")\n" +
                "\n" +
                "\n" +
                "SHOW_LEGEND()\n" +
                "@enduml", diagram.getDefinition());
    }

}