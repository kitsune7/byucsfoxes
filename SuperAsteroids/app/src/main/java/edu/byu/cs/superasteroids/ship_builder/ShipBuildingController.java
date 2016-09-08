package edu.byu.cs.superasteroids.ship_builder;

import android.content.Context;

import java.util.ArrayList;
import java.util.TreeMap;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.Cannon;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Engine;
import edu.byu.cs.superasteroids.model.ExtraPart;
import edu.byu.cs.superasteroids.model.MainBody;
import edu.byu.cs.superasteroids.model.PowerCore;
import edu.byu.cs.superasteroids.model.Ship;

/**
 * Controls the ship builder.
 */
public class ShipBuildingController implements IShipBuildingController {
    private IShipBuildingView view; /** The ship building view is needed to access certain functions */
    private Context context; /** We need the context for loading content from the model. */
    private Ship ship; /** There is only ever one ship object and we add parts to it here.  */

    private int currentStateIndex; /** The index of the current state */
    private final String[] states = {"Main body", "Engine", "Cannon", "Extra part", "Power core"}; /** The possible states */

    private TreeMap<String,Integer> selectedPartMap; /** Maps the part name to an image id. */
    private TreeMap<String,IShipBuildingView.PartSelectionView> nameEnumMap; /** Maps part name to its enum */
    private TreeMap<Integer,MainBody> idBodyMap; /** Maps an image id to a MainBody */
    private TreeMap<Integer,Cannon> idCannonMap; /** Maps an image id to a Cannon */
    private TreeMap<Integer,ExtraPart> idPartMap; /** Maps an image id to a ExtraPart */
    private TreeMap<Integer,Engine> idEngineMap; /** Maps an image id to a Engine */
    private TreeMap<Integer,PowerCore> idCoreMap; /** Maps an image id to a PowerCore */

    public ShipBuildingController(Context context, IShipBuildingView view) {
        this.context = context;
        this.view = view;

        nameEnumMap = new TreeMap<String,IShipBuildingView.PartSelectionView>();
        nameEnumMap.put("Main body", IShipBuildingView.PartSelectionView.MAIN_BODY);
        nameEnumMap.put("Engine", IShipBuildingView.PartSelectionView.ENGINE);
        nameEnumMap.put("Cannon", IShipBuildingView.PartSelectionView.CANNON);
        nameEnumMap.put("Extra part", IShipBuildingView.PartSelectionView.EXTRA_PART);
        nameEnumMap.put("Power core", IShipBuildingView.PartSelectionView.POWER_CORE);

        selectedPartMap = new TreeMap<String,Integer>();
        selectedPartMap.put("Main body", -1);
        selectedPartMap.put("Engine", -1);
        selectedPartMap.put("Cannon", -1);
        selectedPartMap.put("Extra part", -1);
        selectedPartMap.put("Power core", -1);

        idBodyMap = new TreeMap<Integer,MainBody>();
        idEngineMap = new TreeMap<Integer,Engine>();
        idCannonMap = new TreeMap<Integer,Cannon>();
        idPartMap = new TreeMap<Integer,ExtraPart>();
        idCoreMap = new TreeMap<Integer,PowerCore>();

        currentStateIndex = 0;
    }

    @Override
    public void onViewLoaded(IShipBuildingView.PartSelectionView partView) {
        // I don't want to use the up and down arrows
        view.setArrow(partView, IShipBuildingView.ViewDirection.UP, false, "");
        view.setArrow(partView, IShipBuildingView.ViewDirection.DOWN, false, "");

        String left_part = (currentStateIndex == 0)?
                states[states.length-1]: states[currentStateIndex-1];
        String right_part = (currentStateIndex == states.length-1)?
                states[0]: states[currentStateIndex+1];

        view.setArrow(partView, IShipBuildingView.ViewDirection.LEFT, true, left_part);
        view.setArrow(partView, IShipBuildingView.ViewDirection.RIGHT, true, right_part);
        if (ship.getBody() != null && ship.getCannon() != null && ship.getEngine() != null &&
                ship.getExtra() != null && ship.getCore() != null) {
            view.setStartGameButton(true);
        }
    }

    @Override
    public void update(double elapsedTime) {}

