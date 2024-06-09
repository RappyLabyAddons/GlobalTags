package com.rappytv.globaltags.activities;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import java.util.UUID;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@Link("report.lss")
@AutoActivity
public class ChangeTagActivity extends SimpleActivity {

    private final UUID uuid;
    private final String username;

    public ChangeTagActivity(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        TagCache.resolve(uuid, (info) -> {
            FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
            HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header");
            IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
            ComponentWidget titleWidget = ComponentWidget.i18n("globaltags.context.changeTag.title", this.username).addId("username");
            VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");
            ComponentWidget labelWidget = ComponentWidget.i18n("globaltags.context.changeTag.label").addId("reason");
            TextFieldWidget textField = new TextFieldWidget()
                .placeholder(Component.translatable("globaltags.context.changeTag.placeholder", NamedTextColor.DARK_GRAY))
                .addId("text-field");
            boolean hasTag = info.getPlainTag() != null;
            textField.setText(hasTag ? info.getPlainTag() : "");
            ButtonWidget button = new ButtonWidget()
                .updateComponent(Component.translatable("globaltags.context.changeTag.send", NamedTextColor.AQUA))
                .addId("report-button");
            button.setEnabled(!textField.getText().isBlank() && (!hasTag || !textField.getText().equals(info.getPlainTag())));
            button.setActionListener(() -> {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
                ApiHandler.setTag(uuid, textField.getText(), (response) -> Laby.references().chatExecutor().displayClientMessage(
                    Component
                        .text(GlobalTagAddon.prefix)
                        .append(response.getMessage())
                ));
            });
            textField.updateListener((text) -> button.setEnabled(!text.isBlank() && (!hasTag || !textField.getText().equals(info.getPlainTag()))));

            profileWrapper.addEntry(headWidget);
            profileWrapper.addEntry(titleWidget);

            content.addChild(labelWidget);
            content.addChild(textField);
            content.addChild(button);

            windowWidget.addContent(profileWrapper);
            windowWidget.addContent(content);
            this.document.addChild(windowWidget);
        });
    }
}