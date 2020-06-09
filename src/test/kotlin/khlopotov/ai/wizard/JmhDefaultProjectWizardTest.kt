package khlopotov.ai.wizard

import com.intellij.openapi.vfs.LocalFileSystem
import khlopotov.ai.JmhWizardBaseTest
import khlopotov.ai.file.templates.jmhBenchmarkFileTemplate
import khlopotov.ai.file.templates.jmhPomFileTemplate
import khlopotov.ai.project.templates.JMHMavenProjectTemplate.Companion.JMH_MAVEN_PROJECT_NAME
import khlopotov.ai.project.templates.JMHTemplateFactory.Companion.JMH_GROUP_NAME
import org.junit.Test

class JmhDefaultProjectWizardTest : JmhWizardBaseTest() {

    override val skipSetUp: Boolean = true

    @Test fun test() {
        val defaultProject = createProjectFromTemplate(JMH_GROUP_NAME, JMH_MAVEN_PROJECT_NAME, null)
        val projectRoot = LocalFileSystem.getInstance().findFileByPath(defaultProject.basePath!!)!!

        val actualPom = projectRoot.findChild("pom.xml")!!
        val actualSourceFile =
            projectRoot.findFileByRelativePath("src/main/java/org/example/JavaBenchmarkTemplate.java")!!

        val expectedPom = jmhPomFileTemplate(
            "org.example",
            defaultProject.name,
            "1.0-SNAPSHOT",
            "1.23",
            "1.8"
        )
        val expectedSourceFile = jmhBenchmarkFileTemplate("org.example")

        assertEquals("Wrong pom file was created", expectedPom, actualPom.getText())
        assertEquals("Wrong template file was created", expectedSourceFile, actualSourceFile.getText())
    }

}