package com.intellij.plugin.cljfmt.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.plugin.cljfmt.state.AppSettingsState;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AppSettingsConfigurable implements Configurable {
    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "cljfmt";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modifiedReplHost = !mySettingsComponent.getReplHostText().equals(settings.replHost);
        boolean modifiedReplPort = !mySettingsComponent.getReplPortText().equals(Integer.toString(settings.replPort));
        return modifiedReplHost || modifiedReplPort;
    }

    @Override
    public void apply() throws ConfigurationException {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.replHost = mySettingsComponent.getReplHostText();
        settings.replPort = Integer.parseInt(mySettingsComponent.getReplPortText());
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setReplHostText(settings.replHost);
        mySettingsComponent.setReplPortText(Integer.toString(settings.replPort));
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
