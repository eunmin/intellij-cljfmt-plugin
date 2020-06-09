package com.intellij.plugin.cljfmt.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugin.cljfmt.exception.ReplConnectionException;
import com.intellij.plugin.cljfmt.repl.ReplClient;
import com.intellij.plugin.cljfmt.state.AppSettingsState;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

public class ReformatAction extends AnAction {

    private String escapeCode(String code) {
        String text = code;
        text = text.replace("\\", "\\\\");
        return text.replace("\"", "\\\"");
    }

    private String unescapeCode(String code) {
        String text = code;
        text = text.substring(1, text.length() - 1);
        return StringEscapeUtils.unescapeJava(text);
    }

    public void reformat(Project project,  Editor editor) {
        AppSettingsState settings = AppSettingsState.getInstance();
        ReplClient repl = null;
        try {
            repl = new ReplClient(settings.replHost, settings.replPort);
            repl.connect();

            String text = editor.getDocument().getText();
            String code = String.format("(do (require 'cljfmt.core) (cljfmt.core/reformat-string \"%s\"))", escapeCode(text));
            String result = repl.eval(code);

            WriteCommandAction.runWriteCommandAction(project, () -> editor.getDocument().setText(unescapeCode(result)));
        } catch (ReplConnectionException e) {
            new ErrorsNotifier().notify(e.getMessage());
        } catch (Exception e) {
            if (e.getMessage() != null) {
                new ErrorsNotifier().notify(e.getMessage());
            }
            e.printStackTrace();
        } finally {
            if (repl != null) repl.close();
        }
    }

    public boolean isClojureFile(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        return vFile.getName().endsWith(".clj")
                || vFile.getName().endsWith(".cljs")
                || vFile.getName().endsWith(".cljc");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();

        if (isClojureFile(e)) {
            final Editor editor = e.getData(CommonDataKeys.EDITOR);
            reformat(project, editor);
        } else {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                CodeStyleManager styleManager = CodeStyleManager.getInstance(project);
                PsiElement psiFile = e.getData(LangDataKeys.PSI_FILE);
                styleManager.reformat(psiFile);
            });
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isClojureFile(e));
    }
}
