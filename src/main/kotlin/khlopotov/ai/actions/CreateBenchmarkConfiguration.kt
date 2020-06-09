package khlopotov.ai.actions

import com.intellij.execution.RunManager
import com.intellij.execution.jar.JarApplicationConfiguration
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiIdentifier
import khlopotov.ai.highlighting.benchmarkIcon
import khlopotov.ai.run.configuration.JmhConfigurationType
import org.jetbrains.idea.maven.project.MavenProjectsManager

open class CreateBenchmarkConfiguration(
    protected val psiElement: PsiIdentifier,
    text: String = "Create ${psiElement.text} configuration"
) : AnAction(text, null, benchmarkIcon) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = psiElement.project
        val runManager = RunManager.getInstance(project)

        var configuration = runManager.findConfigurationByName(psiElement.text)
        if (configuration == null) {
            configuration = runManager.createConfiguration(psiElement.text, JmhConfigurationType::class.java)
        }

        with(configuration.configuration as JarApplicationConfiguration) {
            programParameters = psiElement.text
            jarPath = "${project.basePath}/target/${jarName(project)}"
        }

        runManager.addConfiguration(configuration)
        runManager.selectedConfiguration = configuration
    }

    private fun jarName(project: Project): String {
        val mavenProject = MavenProjectsManager.getInstance(project).projects.first()
        val finalName = mavenProject
            .findPlugin("org.apache.maven.plugins", "maven-shade-plugin")
            ?.getGoalConfiguration("shade")
            ?.getChildTextNormalize("finalName")

        return (finalName ?: "${mavenProject.mavenId.artifactId}-${mavenProject.mavenId.version}") + ".jar"
    }
}