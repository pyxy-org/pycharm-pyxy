package org.pyxy.pyxycharm

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.AsyncFileListener.ChangeApplier
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.psi.PsiManager
import com.intellij.util.concurrency.AppExecutorUtil


class PyxyFileListener : AsyncFileListener {
    private val executorService = AppExecutorUtil.getAppExecutorService()

    override fun prepareChange(events: List<VFileEvent>): ChangeApplier {
        return object : ChangeApplier {
            override fun afterVfsChange() {
                executorService.submit {
                    ApplicationManager.getApplication().runReadAction {
                        for (event in events) {
                            val file = event.file ?: continue
                            val project = ProjectLocator.getInstance().guessProjectForFile(file) ?: continue
                            val psiFile = PsiManager.getInstance(project).findFile(file) ?: continue
                            if (!psiFile.language.isKindOf(PyxyLanguageDialect)) continue
                            println("${project.name} ${file.path}")
                        }
                    }
                }
            }
        }
    }
}
