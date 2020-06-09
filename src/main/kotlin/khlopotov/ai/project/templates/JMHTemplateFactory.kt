package khlopotov.ai.project.templates

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.platform.ProjectTemplatesFactory
import khlopotov.ai.highlighting.benchmarkIcon

class JMHTemplateFactory : ProjectTemplatesFactory() {
    override fun getGroups() = arrayOf(JMH_GROUP_NAME)

    override fun getGroupIcon(group: String) = benchmarkIcon
    override fun getParentGroup(group: String?): String = JMH_PARENT_GROUP_NAME
    override fun getGroupWeight(group: String?): Int = 1
    override fun createTemplates(group: String?, context: WizardContext?) = arrayOf(JMHMavenProjectTemplate())

    companion object {
        const val JMH_GROUP_NAME: String = "JMH"
        const val JMH_PARENT_GROUP_NAME = "JMH Group"
    }
}