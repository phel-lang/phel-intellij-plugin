package org.phellang.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.phellang.PhelFileType;
import org.phellang.language.psi.*;
import org.phellang.language.psi.impl.PhelAccessImpl;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Local symbol completions for Phel - provides completions for parameters,
 * let bindings, and other local variables in the current scope
 */
public class PhelLocalSymbolCompletions {

    private static final Icon PARAMETER_ICON = AllIcons.Nodes.Parameter;
    private static final Icon LOCAL_VARIABLE_ICON = AllIcons.Nodes.Variable;
    private static final Icon DEFINITION_ICON = AllIcons.Nodes.Method;

    /**
     * Add local symbol completions based on current position
     * Enhanced with performance optimizations
     */
    public static void addLocalSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position) {
        System.out.println("DEBUG: addLocalSymbols called for position: " + position.getText());
        
        // Performance check - skip expensive operations if needed
        if (PhelCompletionPerformanceOptimizer.shouldSkipExpensiveOperations(position)) {
            System.out.println("DEBUG: Skipping expensive operations");
            return;
        }
        
        Set<String> addedSymbols = new HashSet<>();
        
        // Use optimized binding collection
        Map<String, String> bindings = new HashMap<>();
        PhelCompletionPerformanceOptimizer.collectBindingsEfficiently(position, bindings);
        
        System.out.println("DEBUG: Found " + bindings.size() + " bindings from optimizer");
        
        // Add optimized bindings to completion results
        for (Map.Entry<String, String> entry : bindings.entrySet()) {
            String symbolName = entry.getKey();
            String bindingType = entry.getValue();
            Icon icon = getIconForBindingType(bindingType);
            addSymbolCompletion(result, symbolName, bindingType, icon, addedSymbols);
        }
        
        System.out.println("DEBUG: About to call addLocalDefinitionSymbolsOptimized");
        // Add local definitions with performance limits
        addLocalDefinitionSymbolsOptimized(result, position, addedSymbols);
        
        System.out.println("DEBUG: About to call SIMPLE version");
        // ALSO try a completely simple version without optimizations
        addLocalDefinitionSymbolsSimple(result, position, addedSymbols);
        
        System.out.println("DEBUG: About to call addProjectGlobalDefinitions");
        // Add global definitions from other files in project (with limits for performance)
        addProjectGlobalDefinitions(result, position, addedSymbols);
    }

    /**
     * Add function/macro parameter symbols in current scope
     */
    private static void addParameterSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position, @NotNull Set<String> addedSymbols) {
        // Find containing function or macro definition
        PsiElement current = position;
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement firstElement = list.getFirstChild();
                if (firstElement instanceof PhelSymbol) {
                    String symbolName = firstElement.getText();
                    if (symbolName.equals("defn") || symbolName.equals("defn-") || 
                        symbolName.equals("defmacro") || symbolName.equals("defmacro-") ||
                        symbolName.equals("fn")) {
                        
                        // Find parameter vector
                        PhelVec paramVector = PsiTreeUtil.findChildOfType(list, PhelVec.class);
                        if (paramVector != null) {
                            addSymbolsFromVector(result, paramVector, "Function Parameter", PARAMETER_ICON, addedSymbols);
                        }
                        break; // Don't look beyond current function
                    }
                }
            }
            current = current.getParent();
        }
    }

    /**
     * Add let binding symbols in current scope
     */
    private static void addLetBindingSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position, @NotNull Set<String> addedSymbols) {
        // Find all let forms that contain this position
        PsiElement current = position;
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement firstElement = list.getFirstChild();
                if (firstElement instanceof PhelSymbol) {
                    String symbolName = firstElement.getText();
                    if (symbolName.equals("let") || symbolName.equals("binding")) {
                        // Find binding vector (second element)
                        PhelVec bindingVector = findSecondElementOfType(list, PhelVec.class);
                        if (bindingVector != null) {
                            addBindingSymbolsFromVector(result, bindingVector, "Let Binding", LOCAL_VARIABLE_ICON, addedSymbols);
                        }
                    }
                }
            }
            current = current.getParent();
        }
    }

    /**
     * Add loop binding symbols in current scope
     */
    private static void addLoopBindingSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position, @NotNull Set<String> addedSymbols) {
        PsiElement current = position;
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement firstElement = list.getFirstChild();
                if (firstElement instanceof PhelSymbol && firstElement.getText().equals("loop")) {
                    PhelVec bindingVector = findSecondElementOfType(list, PhelVec.class);
                    if (bindingVector != null) {
                        addBindingSymbolsFromVector(result, bindingVector, "Loop Binding", LOCAL_VARIABLE_ICON, addedSymbols);
                    }
                    break; // Don't look beyond current loop
                }
            }
            current = current.getParent();
        }
    }

    /**
     * Add for binding symbols in current scope
     */
    private static void addForBindingSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position, @NotNull Set<String> addedSymbols) {
        PsiElement current = position;
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement firstElement = list.getFirstChild();
                if (firstElement instanceof PhelSymbol) {
                    String symbolName = firstElement.getText();
                    if (symbolName.equals("for") || symbolName.equals("dofor")) {
                        PhelVec bindingVector = findSecondElementOfType(list, PhelVec.class);
                        if (bindingVector != null) {
                            addBindingSymbolsFromVector(result, bindingVector, "For Binding", LOCAL_VARIABLE_ICON, addedSymbols);
                        }
                    } else if (symbolName.equals("foreach")) {
                        PhelVec bindingVector = findSecondElementOfType(list, PhelVec.class);
                        if (bindingVector != null) {
                            addSymbolsFromVector(result, bindingVector, "Foreach Binding", LOCAL_VARIABLE_ICON, addedSymbols);
                        }
                    }
                }
            }
            current = current.getParent();
        }
    }

    /**
     * Add catch binding symbols in current scope
     */
    private static void addCatchBindingSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position, @NotNull Set<String> addedSymbols) {
        PsiElement current = position;
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement firstElement = list.getFirstChild();
                if (firstElement instanceof PhelSymbol && firstElement.getText().equals("catch")) {
                    // In catch, third element is the binding symbol
                    PsiElement[] children = list.getChildren();
                    if (children.length >= 3 && children[2] instanceof PhelSymbol) {
                        String symbolName = children[2].getText();
                        addSymbolCompletion(result, symbolName, "Catch Binding", LOCAL_VARIABLE_ICON, addedSymbols);
                    }
                }
            }
            current = current.getParent();
        }
    }

    /**
     * Add if-let binding symbols in current scope
     */
    private static void addIfLetBindingSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position, @NotNull Set<String> addedSymbols) {
        PsiElement current = position;
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement firstElement = list.getFirstChild();
                if (firstElement instanceof PhelSymbol && firstElement.getText().equals("if-let")) {
                    PhelVec bindingVector = findSecondElementOfType(list, PhelVec.class);
                    if (bindingVector != null) {
                        // if-let has [binding test] format - get first element
                        PsiElement[] children = bindingVector.getChildren();
                        if (children.length >= 1 && children[0] instanceof PhelSymbol) {
                            String symbolName = children[0].getText();
                            addSymbolCompletion(result, symbolName, "If-Let Binding", LOCAL_VARIABLE_ICON, addedSymbols);
                        }
                    }
                }
            }
            current = current.getParent();
        }
    }

    /**
     * Add local definition symbols (defn, def, etc.) from current file
     */
    private static void addLocalDefinitionSymbols(@NotNull CompletionResultSet result, @NotNull PsiElement position, @NotNull Set<String> addedSymbols) {
        PhelFile file = (PhelFile) position.getContainingFile();
        if (file != null) {
            // Find all top-level definitions in current file
            for (PsiElement child : file.getChildren()) {
                if (child instanceof PhelList) {
                    PhelList list = (PhelList) child;
                    PsiElement firstElement = list.getFirstChild();
                    if (firstElement instanceof PhelSymbol) {
                        String defType = firstElement.getText();
                        if (defType.equals("defn") || defType.equals("defn-") || 
                            defType.equals("def") || defType.equals("defmacro") || 
                            defType.equals("defmacro-") || defType.equals("defstruct")) {
                            
                            // Get function/var name (second element)
                            PsiElement nameElement = findSecondElementOfType(list, PhelSymbol.class);
                            if (nameElement instanceof PhelSymbol) {
                                String symbolName = nameElement.getText();
                                addSymbolCompletion(result, symbolName, "Local Definition", DEFINITION_ICON, addedSymbols);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Extract symbols from a vector (for parameters)
     */
    private static void addSymbolsFromVector(@NotNull CompletionResultSet result, @NotNull PhelVec vector, 
                                           String typeText, Icon icon, @NotNull Set<String> addedSymbols) {
        for (PsiElement child : vector.getChildren()) {
            if (child instanceof PhelSymbol) {
                String symbolName = child.getText();
                addSymbolCompletion(result, symbolName, typeText, icon, addedSymbols);
            }
        }
    }

    /**
     * Extract binding symbols from a vector (for let/loop bindings - every other element)
     */
    private static void addBindingSymbolsFromVector(@NotNull CompletionResultSet result, @NotNull PhelVec vector, 
                                                  String typeText, Icon icon, @NotNull Set<String> addedSymbols) {
        PsiElement[] children = vector.getChildren();
        // In binding vectors, symbols are at even indices: [sym1 val1 sym2 val2 ...]
        for (int i = 0; i < children.length; i += 2) {
            if (children[i] instanceof PhelSymbol) {
                String symbolName = children[i].getText();
                addSymbolCompletion(result, symbolName, typeText, icon, addedSymbols);
            }
        }
    }

    /**
     * Add a symbol completion if not already added
     */
    private static void addSymbolCompletion(@NotNull CompletionResultSet result, String symbolName, 
                                          String typeText, Icon icon, @NotNull Set<String> addedSymbols) {
        if (!addedSymbols.contains(symbolName) && !symbolName.trim().isEmpty()) {
            addedSymbols.add(symbolName);
            result.addElement(
                LookupElementBuilder.create(symbolName)
                    .withIcon(icon)
                    .withTypeText(typeText)
                    .withBoldness(true) // Local symbols are bold for visibility
            );
        }
    }

    /**
     * Add global definitions from other Phel files in the project
     */
    private static void addProjectGlobalDefinitions(@NotNull CompletionResultSet result,
                                                   @NotNull PsiElement position,
                                                   @NotNull Set<String> addedSymbols) {
        Project project = position.getProject();
        if (project == null) return;
        
        // Skip in large projects for performance
        if (PhelCompletionPerformanceOptimizer.shouldSkipExpensiveOperations(position)) {
            return;
        }
        
        try {
            // Find all Phel files in the project
            Collection<VirtualFile> phelFiles = FileBasedIndex.getInstance()
                .getContainingFiles(FileTypeIndex.NAME, PhelFileType.INSTANCE, 
                                   GlobalSearchScope.projectScope(project));
            
            PsiManager psiManager = PsiManager.getInstance(project);
            PsiFile currentFile = position.getContainingFile();
            
            int fileCount = 0;
            final int MAX_FILES_TO_SCAN = 20; // Performance limit
            
            for (VirtualFile virtualFile : phelFiles) {
                if (fileCount >= MAX_FILES_TO_SCAN) break;
                
                // Skip current file - already processed
                if (virtualFile.equals(currentFile.getVirtualFile())) continue;
                
                PsiFile psiFile = psiManager.findFile(virtualFile);
                if (psiFile instanceof PhelFile) {
                    PhelFile phelFile = (PhelFile) psiFile;
                    extractGlobalDefinitionsFromFile(phelFile, result, addedSymbols);
                    fileCount++;
                }
            }
        } catch (Exception e) {
            // Silently ignore errors to avoid disrupting completion
        }
    }

    /**
     * Extract global definitions from a specific Phel file
     */
    private static void extractGlobalDefinitionsFromFile(@NotNull PhelFile file,
                                                        @NotNull CompletionResultSet result,
                                                        @NotNull Set<String> addedSymbols) {
        int definitionCount = 0;
        final int MAX_DEFINITIONS_PER_FILE = 10;
        
        for (PsiElement child : file.getChildren()) {
            if (definitionCount >= MAX_DEFINITIONS_PER_FILE) break;
            
            if (child instanceof PhelList) {
                PhelList list = (PhelList) child;
                PsiElement[] children = list.getChildren();
                
                if (children.length >= 2) {
                    PsiElement firstElement = children[0];
                    
                    if (firstElement instanceof PhelSymbol) {
                        String defType = firstElement.getText();
                        
                        // Only include publicly accessible definitions
                        if (defType.equals("defn") || defType.equals("def") || 
                            defType.equals("defmacro") || defType.equals("defstruct")) {
                            
                            PsiElement nameElement = children[1];
                            if (nameElement instanceof PhelSymbol) {
                                String symbolName = nameElement.getText();
                                String displayType = defType.equals("def") ? "Global Variable" : 
                                                   defType.equals("defn") ? "Public Function" :
                                                   defType.equals("defmacro") ? "Public Macro" : 
                                                   "Public Definition";
                                
                                // Add file context
                                String fileName = file.getName().replace(".phel", "");
                                String typeWithContext = displayType + " (" + fileName + ")";
                                
                                addSymbolCompletion(result, symbolName, typeWithContext, DEFINITION_ICON, addedSymbols);
                                definitionCount++;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Simple version without any optimizations - for debugging
     */
    private static void addLocalDefinitionSymbolsSimple(@NotNull CompletionResultSet result, 
                                                      @NotNull PsiElement position, 
                                                      @NotNull Set<String> addedSymbols) {
        System.out.println("DEBUG SIMPLE: Starting simple version");
        
        PhelFile file = (PhelFile) position.getContainingFile();
        if (file == null) {
            System.out.println("DEBUG SIMPLE: File is null");
            return;
        }
        
        System.out.println("DEBUG SIMPLE: File: " + file.getName());
        
        // Just iterate through all children without any limits
        for (PsiElement child : file.getChildren()) {
            System.out.println("DEBUG SIMPLE: Child: " + child.getClass().getSimpleName() + " - " + child.getText().substring(0, Math.min(20, child.getText().length())).trim());
            
            if (child instanceof PhelList) {
                PhelList list = (PhelList) child;
                
                // Get children array and look for def + variable pattern  
                PsiElement[] children = list.getChildren();
                System.out.println("DEBUG SIMPLE: Children array length: " + children.length);
                for (int i = 0; i < children.length; i++) {
                    System.out.println("DEBUG SIMPLE: Child[" + i + "]: " + children[i].getClass().getSimpleName() + " - '" + children[i].getText() + "'");
                }
                
                // Look for (def symbol-name ...) pattern 
                for (int i = 0; i < children.length - 1; i++) {
                    PsiElement defChild = children[i];
                    if ((defChild instanceof PhelSymbol || defChild instanceof PhelAccessImpl) 
                        && "def".equals(defChild.getText())) {
                        System.out.println("DEBUG SIMPLE: Found 'def' at index " + i);
                        
                        // Check next element for the variable name
                        if (i + 1 < children.length) {
                            PsiElement nameChild = children[i + 1];
                            System.out.println("DEBUG SIMPLE: Name child: " + nameChild.getClass().getSimpleName() + " - '" + nameChild.getText() + "'");
                            
                            if (nameChild instanceof PhelSymbol || nameChild instanceof PhelAccessImpl) {
                                String symbolName = nameChild.getText();
                                System.out.println("DEBUG SIMPLE: ADDING SYMBOL: " + symbolName);
                                addSymbolCompletion(result, symbolName, "Simple Global Variable", DEFINITION_ICON, addedSymbols);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get icon for binding type
     */
    private static Icon getIconForBindingType(String bindingType) {
        if (bindingType.contains("Parameter")) {
            return PARAMETER_ICON;
        } else if (bindingType.contains("Definition")) {
            return DEFINITION_ICON;
        } else {
            return LOCAL_VARIABLE_ICON;
        }
    }
    
    /**
     * Get the name of the function currently being defined at the given position
     * This is used to mark recursive function calls with a special indicator
     */
    private static String getCurrentFunctionName(@NotNull PsiElement position) {
        PsiElement current = position;
        
        // Walk up the PSI tree to find the containing function definition
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement[] children = list.getChildren();
                
                if (children.length >= 2) {
                    PsiElement firstElement = children[0];
                    
                    // Check if this is a function definition
                    if (firstElement instanceof PhelSymbol || firstElement instanceof PhelAccessImpl) {
                        String defType = firstElement.getText();
                        if (defType.equals("defn") || defType.equals("defn-") || 
                            defType.equals("defmacro") || defType.equals("defmacro-")) {
                            
                            // Get the function name (second element)
                            PsiElement nameElement = children[1];
                            if (nameElement instanceof PhelSymbol || nameElement instanceof PhelAccessImpl) {
                                return nameElement.getText();
                            }
                        }
                    }
                }
            }
            current = current.getParent();
        }
        
        return null; // Not inside a function definition
    }

    /**
     * Optimized local definition symbols - limits file scanning
     */
    private static void addLocalDefinitionSymbolsOptimized(@NotNull CompletionResultSet result, 
                                                         @NotNull PsiElement position, 
                                                         @NotNull Set<String> addedSymbols) {
        PhelFile file = (PhelFile) position.getContainingFile();
        if (file != null) {
            // DEBUG: Print debug info
            System.out.println("DEBUG: Processing file for local definitions: " + file.getName());
            
            // Limit the number of top-level definitions we process
            int definitionCount = 0;
            final int MAX_DEFINITIONS = 50;
            
            for (PsiElement child : file.getChildren()) {
                if (definitionCount >= MAX_DEFINITIONS) break;
                
                System.out.println("DEBUG: Found child: " + child.getClass().getSimpleName() + " - " + child.getText().substring(0, Math.min(50, child.getText().length())));
                
                if (child instanceof PhelList) {
                    PhelList list = (PhelList) child;
                    PsiElement[] children = list.getChildren();
                    
                    System.out.println("DEBUG: PhelList has " + children.length + " children");
                    
                    // Check if we have at least 3 elements (opening paren, def-type, symbol-name)
                    if (children.length >= 3) {
                        // Debug: show all children
                        System.out.println("DEBUG: All children:");
                        for (int i = 0; i < children.length; i++) {
                            System.out.println("DEBUG:   Child[" + i + "]: " + children[i].getClass().getSimpleName() + " - '" + children[i].getText() + "'");
                        }
                        
                        // Look for definition type starting from first child
                        PsiElement defTypeElement = null;
                        PsiElement nameElement = null;
                        
                        // Find the definition type element (def, defn, etc.)
                        for (int i = 0; i < children.length - 1; i++) {
                            PsiElement childElement = children[i];
                            if (childElement instanceof PhelSymbol || childElement instanceof PhelAccessImpl) {
                                String text = childElement.getText();
                                System.out.println("DEBUG: Checking element[" + i + "]: " + childElement.getClass().getSimpleName() + " - '" + text + "'");
                                
                                if (text.equals("defn") || text.equals("defn-") || 
                                    text.equals("def") || text.equals("defmacro") || 
                                    text.equals("defmacro-") || text.equals("defstruct")) {
                                    
                                    defTypeElement = childElement;
                                    String defType = text;
                                    System.out.println("DEBUG: Found DefType: '" + defType + "' at index " + i);
                                    
                                    // Name should be the next element
                                    if (i + 1 < children.length) {
                                        nameElement = children[i + 1];
                                        System.out.println("DEBUG: Name element: " + nameElement.getClass().getSimpleName() + " - '" + nameElement.getText() + "'");
                                        
                                        if (nameElement instanceof PhelSymbol || nameElement instanceof PhelAccessImpl) {
                                            String symbolName = nameElement.getText();
                                            String displayType = defType.equals("def") ? "Global Variable" : 
                                                               defType.equals("defn") || defType.equals("defn-") ? "Function" :
                                                               defType.equals("defmacro") || defType.equals("defmacro-") ? "Macro" : 
                                                               "Definition";
                                            
                                            // Check if this is the current function being defined (for recursive calls)
                                            String currentFunctionName = getCurrentFunctionName(position);
                                            if (currentFunctionName != null && currentFunctionName.equals(symbolName)) {
                                                displayType += " (recursive)";
                                            }
                                            
                                            System.out.println("DEBUG: ADDING completion: " + symbolName + " (" + displayType + ")");
                                            addSymbolCompletion(result, symbolName, displayType, DEFINITION_ICON, addedSymbols);
                                            definitionCount++;
                                            break; // Found what we need
                                        } else {
                                            System.out.println("DEBUG: Name element is not a symbol: " + nameElement.getClass().getSimpleName());
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("DEBUG: Not enough children in PhelList: " + children.length);
                    }
                }
            }
            
            System.out.println("DEBUG: Total definitions processed: " + definitionCount);
        } else {
            System.out.println("DEBUG: File is null!");
        }
    }

    /**
     * Find the second element of a specific type in a list
     */
    @SuppressWarnings("unchecked")
    private static <T extends PsiElement> T findSecondElementOfType(@NotNull PhelList list, Class<T> clazz) {
        PsiElement[] children = list.getChildren();
        if (children.length >= 2 && clazz.isInstance(children[1])) {
            return (T) children[1];
        }
        return null;
    }
}
