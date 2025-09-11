package org.phellang.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * Centralized error handling for Phel completion system
 * Provides graceful fallbacks and meaningful error messages for edge cases
 */
public class PhelCompletionErrorHandler {
    
    private static final Logger LOG = Logger.getInstance(PhelCompletionErrorHandler.class);
    
    /**
     * Execute completion operation with comprehensive error handling
     * @param operation The completion operation to execute
     * @param context Description of what operation is being performed
     * @param fallbackAction Action to take if the operation fails
     * @return true if operation succeeded, false if fallback was used
     */
    public static boolean withErrorHandling(@NotNull CompletionOperation operation, 
                                          @NotNull String context,
                                          @Nullable Runnable fallbackAction) {
        System.out.println("DEBUG ERROR HANDLER: Starting error handling for: " + context);
        try {
            operation.execute();
            System.out.println("DEBUG ERROR HANDLER: Operation completed successfully for: " + context);
            return true;
        } catch (NullPointerException e) {
            System.out.println("DEBUG ERROR HANDLER: CAUGHT NULL POINTER EXCEPTION in: " + context);
            LOG.warn("Null pointer exception in completion: " + context, e);
            addErrorCompletion(operation.getResult(), "⚠️ PSI tree issue", 
                "Completion failed due to null PSI element in " + context);
            if (fallbackAction != null) {
                fallbackAction.run();
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("DEBUG ERROR HANDLER: CAUGHT INDEX OUT OF BOUNDS in: " + context);
            LOG.warn("Index out of bounds in completion: " + context, e);
            addErrorCompletion(operation.getResult(), "⚠️ Malformed syntax", 
                "Completion failed due to unexpected syntax structure");
            if (fallbackAction != null) {
                fallbackAction.run();
            }
            return false;
        } catch (ClassCastException e) {
            System.out.println("DEBUG ERROR HANDLER: CAUGHT CLASS CAST EXCEPTION in: " + context);
            LOG.warn("Type casting error in completion: " + context, e);
            addErrorCompletion(operation.getResult(), "⚠️ Type mismatch", 
                "Completion failed due to unexpected PSI element type");
            if (fallbackAction != null) {
                fallbackAction.run();
            }
            return false;
        } catch (Exception e) {
            System.out.println("DEBUG ERROR HANDLER: CAUGHT GENERIC EXCEPTION in: " + context + " - " + e.getClass().getSimpleName() + ": " + e.getMessage());
            LOG.error("Unexpected error in completion: " + context, e);
            addErrorCompletion(operation.getResult(), "❌ Completion error", 
                "Unexpected error: " + e.getMessage());
            if (fallbackAction != null) {
                fallbackAction.run();
            }
            return false;
        }
    }
    
    /**
     * Safely get PSI element text with null checking
     */
    @NotNull
    public static String safeGetText(@Nullable PsiElement element) {
        try {
            if (element == null) {
                LOG.debug("Attempted to get text from null PSI element");
                return "";
            }
            String text = element.getText();
            return text != null ? text : "";
        } catch (Exception e) {
            LOG.warn("Error getting text from PSI element", e);
            return "";
        }
    }
    
    /**
     * Safely get PSI element children with null checking
     */
    @NotNull
    public static PsiElement[] safeGetChildren(@Nullable PsiElement element) {
        try {
            if (element == null) {
                LOG.debug("Attempted to get children from null PSI element");
                return new PsiElement[0];
            }
            PsiElement[] children = element.getChildren();
            return children != null ? children : new PsiElement[0];
        } catch (Exception e) {
            LOG.warn("Error getting children from PSI element", e);
            return new PsiElement[0];
        }
    }
    
    /**
     * Safely access array element with bounds checking
     */
    @Nullable
    public static <T> T safeArrayAccess(@NotNull T[] array, int index) {
        if (array.length == 0) {
            LOG.debug("Attempted to access element from empty array at index " + index);
            return null;
        }
        if (index < 0 || index >= array.length) {
            LOG.debug("Array index out of bounds: " + index + " (length: " + array.length + ")");
            return null;
        }
        try {
            return array[index];
        } catch (Exception e) {
            LOG.warn("Unexpected error accessing array element at index " + index, e);
            return null;
        }
    }
    
    /**
     * Validate PSI element is not null and has expected type
     */
    public static boolean validatePsiElement(@Nullable PsiElement element, 
                                           @NotNull Class<?> expectedType, 
                                           @NotNull String context) {
        if (element == null) {
            LOG.debug("PSI element validation failed: null element in " + context);
            return false;
        }
        
        if (!expectedType.isAssignableFrom(element.getClass())) {
            LOG.debug("PSI element validation failed: expected " + expectedType.getSimpleName() + 
                     ", got " + element.getClass().getSimpleName() + " in " + context);
            return false;
        }
        
        return true;
    }
    
    /**
     * Add error completion item to inform user of completion issues
     */
    private static void addErrorCompletion(@Nullable CompletionResultSet result, 
                                         @NotNull String name, 
                                         @NotNull String description) {
        if (result == null) return;
        
        try {
            LookupElementBuilder errorElement = LookupElementBuilder.create(name)
                .withTailText(" - " + description, true)
                .withIcon(PhelIconProvider.STRUCTURAL)
                .withInsertHandler((context, item) -> {
                    // Remove the error completion after insertion
                    context.getDocument().deleteString(context.getStartOffset(), context.getTailOffset());
                });
                
            result.addElement(errorElement);
        } catch (Exception e) {
            LOG.error("Failed to add error completion", e);
        }
    }
    
    /**
     * Check if completion context is in a recoverable state
     */
    public static boolean isCompletionContextValid(@Nullable PsiElement element) {
        if (element == null) {
            LOG.debug("Completion context invalid: null element");
            return false;
        }
        
        // Check if we're in a malformed or incomplete PSI tree
        try {
            element.getContainingFile();
            element.getParent();
            return true;
        } catch (Exception e) {
            LOG.debug("Completion context invalid: PSI tree issues", e);
            return false;
        }
    }
    
    /**
     * Functional interface for completion operations that can fail
     */
    @FunctionalInterface
    public interface CompletionOperation {
        void execute() throws Exception;
        
        /**
         * Get the completion result set for error reporting
         * Default implementation returns null - override if error completions needed
         */
        default CompletionResultSet getResult() {
            return null;
        }
    }
    
    /**
     * Create a completion operation with result set access
     */
    public static CompletionOperation withResultSet(@NotNull CompletionResultSet result, 
                                                  @NotNull ThrowingRunnable operation) {
        return new CompletionOperation() {
            @Override
            public void execute() throws Exception {
                operation.run();
            }
            
            @Override
            public CompletionResultSet getResult() {
                return result;
            }
        };
    }
    
    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }
}
