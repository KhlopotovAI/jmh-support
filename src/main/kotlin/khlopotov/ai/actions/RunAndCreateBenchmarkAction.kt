package khlopotov.ai.actions

import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiIdentifier
import org.jetbrains.idea.maven.execution.MavenRunner
import org.jetbrains.idea.maven.execution.MavenRunnerParameters
import org.jetbrains.idea.maven.execution.MavenRunnerSettings
import org.jetbrains.idea.maven.project.MavenProjectsManager

class RunAndCreateBenchmarkAction(
    psiElement: PsiIdentifier,
    text: String = "Run ${psiElement.text} benchmark"
) : CreateBenchmarkConfiguration(psiElement, text) {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        val project = psiElement.project
        val mavenRunner = MavenRunner.getInstance(project)
        val mavenParameters = mavenParameters(project)
        val mavenSettings = mavenSettings(project)

        mavenRunner.run(mavenParameters, mavenSettings) {
            val runManager = RunManager.getInstance(project)
            val executor = ExecutorRegistry.getInstance().getExecutorById(DefaultRunExecutor.EXECUTOR_ID)!!
            val env = ExecutionEnvironmentBuilder.create(executor, runManager.selectedConfiguration!!).build()
            ExecutionManager.getInstance(project).restartRunProfile(env)
        }
    }

    private fun mavenSettings(project: Project): MavenRunnerSettings {
        return MavenRunner.getInstance(project).settings.clone().apply { setVmOptions("-DskipTests") }
    }

    private fun mavenParameters(project: Project): MavenRunnerParameters {
        val projectsManager = MavenProjectsManager.getInstance(project)
        val mavenProject = projectsManager.projects.first()
        val workingDirPath = mavenProject.directory
        val pomFileName = mavenProject.file.name
        val explicitProfiles = projectsManager.explicitProfiles
        val enabledProfiles = explicitProfiles.enabledProfiles
        val disabledProfiles = explicitProfiles.disabledProfiles
        val goals = listOf("package")
        return MavenRunnerParameters(true, workingDirPath, pomFileName, goals, enabledProfiles, disabledProfiles)
    }
}
