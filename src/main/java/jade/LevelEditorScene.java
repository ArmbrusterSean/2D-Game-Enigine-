package jade;


import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    // identifiers to pass data to GPU from CPU
    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
          // position               // color                           // UV Coordinates
          100.5f, 0.5f,   0.0f,      1.0f, 0.0f, 0.0f, 1.0f,           1, 1, // Bottom right   | 0
          -0.5f,  100.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,           0, 0, // Top left       | 1
          100.5f, 100.5f, 0.0f,      1.0f, 0.0f, 1.0f, 1.0f,           1, 0, // Top right      | 2
          0.5f,   0.5f,   0.0f,      1.0f, 1.0f, 0.0f, 1.0f,           0, 1  // Bottom left    | 3
    };

    // IMPORTANT : Must be in counter clockwise order
    private int[] elementArray = {
            /*
                    x       x


                    x       x
             */
            2, 1, 0, // Top right Triangle
            0, 1, 3, // bottom left triangle
    };

    // vertex array object, vertex buffer object, element buffer object
    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        // Initialize camera at (0,0)
        this.camera = new Camera(new Vector2f(-200, -300));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        // load texture image
        this.testTexture = new Texture("assets/images/Y.jpg");

        /* -----------------------------------------------------------
           Generate VAO, VBO, and EBO buffer objects, and send to GPU
           -----------------------------------------------------------*/
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // create VBO upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        // enable attribute pointer for UV Coordinates
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();

        // Upload Texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();         // push texture into slot

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        //Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
}
