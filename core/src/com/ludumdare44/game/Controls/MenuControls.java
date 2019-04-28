package com.ludumdare44.game.Controls;

import com.ludumdare44.game.UI.Menu.MenuManager;

public class MenuControls {
    private ControlManager controlManager;
    private MenuManager menuManager;

    public void update() {

    }

    public MenuControls(ControlManager _controlManager, MenuManager _menuManager) {
        menuManager = _menuManager;
        controlManager = _controlManager;
    }
}
