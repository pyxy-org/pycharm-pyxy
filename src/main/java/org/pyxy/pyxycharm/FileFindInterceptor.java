package org.pyxy.pyxycharm;

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import net.bytebuddy.asm.Advice;

public class FileFindInterceptor {
    @Advice.OnMethodExit
    public static void onExit(
            @Advice.Return(readOnly = false) PsiFile file,
            @Advice.Argument(0) PsiDirectory dir,
            @Advice.Argument(1) String referencedName,
            @Advice.Argument(2) boolean withoutStubs
    ) {
        PsiFile pyxyFile = dir.findFile(referencedName + ".pyxy");
        if (file != null) {
            if (pyxyFile != null) {
                file = pyxyFile;
            }
        } else {
            file = pyxyFile;
        }
    }
}
