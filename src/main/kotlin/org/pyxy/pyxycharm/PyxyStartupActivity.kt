package org.pyxy.pyxycharm

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.python.psi.resolve.ResolveImportUtil
import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.matcher.ElementMatchers

class PyxyStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        ByteBuddyAgent.install()
        ByteBuddy()
            .redefine(ResolveImportUtil::class.java)
            .visit(Advice.to(FileFindInterceptor::class.java).on(ElementMatchers.named("findPyFileInDir")))
            .make()
            .load(ResolveImportUtil::class.java.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
    }
}
