package org.pyxy.pyxycharm

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.jetbrains.python.psi.resolve.ResolveImportUtil
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy

class PyxyUnloadListener : DynamicPluginListener {
    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        val reloadingStrategy = ClassReloadingStrategy.fromInstalledAgent()
        reloadingStrategy.reset(ResolveImportUtil::class.java)
    }
}