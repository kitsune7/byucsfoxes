package edu.byu.cs.superasteroids.main_menu;

import android.content.Context;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.Cannon;
import edu.byu.cs.superasteroids.model.Engine;
import edu.byu.cs.superasteroids.model.ExtraPart;
import edu.byu.cs.superasteroids.model.MainBody;
import edu.byu.cs.superasteroids.model.PowerCore;
import edu.byu.cs.superasteroids.model.Ship;

/**
 * Allows you to skip the ship builder with a pre-chosen ship.
 */
public class MainMenuController implements IMainMenuController {
    private IMainMenuView view; /** We need to view to start the game */
    private Context context; /** Needed to load data from the model */

    public MainMenuController(Context context, IMainMenuView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void onQuickPlayPressed() {
        AsteroidsGame instance = AsteroidsGame.getInstance(context);
        Ship ship = instance.getShip();
        ArrayList<MainBody> mainBodies = instance.getMainBodies();
        ArrayList<Cannon> cannons = instance.getCannons();
        ArrayList<ExtraPart> extraParts = instance.getExtraParts();
        ArrayList<Engine> engines = instance.getEngines();
        ArrayList<PowerCore> powerCores = instance.getPowerCores();

        // We just want the second ship for quick play because it looks cool
        ship.setBody(mainBodies.get(1));
        ship.setCannon(cannons.get(1));
        ship.setExtra(extraParts.get(1));
        ship.setEngine(engines.get(1));
        ship.setCore(powerCores.get(1));

        view.startGame();
    }

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public void setView(IView view) {}
}
