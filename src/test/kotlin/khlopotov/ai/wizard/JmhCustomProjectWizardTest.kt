package khlopotov.ai.wizard

import khlopotov.ai.JmhWizardBaseTest
import khlopotov.ai.file.templates.jmhBenchmarkFileTemplate
import khlopotov.ai.file.templates.jmhPomFileTemplate
import org.junit.Test

class JmhCustomProjectWizardTest : JmhWizardBaseTest() {

    @Test fun test() {
        val actualPom = jmhProjectVirtualFile.findChild("pom.xml")!!
        val actualSourceFile =
            jmhProjectVirtualFile.findFileByRelativePath("src/main/java/jmh/unit/test/JavaBenchmarkTemplate.java")!!

        val expectedPom = jmhPomFileTemplate(
            JMH_UNIT_TEST_PKG,
            jmhProjectName,
            "1.0-SNAPSHOT",
            "1.23",
            "1.8"
        )
        val expectedSourceFile = jmhBenchmarkFileTemplate(JMH_UNIT_TEST_PKG)

        assertEquals("Wrong pom file was created", expectedPom, actualPom.getText())
        assertEquals("Wrong template file was created", expectedSourceFile, actualSourceFile.getText())
    }
}