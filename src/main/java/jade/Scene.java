package jade;
// Scene Manager
public abstract class Scene {

    public Scene() {
        // Game objects, renderer, physics handle
    }

    public void init() {

    }

    // pass update method
    public abstract void update(float dt);

}
