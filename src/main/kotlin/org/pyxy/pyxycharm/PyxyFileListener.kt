package org.pyxy.pyxycharm

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil.execAndGetOutput
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.AsyncFileListener.ChangeApplier
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.util.concurrency.AppExecutorUtil
import com.jetbrains.python.sdk.PythonSdkUtil


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
                            compileWithPyxy(psiFile)
//                            println("Compiled ${project.name} ${file.path}")
                        }
                    }
                }
            }
        }
    }

    fun compileWithPyxy(psiFile: PsiFile) {
        val pythonSdk = PythonSdkUtil.findPythonSdk(psiFile) ?: return
        val commandLine = GeneralCommandLine()
            .withExePath(pythonSdk.homePath!!)
            .withParameters("-m", "pyxy", psiFile.virtualFile.path)
            .withWorkDirectory(psiFile.project.basePath)
        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                val output = execAndGetOutput(commandLine)
//                Logger.getInstance(PyxyFileListener::class.java).warn("stdout: " + output.stdout)
//                Logger.getInstance(PyxyFileListener::class.java).warn("stderr: " + output.stderr)
//                Logger.getInstance(PyxyFileListener::class.java).warn("exitcode: " + output.exitCode)
                VfsUtil.markDirtyAndRefresh(true, false, true, psiFile.virtualFile.parent);
            } catch (e: Exception) {
                Logger.getInstance(PyxyFileListener::class.java).error("Error running module foo", e);
            }
        }
    }
}
