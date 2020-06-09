package com.intellij.plugin.cljfmt.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {
    private final JPanel myMainPanel;
    private final JBTextField replHostText = new JBTextField();
    private final JBTextField replPortText = new JBTextField();

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("nREPL Host"), replHostText, 1, false)
                .addLabeledComponent(new JBLabel("nREPL Port"), replPortText, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return replHostText;
    }

    @NotNull
    public String getReplHostText() {
        return replHostText.getText();
    }

    public void setReplHostText(@NotNull String host) {
        replHostText.setText(host);
    }

    @NotNull
    public String getReplPortText() {
        return replPortText.getText();
    }

    public void setReplPortText(@NotNull String port) {
        replPortText.setText(port);
    }
}
