package org.pyxy.pyxycharm

import com.intellij.codeInspection.ex.InspectionProfileImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.profile.codeInspection.InspectionProfileManager


class MyPostStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val profile = InspectionProfileManager.getInstance().currentProfile
        val inspectionName = "PyRedundantParenthesesInspection"
        println("Disabling PyRedundantParenthesesInspection for ${profile.name}")
        InspectionProfileImpl.setToolEnabled(false, profile, inspectionName, project)
    }
}