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

public class FormatMethodParametersInvocationIntention extends MutablyNamedIntention {

    @Override
    protected String getTextForElement(PsiElement psiElement) {
        return StrixSoftwareBundle.message("intention.format.method.parameters.text");
    }

    @Override
    protected void processIntention(@NotNull PsiElement psiElement) {
        PsiCall method = (PsiCall) psiElement;
        PsiExpressionList expressionList = method.getArgumentList();
        if (expressionList == null) {
            return;
        }

        PsiExpression[] expressions = expressionList.getExpressions();
        if (expressions.length == 0) {
            return;
        }

        PsiParserFacade parserFacade = PsiParserFacade.SERVICE.getInstance(psiElement.getProject());
        for (PsiExpression expression : expressions) {
            if (!(expression.getPrevSibling() instanceof PsiWhiteSpace
                    && expression.getPrevSibling().getText().contains("\n"))) {
                expressionList.addBefore(parserFacade.createWhiteSpaceFromText("\n"), expression);
            }
        }
        expressionList.add(parserFacade.createWhiteSpaceFromText("\n"));

        highlightElement(expressionList, IntentionPowerPackBundle.message("press.escape.to.remove.highlighting.message"));
    }

    @NotNull
    @Override
    protected PsiElementPredicate getElementPredicate() {
        return new PsiElementEditorPredicate() {
            @Override
            public boolean satisfiedBy(PsiElement psiElement, Editor editor) {
                return psiElement instanceof PsiCall;
            }
        };

    }
}
