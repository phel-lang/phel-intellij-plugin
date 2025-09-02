package org.phellang.language.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * Custom presentation for Phel PSI elements in navigation dialogs.
 * This controls how elements appear in "Choose Declaration" and similar modals.
 */
public class PhelItemPresentation implements ItemPresentation {
    
    private final PhelSymbol myElement;
    
    public PhelItemPresentation(@NotNull PhelSymbol element) {
        this.myElement = element;
    }
    
    @Override
    @Nullable
    public String getPresentableText() {
        String name = PhelPsiUtil.getName(myElement);
        if (name == null) return null;
        
        // Add type information if this is a definition
        if (PhelPsiUtil.isDefinition(myElement)) {
            String definingKeyword = getDefiningKeyword(myElement);
            String type = getTypeFromDefiningKeyword(definingKeyword);
            if (type != null) {
                return name + " [" + type + "]";
            }
        }
        
        return name;
    }
    
    @Override
    @Nullable
    public String getLocationString() {
        // Get context and location information
        String context = getElementContext();
        String location = getLocationInfo();
        
        if (context != null && location != null) {
            return context + " " + location;
        } else if (context != null) {
            return context;
        } else if (location != null) {
            return location;
        }
        
        return null;
    }
    
    @Override
    @Nullable
    public Icon getIcon(boolean unused) {
        // Could return different icons based on element type
        return null;
    }
    
    /**
     * Get contextual information about where/how the element is used.
     */
    @Nullable
    private String getElementContext() {
        PhelList containingList = PsiTreeUtil.getParentOfType(myElement, PhelList.class);
        if (containingList != null) {
            String listText = containingList.getText();
            if (listText != null) {
                if (listText.length() > 60) {
                    // Truncate long expressions
                    return "(" + listText.substring(1, Math.min(50, listText.length() - 1)) + "...)";
                } else {
                    return listText;
                }
            }
        }
        return null;
    }
    
    /**
     * Get location information (file name and line number).
     */
    @NotNull
    private String getLocationInfo() {
        StringBuilder location = new StringBuilder();
        
        PsiFile containingFile = myElement.getContainingFile();
        if (containingFile != null) {
            String fileName = containingFile.getName();
            location.append("(").append(fileName);
            
            int lineNumber = getLineNumber();
            if (lineNumber > 0) {
                location.append(":").append(lineNumber);
            }
            
            location.append(")");
        }
        
        return location.toString();
    }
    
    /**
     * Calculate the line number for the element.
     */
    private int getLineNumber() {
        PsiFile containingFile = myElement.getContainingFile();
        if (containingFile != null) {
            String fileText = containingFile.getText();
            if (fileText != null) {
                int offset = myElement.getTextOffset();
                int lineNumber = 1;
                for (int i = 0; i < offset && i < fileText.length(); i++) {
                    if (fileText.charAt(i) == '\n') {
                        lineNumber++;
                    }
                }
                return lineNumber;
            }
        }
        return 0;
    }
    
    /**
     * Get the defining keyword (def, defn, defmacro, etc.) for a symbol definition.
     */
    @Nullable
    private String getDefiningKeyword(@NotNull PhelSymbol symbol) {
        PhelList containingList = PsiTreeUtil.getParentOfType(symbol, PhelList.class);
        if (containingList != null) {
            PhelForm firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm.class);
            if (firstForm != null) {
                PhelSymbol firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol.class);
                if (firstSymbol != null) {
                    return firstSymbol.getText();
                }
            }
        }
        return null;
    }
    
    /**
     * Convert defining keyword to readable type name.
     */
    @Nullable
    private String getTypeFromDefiningKeyword(@Nullable String keyword) {
        if (keyword == null) return null;
        
        switch (keyword) {
            case "def":
            case "def-":
                return "variable";
            case "defn":
            case "defn-":
                return "function";
            case "defmacro":
            case "defmacro-":
                return "macro";
            case "defstruct":
                return "struct";
            case "definterface":
                return "interface";
            case "defexception":
                return "exception";
            case "declare":
                return "declaration";
            default:
                return null;
        }
    }
}



