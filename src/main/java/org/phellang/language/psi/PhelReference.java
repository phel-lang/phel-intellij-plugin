package org.phellang.language.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reference implementation for Phel symbols that supports resolving to multiple targets.
 * This handles cases where a symbol might refer to multiple definitions, such as:
 * - Function overloads
 * - Macro vs function with same name  
 * - Forward declarations + definitions
 * - Redefinitions
 */
public class PhelReference extends PsiReferenceBase<PhelSymbol> implements PsiPolyVariantReference {
    
    private final String symbolName;
    private final String qualifier;
    private final boolean findUsages;  // If true, find usages; if false, find definitions
    
    public PhelReference(@NotNull PhelSymbol element) {
        this(element, PhelPsiUtil.isDefinition(element));
    }
    
    public PhelReference(@NotNull PhelSymbol element, boolean findUsages) {
        super(element, calculateRangeInElement(element));
        this.symbolName = PhelPsiUtil.getName(element);
        this.qualifier = PhelPsiUtil.getQualifier(element);
        this.findUsages = findUsages;
    }
    
    /**
     * Calculate the text range within the symbol that represents the reference.
     * For qualified symbols like "ns/symbol", we only reference the symbol part.
     */
    private static TextRange calculateRangeInElement(PhelSymbol element) {
        String text = element.getText();
        if (text == null) return TextRange.EMPTY_RANGE;
        
        // For qualified symbols, reference only the name part after '/'
        int slashIndex = text.lastIndexOf('/');
        if (slashIndex >= 0 && slashIndex < text.length() - 1) {
            return TextRange.from(slashIndex + 1, text.length() - slashIndex - 1);
        }
        
        // For unqualified symbols, reference the entire text
        return TextRange.from(0, text.length());
    }
    
    /**
     * Resolve to multiple targets. This is the key method for polyvariant references.
     * Returns all possible targets that this symbol could refer to.
     */
    @Override
    @NotNull
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        if (symbolName == null || symbolName.isEmpty()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        
        List<ResolveResult> results = new ArrayList<>();
        
        if (findUsages) {
            // Find all usages of this symbol (for definitions)
            Collection<PsiElement> usages = findAllUsages();
            for (PsiElement usage : usages) {
                results.add(new PsiElementResolveResult(usage));
            }
        } else {
            // Find definitions (original behavior)
            findDefinitions(results);
        }
        
        return results.toArray(new ResolveResult[0]);
    }
    
    /**
     * Find all definitions for a symbol usage (original multiResolve logic).
     */
    private void findDefinitions(List<ResolveResult> results) {
        
        // Priority order for resolution:
        // 1. Local scope (let bindings, function parameters) - highest priority
        // 2. Current file definitions  
        // 3. Project-wide definitions
        
        // 1. Check local scope first - but collect ALL definitions for polyvariant resolution
        PsiElement localDefinition = resolveInLocalScope();
        if (localDefinition != null) {
            results.add(new PsiElementResolveResult(localDefinition));
            // Don't return immediately - collect ALL definitions for polyvariant resolution
        }
        
        // 2. Current file definitions (def, defn, defmacro, etc.)
        Collection<PsiElement> fileDefinitions = resolveInCurrentFile();
        for (PsiElement def : fileDefinitions) {
            // Avoid duplicates
            boolean alreadyAdded = false;
            for (ResolveResult existing : results) {
                if (existing.getElement() == def) {
                    alreadyAdded = true;
                    break;
                }
            }
            
            if (!alreadyAdded) {
                results.add(new PsiElementResolveResult(def));
            }
        }
        
        // 2.5. ALL function parameters in current file (for polyvariant resolution)
        Collection<PsiElement> parameterDefinitions = findAllFunctionParameters();
        for (PsiElement param : parameterDefinitions) {
            // Avoid duplicates - check if we already added this element
            boolean alreadyAdded = false;
            for (ResolveResult existing : results) {
                if (existing.getElement() == param) {
                    alreadyAdded = true;
                    break;
                }
            }
            
            if (!alreadyAdded) {
                results.add(new PsiElementResolveResult(param));
            }
        }
        
        // 3. Project-wide definitions (other files)
        Collection<PsiElement> projectDefinitions = resolveInProject();
        for (PsiElement def : projectDefinitions) {
            // Avoid duplicates
            boolean alreadyAdded = false;
            for (ResolveResult existing : results) {
                if (existing.getElement() == def) {
                    alreadyAdded = true;
                    break;
                }
            }
            
            if (!alreadyAdded) {
                results.add(new PsiElementResolveResult(def));
            }
        }
    }
    
