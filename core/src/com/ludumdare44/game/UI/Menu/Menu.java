package com.ludumdare44.game.UI.Menu;

import com.ludumdare44.game.GFX.GFXManager;
import com.ludumdare44.game.UI.INavigatable;

public abstract class Menu implements INavigatable {
    private static Option[] options;

    private int optionIndex = 0;

    public void upOption() {
        optionIndex--;
        if (optionIndex < 0) optionIndex = options.length - 1;
    }

    public void downOption() {
        optionIndex++;
        if (optionIndex > options.length - 1) optionIndex = 0;
    }

    public void selectOption() {
        options[optionIndex].use();
    }

    public abstract void render(GFXManager gfx);

    Menu(Option[] _options) {
        options = _options;
    }
}
