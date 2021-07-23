package jade;

public abstract class Component {

    // Transient so that GSON will not serialize
    public transient GameObject gameObject = null;

    public void start() {

    }

    public void update(float dt) {

    }

    public void imgui() {

    }
}
