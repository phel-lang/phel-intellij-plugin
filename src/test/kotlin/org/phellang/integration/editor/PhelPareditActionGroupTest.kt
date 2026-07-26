package org.phellang.integration.editor

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.testFramework.TestActionEvent
import org.phellang.integration.PhelIntegrationTestCase

/**
 * The Paredit submenu is registered, and appears only in Phel files.
 *
 * The group used to have no `<add-to-group>`, so its nine actions had no menu entry anywhere and
 * were reachable only by chord. Its own visibility check matters because a `popup="true"` group
 * renders its entry whatever its children report, so without one "Phel Paredit" would show up in
 * the right-click menu of every file type in the IDE.
 */
class PhelPareditActionGroupTest : PhelIntegrationTestCase() {

    private fun action(id: String): AnAction =
        requireNotNull(ActionManager.getInstance().getAction(id)) { "$id is not registered" }

    private fun isVisibleIn(fileName: String, source: String): Boolean {
        myFixture.configureByText(fileName, source)

        val context = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .add(CommonDataKeys.EDITOR, myFixture.editor)
            .add(CommonDataKeys.PSI_FILE, myFixture.file)
            .build()

        val group = action(GROUP_ID)
        val event = TestActionEvent.createTestEvent(group, context)
        group.update(event)

        return event.presentation.isVisible
    }

    fun testTheGroupIsShownInAPhelFile() {
        assertTrue("Phel Paredit should be offered in a .phel file", isVisibleIn("a.phel", "(a<caret> b)"))
    }

    fun testTheGroupIsHiddenInAnotherLanguage() {
        assertFalse("Phel Paredit must not appear in every file type", isVisibleIn("a.txt", "plain<caret> text"))
    }

    /** A submenu attached to no menu is what this fixed; the nine actions all hang off this group. */
    fun testTheGroupIsAttachedToTheEditorPopupMenuAndKeepsItsActions() {
        val popup = action(EDITOR_POPUP_ID) as DefaultActionGroup
        val ids = popup.getChildren(null).map { ActionManager.getInstance().getId(it) }

        assertTrue("expected $GROUP_ID in the editor popup, found $ids", ids.contains(GROUP_ID))

        val pareditActions = (action(GROUP_ID) as DefaultActionGroup).getChildren(null)
        assertEquals("all nine paredit actions should hang off the group", 9, pareditActions.size)
    }

    private companion object {
        const val GROUP_ID = "Phel.Paredit"
        const val EDITOR_POPUP_ID = "EditorPopupMenu"
    }
}