    @Override
    public void loadContent(ContentManager content) {
        AsteroidsGame instance = AsteroidsGame.getInstance(context);
        ship = instance.getShip();
        
        ArrayList<MainBody> mainBodies = instance.getMainBodies();
        ArrayList<Cannon> cannons = instance.getCannons();
        ArrayList<ExtraPart> extraParts = instance.getExtraParts();
        ArrayList<Engine> engines = instance.getEngines();
        ArrayList<PowerCore> powerCores = instance.getPowerCores();

        ArrayList<String> mainBodyImages = new ArrayList<String>();
        ArrayList<String> cannonImages = new ArrayList<String>();
        ArrayList<String> extraPartImages = new ArrayList<String>();
        ArrayList<String> engineImages = new ArrayList<String>();
        ArrayList<String> powerCoreImages = new ArrayList<String>();

        ArrayList<String> partImages = new ArrayList<String>();
        for (MainBody body: mainBodies) {
            partImages.add(body.getImage().getPath());
            mainBodyImages.add(body.getImage().getPath());
        }
        for (Cannon cannon: cannons) {
            partImages.add(cannon.getImage().getPath());
            cannonImages.add(cannon.getImage().getPath());
        }
        for (ExtraPart part: extraParts) {
            partImages.add(part.getImage().getPath());
            extraPartImages.add(part.getImage().getPath());
        }
        for (Engine engine: engines) {
            partImages.add(engine.getImage().getPath());
            engineImages.add(engine.getImage().getPath());
        }
        for (PowerCore core: powerCores) {
            partImages.add(core.getImagePath());
            powerCoreImages.add(core.getImagePath());
        }
        content.loadImages(partImages);

        ArrayList<Integer> mainBodyIds = new ArrayList<Integer>();
        ArrayList<Integer> cannonIds = new ArrayList<Integer>();
        ArrayList<Integer> extraPartIds = new ArrayList<Integer>();
        ArrayList<Integer> engineIds = new ArrayList<Integer>();
        ArrayList<Integer> powerCoreIds = new ArrayList<Integer>();
        
        for (int i = 0; i < mainBodyImages.size(); ++i) {
            String path = mainBodyImages.get(i);
            mainBodyIds.add(content.getImageId(path));
            idBodyMap.put(content.getImageId(path), mainBodies.get(i));
        }
        view.setPartViewImageList(IShipBuildingView.PartSelectionView.MAIN_BODY, mainBodyIds);

        for (int i = 0; i < cannonImages.size(); ++i) {
            String path = cannonImages.get(i);
            cannonIds.add(content.getImageId(path));
            idCannonMap.put(content.getImageId(path), cannons.get(i));
        }
        view.setPartViewImageList(IShipBuildingView.PartSelectionView.CANNON, cannonIds);

        for (int i = 0; i < extraPartImages.size(); ++i) {
            String path = extraPartImages.get(i);
            extraPartIds.add(content.getImageId(path));
            idPartMap.put(content.getImageId(path), extraParts.get(i));
        }
        view.setPartViewImageList(IShipBuildingView.PartSelectionView.EXTRA_PART, extraPartIds);

        for (int i = 0; i < engineImages.size(); ++i) {
            String path = engineImages.get(i);
            engineIds.add(content.getImageId(path));
            idEngineMap.put(content.getImageId(path), engines.get(i));
        }
        view.setPartViewImageList(IShipBuildingView.PartSelectionView.ENGINE, engineIds);

        for (int i = 0; i < powerCoreImages.size(); ++i) {
            String path = powerCoreImages.get(i);
            powerCoreIds.add(content.getImageId(path));
            idCoreMap.put(content.getImageId(path), powerCores.get(i));
        }
        view.setPartViewImageList(IShipBuildingView.PartSelectionView.POWER_CORE, powerCoreIds);
    }

    @Override
    public void unloadContent(ContentManager content) {
        unloadPiece(content, "Main body", idBodyMap);
        unloadPiece(content, "Cannon", idCannonMap);
        unloadPiece(content, "Extra part", idPartMap);
        unloadPiece(content, "Engine", idEngineMap);
        unloadPiece(content, "Power core", idCoreMap);
    }

    /**
     * Unloads a particular piece of the ship
     * @param content The content manager
     * @param type The string describing the piece of the ship (e.g. "Main body")
     * @param map A map with all the image ids for the ship pieces
     */
    private void unloadPiece(ContentManager content, String type, TreeMap map) {
        if (selectedPartMap.get(type) == map.firstKey()) {
            content.unloadImage((int)map.lastKey());
        } else {
            content.unloadImage((int)map.firstKey());
        }
    }

