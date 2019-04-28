package com.ludumdare44.game.UI.Menu;

import com.ludumdare44.game.GFX.GFXManager;

public class MenuManager implements INavigatable {
    private Menu currentMenu;
    private Menu controlsMenu = new ControlsMenu();
    private Menu creditsMenu = new CreditsMenu();
    private boolean menuEnabled = false;

    public class MainMenu extends Menu {
        @Override
        public void render(GFXManager gfx) {

        }

        MainMenu() {
            super(new Option[] {
                    () -> menuEnabled = false,
                    () -> currentMenu = controlsMenu,
                    () -> currentMenu = creditsMenu
            });
        }
    }

    public boolean enabled() { return menuEnabled; }

    @Override
    public void render(GFXManager gfx) {
        currentMenu.render(gfx);
    }

    @Override
    public void upOption() {
        currentMenu.upOption();
    }

    @Override
    public void downOption() {
        currentMenu.downOption();
    }

    @Override
    public void selectOption() {
        currentMenu.selectOption();
    }

    public MenuManager() {
        currentMenu = new MainMenu();
    }
}