    /**
     * Find all usages of this symbol (for when clicking on definitions).
     * This includes both actual usages AND other definitions (not the one being clicked).
     */
    @NotNull
    private Collection<PsiElement> findAllUsages() {
        List<PsiElement> usages = new ArrayList<>();
        
        // For function parameters, let bindings, etc., only search within the local scope
        if (PhelPsiUtil.isDefinition(myElement) && isLocalBinding()) {
            // For local bindings, search only within the containing form (function/let block)
            Collection<PsiElement> localUsages = findUsagesInLocalScope();
            if (!localUsages.isEmpty()) {
                usages.addAll(localUsages);
                return usages; // Return immediately for local bindings - no project-wide search needed
            }
        }
        
        // 1. Search current file for usages and other definitions
        PsiFile containingFile = myElement.getContainingFile();
        if (containingFile instanceof PhelFile) {
            Collection<PhelSymbol> allSymbols = PsiTreeUtil.findChildrenOfType(containingFile, PhelSymbol.class);
            
            for (PhelSymbol symbol : allSymbols) {
                String name = PhelPsiUtil.getName(symbol);
                if (symbolName.equals(name) && symbol != myElement) {
                    // Include both usages AND other definitions (but not the element we clicked on)
                    usages.add(symbol);
                }
            }
        }
        
        // 2. Search project-wide ONLY for top-level definitions (def, defn, etc.)
        // Skip expensive project-wide search for local bindings
        if (!isLocalBinding()) {
            Project project = myElement.getProject();
            GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
            
            Collection<VirtualFile> phelFiles = FilenameIndex.getAllFilesByExt(project, "phel", projectScope);
            for (VirtualFile virtualFile : phelFiles) {
                if (virtualFile.equals(containingFile.getVirtualFile())) {
                    continue; // Skip current file (already searched above)
                }
                
                PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                if (!(psiFile instanceof PhelFile)) continue;
                
                Collection<PhelSymbol> allSymbols = PsiTreeUtil.findChildrenOfType(psiFile, PhelSymbol.class);
                
                for (PhelSymbol symbol : allSymbols) {
                    String name = PhelPsiUtil.getName(symbol);
                    if (symbolName.equals(name)) {
                        // Include both usages AND definitions from other files
                        usages.add(symbol);
                    }
                }
            }
        }
        
        return usages;
    }
    
    /**
     * Check if this symbol is a local binding (parameter, let binding, etc.)
     * This checks if it's NOT a top-level definition (def, defn, etc.)
     */
    private boolean isLocalBinding() {
        if (!PhelPsiUtil.isDefinition(myElement)) {
            return false; // Not a definition at all
        }
        
        // Check if it's inside a parameter vector or binding vector
        PhelVec parentVec = PsiTreeUtil.getParentOfType(myElement, PhelVec.class);
        if (parentVec == null) {
            return false; // Not in a vector, so likely a top-level definition
        }
        
        // If it's in a vector, it's likely a parameter or binding (local binding)
        return true;
    }
    
    /**
     * Find usages within local scope only (for performance optimization)
     */
    @NotNull
    private Collection<PsiElement> findUsagesInLocalScope() {
        List<PsiElement> usages = new ArrayList<>();
        
        // Find the containing function or let form
        PhelList containingForm = findContainingForm();
        if (containingForm == null) {
            return usages;
        }
        
        // Search only within this form
        Collection<PhelSymbol> localSymbols = PsiTreeUtil.findChildrenOfType(containingForm, PhelSymbol.class);
        
        for (PhelSymbol symbol : localSymbols) {
            String name = PhelPsiUtil.getName(symbol);
            if (symbolName.equals(name) && symbol != myElement && !PhelPsiUtil.isDefinition(symbol)) {
                // Include only usages (not other definitions) within local scope
                usages.add(symbol);
            }
        }
        
        return usages;
    }
    
