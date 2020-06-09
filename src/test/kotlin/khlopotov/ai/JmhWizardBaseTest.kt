package khlopotov.ai

import com.intellij.ide.projectWizard.NewProjectWizardTestCase
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import khlopotov.ai.project.templates.JMHMavenProjectTemplate.Companion.JMH_MAVEN_PROJECT_NAME
import khlopotov.ai.project.templates.JMHTemplateFactory.Companion.JMH_GROUP_NAME
import org.jetbrains.idea.maven.wizards.MavenStructureWizardStep

abstract class JmhWizardBaseTest: NewProjectWizardTestCase() {

    protected lateinit var jmhProjectName: String
    protected lateinit var jmhProject: Project
    protected lateinit var jmhProjectVirtualFile: VirtualFile

    protected open val skipSetUp = false

    override fun setUp() {
        super.setUp()
        if (skipSetUp) return
        jmhProjectName = nextProjectName()
        jmhProject = createProjectFromTemplate(JMH_GROUP_NAME, JMH_MAVEN_PROJECT_NAME) { step ->
            when (step) {
                is MavenStructureWizardStep -> {
                    step.entityName = jmhProjectName
                    step.artifactId = jmhProjectName
                    step.groupId = JMH_UNIT_TEST_PKG
                }
            }
        }
        assertNotNull(jmhProject)
        jmhProjectVirtualFile = LocalFileSystem.getInstance().findFileByPath(jmhProject.basePath!!)!!
    }

    fun VirtualFile.getText() = VfsUtil.loadText(this)

    companion object {
        const val JMH_UNIT_TEST_PKG = "jmh.unit.test"

        private var counter = 0
        fun nextProjectName() = "unit_test_project${counter++}"
    }
}