package com.strixsoftware.intellij.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.siyeh.IntentionPowerPackBundle;
import com.siyeh.ipp.base.MutablyNamedIntention;
import com.siyeh.ipp.base.PsiElementEditorPredicate;
import com.siyeh.ipp.base.PsiElementPredicate;
import com.strixsoftware.intellij.StrixSoftwareBundle;
import org.jetbrains.annotations.NotNull;

import static com.siyeh.ipp.psiutils.HighlightUtil.highlightElement;

public class FormatMethodParametersDefinitionIntention extends MutablyNamedIntention {

    @Override
    protected String getTextForElement(PsiElement psiElement) {
        return StrixSoftwareBundle.message("intention.format.method.parameters.text");
    }

    @Override
    protected void processIntention(@NotNull PsiElement psiElement) {
        PsiParameterList parameterList = (PsiParameterList) psiElement;
        PsiParameter[] parameters = parameterList.getParameters();
        if (parameters.length == 0) {
            return;
        }

        PsiParserFacade parserFacade = PsiParserFacade.SERVICE.getInstance(psiElement.getProject());
        for (PsiParameter parameter : parameters) {
            if (!(parameter.getPrevSibling() instanceof PsiWhiteSpace
                    && parameter.getPrevSibling().getText().contains("\n"))) {
                parameterList.addBefore(parserFacade.createWhiteSpaceFromText("\n"), parameter);
            }
        }
        parameterList.add(parserFacade.createWhiteSpaceFromText("\n"));

        highlightElement(parameterList, IntentionPowerPackBundle.message("press.escape.to.remove.highlighting.message"));
    }

    @NotNull
    @Override
    protected PsiElementPredicate getElementPredicate() {
        return new PsiElementEditorPredicate() {
            @Override
            public boolean satisfiedBy(PsiElement psiElement, Editor editor) {
                return psiElement instanceof PsiParameterList;
            }
        };

    }
}
