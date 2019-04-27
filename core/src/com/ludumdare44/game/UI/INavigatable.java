package com.ludumdare44.game.UI;

import com.ludumdare44.game.GFX.IRenderable;

public interface INavigatable extends IRenderable {
    public void upOption();

    public void downOption();

    public void selectOption();
}