    @Override
    public void draw() {
        Coordinate center = new Coordinate(
                DrawingHelper.getGameViewWidth()/(float)2,
                DrawingHelper.getGameViewHeight()/(float)3
        );

        if (selectedPartMap.get("Main body") != -1) {
            DrawingHelper.drawImage(
                    selectedPartMap.get("Main body"),
                    center.getExactX(),
                    center.getExactY(),
                    0,
                    ship.getScale(),
                    ship.getScale(),
                    255
            );
        }
        if (selectedPartMap.get("Engine") != -1) {
            DrawingHelper.drawImage(
                    selectedPartMap.get("Engine"),
                    center.getExactX() + ship.getEngineOffset().getExactX(),
                    center.getExactY() + ship.getEngineOffset().getExactY(),
                    0,
                    ship.getScale(),
                    ship.getScale(),
                    255
            );
        }
        if (selectedPartMap.get("Cannon") != -1) {
            DrawingHelper.drawImage(
                    selectedPartMap.get("Cannon"),
                    center.getExactX() + ship.getCannonOffset().getExactX(),
                    center.getExactY() + ship.getCannonOffset().getExactY(),
                    0,
                    ship.getScale(),
                    ship.getScale(),
                    255
            );
        }
        if (selectedPartMap.get("Extra part") != -1) {
            DrawingHelper.drawImage(
                    selectedPartMap.get("Extra part"),
                    center.getExactX() + ship.getExtraPartOffset().getExactX(),
                    center.getExactY() + ship.getExtraPartOffset().getExactY(),
                    0,
                    ship.getScale(),
                    ship.getScale(),
                    255
            );
        }
    }

    @Override
    public void onSlideView(IShipBuildingView.ViewDirection direction) {
        IShipBuildingView.PartSelectionView newView;

        switch (direction) {
            case LEFT: // Moving RIGHT
                newView = nameEnumMap.get(
                        (currentStateIndex == states.length-1)?
                        states[0]: states[currentStateIndex+1]
                );
                view.animateToView(newView,IShipBuildingView.ViewDirection.RIGHT);
                currentStateIndex = (currentStateIndex == states.length-1)? 0: currentStateIndex+1;
                break;

            case RIGHT: // Moving LEFT
                newView = nameEnumMap.get(
                        (currentStateIndex == 0)?
                        states[states.length-1]: states[currentStateIndex-1]
                );
                view.animateToView(newView,IShipBuildingView.ViewDirection.LEFT);
                currentStateIndex = (currentStateIndex == 0)? states.length-1: currentStateIndex-1;
                break;
        }
    }

    @Override
    public void onPartSelected(int index) {
        switch(states[currentStateIndex]) {
            case "Main body":
                selectedPartMap.put("Main body", (index == 0)? idBodyMap.firstKey(): idBodyMap.lastKey());
                ship.setBody( idBodyMap.get(selectedPartMap.get("Main body")) );
                break;
            case "Engine":
                selectedPartMap.put("Engine", (index == 0) ? idEngineMap.firstKey() : idEngineMap.lastKey());
                ship.setEngine(idEngineMap.get(selectedPartMap.get("Engine")));
                break;
            case "Cannon":
                selectedPartMap.put("Cannon", (index == 0)? idCannonMap.firstKey(): idCannonMap.lastKey());
                ship.setCannon(idCannonMap.get(selectedPartMap.get("Cannon")));
                break;
            case "Extra part":
                selectedPartMap.put("Extra part", (index == 0)? idPartMap.firstKey(): idPartMap.lastKey());
                ship.setExtra( idPartMap.get(selectedPartMap.get("Extra part")) );
                break;
            case "Power core":
                selectedPartMap.put("Power core", (index == 0)? idCoreMap.firstKey(): idCoreMap.lastKey());
                ship.setCore( idCoreMap.get(selectedPartMap.get("Power core")) );
                break;
        }
        if (ship.getBody() != null && ship.getCannon() != null && ship.getEngine() != null &&
                ship.getExtra() != null && ship.getCore() != null) {
            view.setStartGameButton(true);
        }
    }

    @Override
    public void onStartGamePressed() {
        view.startGame();
    }

    @Override
    public void onResume() {

    }

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public void setView(IView view) {}
}
