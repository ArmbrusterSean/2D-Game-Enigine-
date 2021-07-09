package jade;

import java.util.ArrayList;
import java.util.List;

// Scene Manager
public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {
        // Game objects, renderer, physics handle
    }

    public void init() {

    }

    public void start() {
        for(GameObject go : gameObjects) {
            go.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
        }
    }

    // pass update method
    public abstract void update(float dt);

}
