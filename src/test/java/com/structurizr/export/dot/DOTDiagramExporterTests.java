package com.structurizr.export.dot;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.export.plantuml.StructurizrPlantUMLExporter;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class DOTDiagramExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/structurizr-36141-workspace.json"));
        DOTExporter dotWriter = new DOTExporter();

        Collection<Diagram> diagrams = dotWriter.export(workspace);
        assertEquals(7, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/dot/36141-SystemLandscape.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemContext")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/36141-SystemContext.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/36141-Containers.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/36141-Components.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("SignIn")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/36141-SignIn.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("DevelopmentDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/36141-DevelopmentDeployment.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("LiveDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/36141-LiveDeployment.dot"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/structurizr-54915-workspace.json"));
        ThemeUtils.loadThemes(workspace);
        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);

        DOTExporter exporter = new DOTExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/dot/54915-AmazonWebServicesDeployment.dot"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_GroupsExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/groups.json"));
        ThemeUtils.loadThemes(workspace);

        DOTExporter exporter = new DOTExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(3, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/dot/groups-SystemLandscape.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/groups-Containers.dot"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/dot/groups-Components.dot"));
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
        Diagram diagram = new DOTExporter().export(containerView);
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [fontname=\"Arial\", rankdir=TB, ranksep=1.0, nodesep=1.0]\n" +
                "  node [fontname=\"Arial\", shape=box, margin=\"0.4,0.3\"]\n" +
                "  edge [fontname=\"Arial\"]\n" +
                "  label=<<br /><font point-size=\"34\">Software System 1 - Containers</font>>\n" +
                "\n" +
                "  subgraph cluster_1 {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Software System 1</font><br /><font point-size=\"19\">[Software System]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#444444\"\n" +
                "    fontcolor=\"#444444\"\n" +
                "    fillcolor=\"#444444\"\n" +
                "\n" +
                "    2 [id=2,shape=rect, label=<<font point-size=\"34\">Container 1</font><br /><font point-size=\"19\">[Container]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph cluster_3 {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Software System 2</font><br /><font point-size=\"19\">[Software System]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#cccccc\"\n" +
                "    fontcolor=\"#cccccc\"\n" +
                "    fillcolor=\"#cccccc\"\n" +
                "\n" +
                "    4 [id=4,shape=rect, label=<<font point-size=\"34\">Container 2</font><br /><font point-size=\"19\">[Container]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  2 -> 4 [id=5, label=<<font point-size=\"24\">Uses</font>>, style=\"dashed\", color=\"#707070\", fontcolor=\"#707070\"]\n" +
                "}", diagram.getDefinition());

        containerView.setExternalSoftwareSystemBoundariesVisible(false);
        diagram = new DOTExporter().export(containerView);
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [fontname=\"Arial\", rankdir=TB, ranksep=1.0, nodesep=1.0]\n" +
                "  node [fontname=\"Arial\", shape=box, margin=\"0.4,0.3\"]\n" +
                "  edge [fontname=\"Arial\"]\n" +
                "  label=<<br /><font point-size=\"34\">Software System 1 - Containers</font>>\n" +
                "\n" +
                "  subgraph cluster_1 {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Software System 1</font><br /><font point-size=\"19\">[Software System]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#444444\"\n" +
                "    fontcolor=\"#444444\"\n" +
                "    fillcolor=\"#444444\"\n" +
                "\n" +
                "    2 [id=2,shape=rect, label=<<font point-size=\"34\">Container 1</font><br /><font point-size=\"19\">[Container]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  4 [id=4,shape=rect, label=<<font point-size=\"34\">Container 2</font><br /><font point-size=\"19\">[Container]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "\n" +
                "  2 -> 4 [id=5, label=<<font point-size=\"24\">Uses</font>>, style=\"dashed\", color=\"#707070\", fontcolor=\"#707070\"]\n" +
                "}", diagram.getDefinition());
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
        Diagram diagram = new DOTExporter().export(componentView);
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [fontname=\"Arial\", rankdir=TB, ranksep=1.0, nodesep=1.0]\n" +
                "  node [fontname=\"Arial\", shape=box, margin=\"0.4,0.3\"]\n" +
                "  edge [fontname=\"Arial\"]\n" +
                "  label=<<br /><font point-size=\"34\">Software System 1 - Container 1 - Components</font>>\n" +
                "\n" +
                "  subgraph cluster_2 {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Container 1</font><br /><font point-size=\"19\">[Container]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#444444\"\n" +
                "    fontcolor=\"#444444\"\n" +
                "    fillcolor=\"#444444\"\n" +
                "\n" +
                "    3 [id=3,shape=rect, label=<<font point-size=\"34\">Component 1</font><br /><font point-size=\"19\">[Component]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph cluster_5 {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Container 2</font><br /><font point-size=\"19\">[Container]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#cccccc\"\n" +
                "    fontcolor=\"#cccccc\"\n" +
                "    fillcolor=\"#cccccc\"\n" +
                "\n" +
                "    6 [id=6,shape=rect, label=<<font point-size=\"34\">Component 2</font><br /><font point-size=\"19\">[Component]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  3 -> 6 [id=7, label=<<font point-size=\"24\">Uses</font>>, style=\"dashed\", color=\"#707070\", fontcolor=\"#707070\"]\n" +
                "}", diagram.getDefinition());

        componentView.setExternalSoftwareSystemBoundariesVisible(false);
        diagram = new DOTExporter().export(componentView);
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [fontname=\"Arial\", rankdir=TB, ranksep=1.0, nodesep=1.0]\n" +
                "  node [fontname=\"Arial\", shape=box, margin=\"0.4,0.3\"]\n" +
                "  edge [fontname=\"Arial\"]\n" +
                "  label=<<br /><font point-size=\"34\">Software System 1 - Container 1 - Components</font>>\n" +
                "\n" +
                "  subgraph cluster_2 {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Container 1</font><br /><font point-size=\"19\">[Container]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#444444\"\n" +
                "    fontcolor=\"#444444\"\n" +
                "    fillcolor=\"#444444\"\n" +
                "\n" +
                "    3 [id=3,shape=rect, label=<<font point-size=\"34\">Component 1</font><br /><font point-size=\"19\">[Component]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  6 [id=6,shape=rect, label=<<font point-size=\"34\">Component 2</font><br /><font point-size=\"19\">[Component]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "\n" +
                "  3 -> 6 [id=7, label=<<font point-size=\"24\">Uses</font>>, style=\"dashed\", color=\"#707070\", fontcolor=\"#707070\"]\n" +
                "}", diagram.getDefinition());
    }

    @Test
    public void test_renderGroupStyles() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User 1").setGroup("Group 1");
        workspace.getModel().addPerson("User 2").setGroup("Group 2");
        workspace.getModel().addPerson("User 3").setGroup("Group 3");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "");
        view.addDefaultElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 1").color("#111111");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 2").color("#222222");

        DOTExporter exporter = new DOTExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [fontname=\"Arial\", rankdir=TB, ranksep=1.0, nodesep=1.0]\n" +
                "  node [fontname=\"Arial\", shape=box, margin=\"0.4,0.3\"]\n" +
                "  edge [fontname=\"Arial\"]\n" +
                "  label=<<br /><font point-size=\"34\">System Landscape</font>>\n" +
                "\n" +
                "  subgraph \"cluster_group_Group 1\" {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Group 1</font><br /><font point-size=\"19\">[Group]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#111111\"\n" +
                "    fontcolor=\"#111111\"\n" +
                "    fillcolor=\"#ffffff\"\n" +
                "\n" +
                "    1 [id=1,shape=rect, label=<<font point-size=\"34\">User 1</font><br /><font point-size=\"19\">[Person]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph \"cluster_group_Group 2\" {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Group 2</font><br /><font point-size=\"19\">[Group]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#222222\"\n" +
                "    fontcolor=\"#222222\"\n" +
                "    fillcolor=\"#ffffff\"\n" +
                "\n" +
                "    2 [id=2,shape=rect, label=<<font point-size=\"34\">User 2</font><br /><font point-size=\"19\">[Person]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph \"cluster_group_Group 3\" {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Group 3</font><br /><font point-size=\"19\">[Group]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#cccccc\"\n" +
                "    fontcolor=\"#cccccc\"\n" +
                "    fillcolor=\"#ffffff\"\n" +
                "\n" +
                "    3 [id=3,shape=rect, label=<<font point-size=\"34\">User 3</font><br /><font point-size=\"19\">[Person]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "\n" +
                "}", diagram.getDefinition());

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group").color("#aabbcc");

        diagram = exporter.export(view);
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [fontname=\"Arial\", rankdir=TB, ranksep=1.0, nodesep=1.0]\n" +
                "  node [fontname=\"Arial\", shape=box, margin=\"0.4,0.3\"]\n" +
                "  edge [fontname=\"Arial\"]\n" +
                "  label=<<br /><font point-size=\"34\">System Landscape</font>>\n" +
                "\n" +
                "  subgraph \"cluster_group_Group 1\" {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Group 1</font><br /><font point-size=\"19\">[Group]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#111111\"\n" +
                "    fontcolor=\"#111111\"\n" +
                "    fillcolor=\"#ffffff\"\n" +
                "\n" +
                "    1 [id=1,shape=rect, label=<<font point-size=\"34\">User 1</font><br /><font point-size=\"19\">[Person]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph \"cluster_group_Group 2\" {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Group 2</font><br /><font point-size=\"19\">[Group]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#222222\"\n" +
                "    fontcolor=\"#222222\"\n" +
                "    fillcolor=\"#ffffff\"\n" +
                "\n" +
                "    2 [id=2,shape=rect, label=<<font point-size=\"34\">User 2</font><br /><font point-size=\"19\">[Person]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph \"cluster_group_Group 3\" {\n" +
                "    margin=25\n" +
                "    label=<<font point-size=\"24\"><br />Group 3</font><br /><font point-size=\"19\">[Group]</font>>\n" +
                "    labelloc=b\n" +
                "    color=\"#aabbcc\"\n" +
                "    fontcolor=\"#aabbcc\"\n" +
                "    fillcolor=\"#ffffff\"\n" +
                "\n" +
                "    3 [id=3,shape=rect, label=<<font point-size=\"34\">User 3</font><br /><font point-size=\"19\">[Person]</font>>, style=filled, color=\"#9a9a9a\", fillcolor=\"#dddddd\", fontcolor=\"#000000\"]\n" +
                "  }\n" +
                "\n" +
                "\n" +
                "}", diagram.getDefinition());
    }

}