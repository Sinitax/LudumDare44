package com.ludumdare44.game.UI.Menu;

import com.ludumdare44.game.GFX.IRenderable;

 interface INavigatable extends IRenderable {
     void upOption();

     void downOption();

     void selectOption();
}