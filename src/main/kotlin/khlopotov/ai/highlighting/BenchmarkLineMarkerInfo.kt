package khlopotov.ai.highlighting

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import khlopotov.ai.actions.CreateBenchmarkConfiguration
import khlopotov.ai.actions.RunAndCreateBenchmarkAction

class BenchmarkLineMarkerInfo(private val psiElement: PsiIdentifier) : LineMarkerInfo<PsiElement>(
    psiElement,
    psiElement.textRange,
    benchmarkIcon,
    { element -> "Run ${element.text} benchmark" },
    null,
    GutterIconRenderer.Alignment.CENTER
) {
    override fun createGutterRenderer(): GutterIconRenderer? {
        return object : LineMarkerGutterIconRenderer<PsiElement>(this) {
            override fun getPopupMenuActions(): ActionGroup? {
                return DefaultActionGroup(
                    RunAndCreateBenchmarkAction(psiElement),
                    CreateBenchmarkConfiguration(psiElement)
                )
            }

            override fun getClickAction(): AnAction? = null
            override fun isNavigateAction(): Boolean = true
        }
    }
}