package org.phellang.editor.paredit

import com.intellij.openapi.editor.actionSystem.EditorAction

class PhelSlurpForwardAction : EditorAction(PhelParediActionHandler(PhelParedit::slurpForward))

class PhelBarfForwardAction : EditorAction(PhelParediActionHandler(PhelParedit::barfForward))

class PhelSlurpBackwardAction : EditorAction(PhelParediActionHandler(PhelParedit::slurpBackward))

class PhelBarfBackwardAction : EditorAction(PhelParediActionHandler(PhelParedit::barfBackward))

class PhelWrapParenAction : EditorAction(PhelParediActionHandler({ file, offset ->
    PhelParedit.wrap(file, offset, '(', ')')
}))

class PhelWrapBracketAction : EditorAction(PhelParediActionHandler({ file, offset ->
    PhelParedit.wrap(file, offset, '[', ']')
}))

class PhelWrapBraceAction : EditorAction(PhelParediActionHandler({ file, offset ->
    PhelParedit.wrap(file, offset, '{', '}')
}))

class PhelSpliceAction : EditorAction(PhelParediActionHandler(PhelParedit::splice))

class PhelRaiseAction : EditorAction(PhelParediActionHandler(PhelParedit::raise))
