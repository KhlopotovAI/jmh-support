package khlopotov.ai.highlighting

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import khlopotov.ai.CanShowGuttersActivity.Companion.canShowGutters

class ClassWithBenchmarkLineMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = if (
        element is PsiClass &&
        element.allMethods.any { it.hasAnnotation("org.openjdk.jmh.annotations.Benchmark") } &&
        canShowGutters
    ) {
        BenchmarkLineMarkerInfo(element.nameIdentifier!!)
    } else null
}