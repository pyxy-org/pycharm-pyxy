package org.pyxy.pyxycharm.builtins

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ResourceUtil
import com.intellij.util.indexing.IndexableSetContributor

class PyxyBuiltins : IndexableSetContributor() {
    override fun getAdditionalRootsToIndex(): Set<VirtualFile> {
        return setOfNotNull(PYXY_BUILTINS.parent)
    }
}

val PYXY_BUILTINS by lazy {
    val url = ResourceUtil.getResource(PyxyBuiltins::class.java.classLoader, "builtins", "pyxy_builtins.py")
    VfsUtil.findFileByURL(url)!!
}