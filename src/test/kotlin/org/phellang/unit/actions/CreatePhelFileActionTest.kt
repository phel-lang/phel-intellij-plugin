package org.phellang.unit.actions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.actions.CreatePhelFileAction

class CreatePhelFileActionTest {

    private lateinit var action: CreatePhelFileAction

    @BeforeEach
    fun setUp() {
        action = CreatePhelFileAction()
    }

    @Test
    fun `action should be properly initialized`() {
        assertNotNull(action)
    }

    @Test
    fun `action should have correct inheritance hierarchy`() {
        val superClass = action.javaClass.superclass
        assertEquals("CreateFileFromTemplateAction", superClass.simpleName)

        val interfaces = action.javaClass.interfaces
        val dumbAwareInterface = interfaces.find { it.simpleName == "DumbAware" }
        assertNotNull(dumbAwareInterface, "Action should implement DumbAware interface")
    }
}
