package org.pyxy.pyxycharm.lang.builtins

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ResourceUtil
import com.intellij.util.indexing.IndexableSetContributor

class PyxyBuiltins : IndexableSetContributor() {
    companion object {
        val PYXY_BUILTINS by lazy {
            val url = ResourceUtil.getResource(javaClass.classLoader, "builtins", "pyxy_builtins.py")
            VfsUtil.findFileByURL(url)!!
        }
    }

    override fun getAdditionalRootsToIndex(): Set<VirtualFile> {
        return setOfNotNull(PYXY_BUILTINS.parent)
    }
}