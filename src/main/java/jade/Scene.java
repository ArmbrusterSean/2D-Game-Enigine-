package jade;
// Scene Manager
public abstract class Scene {

    protected Camera camera;

    public Scene() {
        // Game objects, renderer, physics handle
    }

    public void init() {

    }

    // pass update method
    public abstract void update(float dt);

}
