package com.strixsoftware.intellij.format;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInsight.intention.impl.FieldFromParameterUtils;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.strixsoftware.intellij.StrixSoftwareBundle;
import org.jetbrains.annotations.NotNull;

public class FormatMethodParametersAction extends BaseIntentionAction {

    private static final Logger log = Logger.getInstance(FormatMethodParametersAction.class);

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        final PsiParameter parameter = FieldFromParameterUtils.findParameterAtCursor(psiFile, editor);
        if (parameter == null || !isAvailable(parameter)) {
            return false;
        }
        setText(StrixSoftwareBundle.message("intention.format.method.parameter.list.text", parameter.getName()));

        return true;
    }

    protected boolean isAvailable(PsiParameter psiParameter) {
        final PsiType type = getSubstitutedType(psiParameter);
        final PsiClass targetClass = PsiTreeUtil.getParentOfType(psiParameter, PsiClass.class);
        return FieldFromParameterUtils.isAvailable(psiParameter, type, targetClass) &&
                psiParameter.getLanguage().isKindOf(JavaLanguage.INSTANCE);
    }

    protected PsiType getSubstitutedType(PsiParameter parameter) {
        return FieldFromParameterUtils.getSubstitutedType(parameter);
    }

    @Override
    @NotNull
    public String getFamilyName() {
        return StrixSoftwareBundle.message("intention.format.method.parameter.list.family");
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        final PsiParameter myParameter = FieldFromParameterUtils.findParameterAtCursor(psiFile, editor);
        if (myParameter == null || !FileModificationService.getInstance().prepareFileForWrite(psiFile)) return;

        IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
        try {
            processParameter(project, myParameter, !ApplicationManager.getApplication().isHeadlessEnvironment());
        } catch (IncorrectOperationException e) {
            log.error(e);
        }
    }

    private void processParameter(final @NotNull Project project,
                                  final @NotNull PsiParameter myParameter,
                                  final boolean isInteractive) {
    }

}