    /**
     * Find the containing form (defn, fn, let, etc.) for scoped search
     */
    @Nullable
    private PhelList findContainingForm() {
        PsiElement current = myElement.getParent();
        
        while (current != null) {
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                
                // Check if this is a defining form
                PhelForm firstForm = PsiTreeUtil.findChildOfType(list, PhelForm.class);
                if (firstForm != null) {
                    PhelSymbol firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol.class);
                    if (firstSymbol != null) {
                        String keyword = firstSymbol.getText();
                        if (keyword.equals("defn") || keyword.equals("defn-") || 
                            keyword.equals("defmacro") || keyword.equals("defmacro-") ||
                            keyword.equals("fn") || keyword.equals("let") || keyword.equals("if-let") ||
                            keyword.equals("for") || keyword.equals("binding") ||
                            keyword.equals("loop") || keyword.equals("foreach") || keyword.equals("dofor") ||
                            keyword.equals("try") || keyword.equals("catch")) {
                            return list;
                        }
                    }
                }
            }
            current = current.getParent();
        }
        
        return null;
    }
    
    /**
     * Single target resolution - returns null for multiple targets to force modal display.
     * This ensures IntelliJ shows a selection modal when multiple targets exist.
     */
    @Override
    @Nullable
    public PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        
        // For definitions finding usages, return null if multiple usages exist
        // This forces IntelliJ to show the polyvariant selection modal
        if (findUsages && results.length > 1) {
            return null; // Force modal for multiple usages
        }
        
        // For single targets or usage-to-definition, return the first result
        return results.length > 0 ? results[0].getElement() : null;
    }
    
    /**
     * Check if this reference points to the given element.
     */
    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        ResolveResult[] results = multiResolve(false);
        for (ResolveResult result : results) {
            if (element.equals(result.getElement())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Provide completion variants for this reference.
     * Returns symbols that are valid in this context.
     */
    @Override
    public Object @NotNull [] getVariants() {
        List<Object> variants = new ArrayList<>();
        
        // Add local scope definitions (let bindings, parameters)
        PsiElement localDef = resolveInLocalScope();
        if (localDef != null) {
            variants.add(localDef);
        }
        
        // Add file-level definitions
        Collection<PsiElement> fileDefinitions = resolveInCurrentFile();
        variants.addAll(fileDefinitions);
        
        // Add project-wide definitions (limited for performance)
        if (variants.size() < 20) { // Limit to prevent performance issues
            Collection<PsiElement> projectDefs = resolveInProject();
            variants.addAll(projectDefs);
        }
        
        return variants.toArray();
    }
    
    /**
     * Handle renaming of the referenced element.
     * Updates the symbol text to the new name.
     */
    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        if (myElement instanceof PhelSymbol) {
            PhelSymbol symbol = (PhelSymbol) myElement;
            
            // For qualified symbols like "ns/symbol", only rename the symbol part
            String currentText = symbol.getText();
            if (currentText != null && currentText.contains("/")) {
                int slashIndex = currentText.lastIndexOf('/');
                String qualifier = currentText.substring(0, slashIndex + 1);
                String newText = qualifier + newElementName;
                return symbol.setName(newText);
            } else {
                // For unqualified symbols, replace entire text
                return symbol.setName(newElementName);
            }
        }
        return myElement;
    }
    
    /**
     * Bind this reference to a different element.
     */
    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return myElement;
    }
    
    // === Resolution Methods ===
    
    /**
     * Resolve in local scope: let bindings, function parameters, etc.
     * Local bindings have highest priority and shadow everything else.
     */
    @Nullable
    private PsiElement resolveInLocalScope() {
        PsiElement current = myElement;
        
        // Walk up the PSI tree looking for binding contexts
        while (current != null) {
            // Check for let bindings
            PhelList letForm = PsiTreeUtil.getParentOfType(current, PhelList.class);
            if (letForm != null) {
                PsiElement binding = findInLetBindings(letForm);
                if (binding != null) {
                    return binding;
                }
            }
            
            // Check for function parameters
            PsiElement fnParam = findInFunctionParameters(current);
            if (fnParam != null) {
                return fnParam;
            }
            
            current = current.getParent();
        }
        
        return null;
    }
    
    /**
     * Resolve in current file: def, defn, defmacro, etc.
     */
    @NotNull
    private Collection<PsiElement> resolveInCurrentFile() {
        List<PsiElement> results = new ArrayList<>();
        PsiFile file = myElement.getContainingFile();
        
        if (file != null) {
            Collection<PhelList> lists = PsiTreeUtil.findChildrenOfType(file, PhelList.class);
            for (PhelList list : lists) {
                PsiElement definition = findDefinitionInList(list);
                if (definition != null) {
                    results.add(definition);
                }
            }
        }
        
        return results;
    }
    
    /**
     * Resolve in entire project: search all .phel files.
     */
    @NotNull
    private Collection<PsiElement> resolveInProject() {
        List<PsiElement> results = new ArrayList<>();
        Project project = myElement.getProject();
        
        // Find all .phel files in the project
        Collection<VirtualFile> phelFiles = FilenameIndex.getAllFilesByExt(
            project, "phel", GlobalSearchScope.projectScope(project)
        );
        
        PsiManager psiManager = PsiManager.getInstance(project);
        
        for (VirtualFile file : phelFiles) {
            PsiFile psiFile = psiManager.findFile(file);
            if (psiFile != null && !psiFile.equals(myElement.getContainingFile())) {
                // Skip current file (already handled above)
                Collection<PhelList> lists = PsiTreeUtil.findChildrenOfType(psiFile, PhelList.class);
                for (PhelList list : lists) {
                    PsiElement definition = findDefinitionInList(list);
                    if (definition != null) {
                        results.add(definition);
                    }
                }
            }
        }
        
        return results;
    }
    
    // === Helper Methods ===
    
    /**
     * Find symbol in let bindings: (let [symbol value] ...)
     */
    @Nullable
    private PsiElement findInLetBindings(@NotNull PhelList letForm) {
        PhelForm[] forms = PsiTreeUtil.getChildrenOfType(letForm, PhelForm.class);
        if (forms == null || forms.length < 2) {
            return null;
        }
        
        // Check if this is a binding form (let, for, binding, loop, foreach, dofor)
        PhelSymbol firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol.class);
        if (firstSymbol == null) {
            return null;
        }
        String formType = firstSymbol.getText();
        if (!"let".equals(formType) && !"if-let".equals(formType) && !"for".equals(formType) && !"binding".equals(formType) && 
            !"loop".equals(formType) && !"foreach".equals(formType) && !"dofor".equals(formType) &&
            !"catch".equals(formType)) {
            return null;
        }
        
        // Look at binding vector - forms[1] should BE the vector directly
        PhelVec bindingsVec = null;
        if (forms[1] instanceof PhelVec) {
            bindingsVec = (PhelVec) forms[1];
        } else {
            // Fallback: look inside forms[1] for a vector
            bindingsVec = PsiTreeUtil.findChildOfType(forms[1], PhelVec.class);
        }
        
        if (bindingsVec == null) {
            return null;
        }
        
        PhelForm[] bindings = PsiTreeUtil.getChildrenOfType(bindingsVec, PhelForm.class);
        if (bindings == null) {
            return null;
        }
        
        // Bindings are pairs: [symbol1 value1 symbol2 value2 ...]
        for (int i = 0; i < bindings.length; i += 2) {
            PhelSymbol bindingSymbol = PsiTreeUtil.findChildOfType(bindings[i], PhelSymbol.class);
            if (bindingSymbol != null && symbolName.equals(bindingSymbol.getText())) {
                return bindingSymbol;
            }
        }
        
        return null;
    }
    
    /**
     * Find symbol in function parameters: (defn func-name [param1 param2] ...)
     * This searches for ALL function parameters with matching names, not just in the current function.
     */
    @Nullable
    private PsiElement findInFunctionParameters(@NotNull PsiElement context) {
        // Walk up the PSI tree to find ALL parent PhelList elements
        // We need to check each one to see if it's a function definition
        PsiElement current = context;
        
        while (current != null) {
            // Find the next parent PhelList
            PhelList fnForm = PsiTreeUtil.getParentOfType(current, PhelList.class);
            if (fnForm == null) {
                break;
            }
            
            PhelForm[] forms = PsiTreeUtil.getChildrenOfType(fnForm, PhelForm.class);
            if (forms != null && forms.length >= 2) {
                // Check if this is a function definition
                PhelSymbol fnKeyword = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol.class);
                
                if (fnKeyword != null) {
                    String keyword = fnKeyword.getText();
                    PhelVec paramVec = null;
                    
                    // Handle different function types:
                    // (defn name [params] body) - parameter vector at forms[2]
                    // (fn [params] body) - parameter vector at forms[1] 
                    if (keyword.equals("defn") || keyword.equals("defn-") ||
                        keyword.equals("defmacro") || keyword.equals("defmacro-")) {
                        if (forms.length >= 3) {
                            if (forms[2] instanceof PhelVec) {
                                paramVec = (PhelVec) forms[2];
                            } else {
                                paramVec = PsiTreeUtil.findChildOfType(forms[2], PhelVec.class);
                            }
                        }
                    } else if (keyword.equals("fn")) {
                        if (forms.length >= 2) {
                            if (forms[1] instanceof PhelVec) {
                                paramVec = (PhelVec) forms[1];
                            } else {
                                paramVec = PsiTreeUtil.findChildOfType(forms[1], PhelVec.class);
                            }
                        }
                    }
                    
                    if (paramVec != null) {
                        PhelForm[] params = PsiTreeUtil.getChildrenOfType(paramVec, PhelForm.class);
                        if (params != null) {
                            for (PhelForm param : params) {
                                PhelSymbol paramSymbol = PsiTreeUtil.findChildOfType(param, PhelSymbol.class);
                                if (paramSymbol != null && symbolName.equals(paramSymbol.getText())) {
                                    return paramSymbol;
                                }
                            }
                        }
                    }
                }
            }
            
            // Continue searching up the tree
            current = fnForm.getParent();
        }
        
        return null;
    }
    
    /**
     * Find ALL function parameters with matching names in the current file.
     * This is for polyvariant resolution - show all parameter definitions.
     */
    @NotNull
    private Collection<PsiElement> findAllFunctionParameters() {
        List<PsiElement> results = new ArrayList<>();
        PsiFile file = myElement.getContainingFile();
        
        if (file != null) {
            Collection<PhelList> lists = PsiTreeUtil.findChildrenOfType(file, PhelList.class);
            for (PhelList list : lists) {
                PhelForm[] forms = PsiTreeUtil.getChildrenOfType(list, PhelForm.class);
                if (forms == null || forms.length < 2) continue;
                
                // Check if this is a function definition
                PhelSymbol fnKeyword = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol.class);
                if (fnKeyword == null) continue;
                
                String keyword = fnKeyword.getText();
                PhelVec paramVec = null;
                
                // Handle different function types
                if (keyword.equals("defn") || keyword.equals("defn-") ||
                    keyword.equals("defmacro") || keyword.equals("defmacro-")) {
                    if (forms.length >= 3) {
                        paramVec = PsiTreeUtil.findChildOfType(forms[2], PhelVec.class);
                    }
                } else if (keyword.equals("fn")) {
                    if (forms.length >= 2) {
                        paramVec = PsiTreeUtil.findChildOfType(forms[1], PhelVec.class);
                    }
                } else {
                    continue;  // Not a function we recognize
                }
                
                if (paramVec == null) continue;
                
                PhelForm[] params = PsiTreeUtil.getChildrenOfType(paramVec, PhelForm.class);
                if (params == null) continue;
                
                for (PhelForm param : params) {
                    PhelSymbol paramSymbol = PsiTreeUtil.findChildOfType(param, PhelSymbol.class);
                    if (paramSymbol != null && symbolName.equals(paramSymbol.getText())) {
                        results.add(paramSymbol);
                    }
                }
            }
        }
        
        return results;
    }
    
    /**
     * Find definition in a list form: (def name value), (defn name [...] ...), etc.
     */
    @Nullable
    private PsiElement findDefinitionInList(@NotNull PhelList list) {
        PhelForm[] forms = PsiTreeUtil.getChildrenOfType(list, PhelForm.class);
        
        if (forms == null || forms.length < 2) {
            return null;
        }
        
        // Check if this is a defining form
        PhelSymbol defKeyword = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol.class);
        String keywordText = defKeyword != null ? defKeyword.getText() : null;
        
        if (defKeyword == null || !isDefiningKeyword(keywordText)) {
            return null;
        }
        
        // Get the defined name (second element)
        PhelSymbol definedName = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol.class);
        
        if (definedName != null && symbolName.equals(definedName.getText())) {
            return definedName;
        }
        
        return null;
    }
    
    /**
     * Check if a keyword is a defining form.
     */
    private boolean isDefiningKeyword(@Nullable String keyword) {
        if (keyword == null) return false;
        
        return keyword.equals("def") || 
               keyword.equals("defn") || 
               keyword.equals("defmacro") ||
               keyword.equals("defstruct") ||
               keyword.equals("declare") ||
               keyword.equals("def-") ||
               keyword.equals("defn-") ||
               keyword.equals("defmacro-");
    }
    
}
