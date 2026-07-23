package org.phellang.editor.paredit

import com.intellij.openapi.editor.actionSystem.EditorAction

class PhelSlurpForwardAction : EditorAction(PhelPareditActionHandler(PhelParedit::slurpForward))

class PhelBarfForwardAction : EditorAction(PhelPareditActionHandler(PhelParedit::barfForward))

class PhelSlurpBackwardAction : EditorAction(PhelPareditActionHandler(PhelParedit::slurpBackward))

class PhelBarfBackwardAction : EditorAction(PhelPareditActionHandler(PhelParedit::barfBackward))

class PhelWrapParenAction : EditorAction(PhelPareditActionHandler({ file, offset ->
    PhelParedit.wrap(file, offset, '(', ')')
}))

class PhelWrapBracketAction : EditorAction(PhelPareditActionHandler({ file, offset ->
    PhelParedit.wrap(file, offset, '[', ']')
}))

class PhelWrapBraceAction : EditorAction(PhelPareditActionHandler({ file, offset ->
    PhelParedit.wrap(file, offset, '{', '}')
}))

class PhelSpliceAction : EditorAction(PhelPareditActionHandler(PhelParedit::splice))

class PhelRaiseAction : EditorAction(PhelPareditActionHandler(PhelParedit::raise))
