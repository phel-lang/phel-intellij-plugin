package org.phellang.integration.refactoring

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.TestActionEvent
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol

/**
 * The refactorings are reachable from the menu, not merely implemented.
 *
 * This exists because Extract Variable shipped invisible: the handler was written and directly
 * tested, but `PhelRefactoringSupportProvider.getIntroduceVariableHandler` was never wired up.
 * `IntroduceVariableAction` asks the provider for a handler and hides itself when none comes back,
 * so the whole feature was absent from Refactor while every test passed.
 *
 * Driving the real actions through `ActionManager` is the only level at which that is visible.
 */
class PhelRefactoringActionsRegisteredTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun presentationOf(actionId: String, source: String, onSymbol: String? = null): Presentation {
        val file = myFixture.configureByText("ra${fileIndex++}.phel", source)

        val builder = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .add(CommonDataKeys.EDITOR, myFixture.editor)
            .add(CommonDataKeys.PSI_FILE, file)

        // Safe Delete acts on an element, not a range: without PSI_ELEMENT the platform falls back to
        // the file, which is why it greys out when a selection is active instead of a caret on a name.
        onSymbol?.let { name ->
            val symbol = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java).first { it.text == name }
            builder.add(CommonDataKeys.PSI_ELEMENT, symbol)
        }

        val action: AnAction = requireNotNull(ActionManager.getInstance().getAction(actionId)) {
            "$actionId is not a registered action"
        }
        val event = TestActionEvent.createTestEvent(action, builder.build() as DataContext)
        action.update(event)

        return event.presentation
    }

    /** The regression: this was invisible, because the provider returned no handler. */
    fun testExtractVariableIsOfferedOnASelectedExpression() {
        val presentation = presentationOf(
            "IntroduceVariable",
            "(ns app\\m)\n(defn f [] (+ 1 <selection>(* 2 3)</selection>))\n",
        )

        assertTrue("Extract Variable should be visible in a Phel file", presentation.isVisible)
        assertTrue("Extract Variable should be enabled on a selected expression", presentation.isEnabled)
    }

    fun testExtractFunctionIsOfferedOnASelectedExpression() {
        val presentation = presentationOf(
            "Phel.ExtractFunction",
            "(ns app\\m)\n(defn f [] (+ 1 <selection>(* 2 3)</selection>))\n",
        )

        assertTrue(presentation.isVisible)
        assertTrue(presentation.isEnabled)
    }

    fun testSafeDeleteIsOfferedOnADefinitionName() {
        val presentation = presentationOf(
            "SafeDelete",
            "(ns app\\m)\n(defn unused [x] x)\n",
            onSymbol = "unused",
        )

        assertTrue("Safe Delete should be visible", presentation.isVisible)
        assertTrue("Safe Delete should be enabled on a top-level definition name", presentation.isEnabled)
    }

    /** Extract Function is Phel's own action and must not appear in other languages. */
    fun testExtractFunctionIsHiddenInAnotherLanguage() {
        myFixture.configureByText("notes.txt", "plain <selection>text</selection>")

        val action = ActionManager.getInstance().getAction("Phel.ExtractFunction")
        val context = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .add(CommonDataKeys.EDITOR, myFixture.editor)
            .add(CommonDataKeys.PSI_FILE, myFixture.file)
            .build()
        val event = TestActionEvent.createTestEvent(action, context)
        action.update(event)

        assertFalse("Extract Phel Function must not appear outside Phel files", event.presentation.isVisible)
    }
}
