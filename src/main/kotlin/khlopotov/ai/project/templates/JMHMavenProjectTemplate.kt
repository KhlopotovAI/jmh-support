package khlopotov.ai.project.templates

import com.intellij.ide.util.projectWizard.AbstractModuleBuilder
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.platform.ProjectTemplate
import khlopotov.ai.highlighting.benchmarkIcon

class JMHMavenProjectTemplate : ProjectTemplate {
    override fun createModuleBuilder(): AbstractModuleBuilder = JMHModuleBuilder()
    override fun validateSettings(): ValidationInfo? = null

    override fun getIcon() = benchmarkIcon
    override fun getName() = JMH_MAVEN_PROJECT_NAME
    override fun getDescription() = JMH_MAVEN_PROJECT_DESCRIPTION

    companion object {
        const val JMH_MAVEN_PROJECT_NAME = "JMH Maven Project"
        const val JMH_MAVEN_PROJECT_DESCRIPTION = "Generates template JMH project with ready to run and edit simple benchmark"
    }
}
