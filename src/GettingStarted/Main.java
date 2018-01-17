package GettingStarted;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * Author : Thomas
 * Date : 09/01/2018
 * Project : Java-OpenGL
 */

public class Main
{

    public static void main(String[] args)
    {
        // Code minimal pour initialiser GLFW
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // Nécessaire pour initialiser sous MacOS

        // utile pour renvoyer les error codes
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                System.out.println(error);
            }
        });

        // Code pour créer la fenêtre GLFW
        long window = glfwCreateWindow(800, 600, "Learn-OpenGL", NULL, NULL);

        if(window == NULL)
        {
            System.out.println("Error - Unable to create GLFW window!");
            glfwTerminate();
            return;
        }

        glfwMakeContextCurrent(window);

        //PRIMORDIAL : Permet d'initialiser OpenGL avec LWJGL
        GL.createCapabilities();

        // informe OpenGL sur la taille de notre vewport
        glViewport(0, 0, 800, 600);

        // Permet de redimentionner la fenêtre et le viewport d'OpenGL
        glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback()
        {
            @Override
            public void invoke(long window, int width, int height)
            {
                glViewport(0, 0, width, height);
            }
        });


        // LE SHADER EST GÉRÉ DANS SA PROPRE CLASS
        /*//On génère notre vertex shader
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        //On récupère le code source du shader
        glShaderSource(vertexShader, vShader);
        //On compile notre shader
        glCompileShader(vertexShader);

        //On récupère les informations de compilation pour savoir si notre shader fonctionne
        int success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        // On affiche les résultats s'il y a une erreure
        if(success != 1)
            System.out.println("ERROR::VERTEX_SHADER::COMPILATION_FAILED " + glGetShaderInfoLog(vertexShader));

        // On suit le même procésus pour générer notre fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fShader);
        glCompileShader(fragmentShader);

        success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if(success != 1)
            System.out.println("ERROR::FRAGMENT_SHADER::COMPILATION_FAILED " + glGetShaderInfoLog(fragmentShader));

        //On créer notre programme de shaders
        int shaderProgram = glCreateProgram();
        //Nous pouvons maintenant ajouter nos deux shaders au programme pour le linking
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        //On peut maintenant link notre programme de shader
        glLinkProgram(shaderProgram);

        //Nous pouvons également vérifier le status de notre programme
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success != 1)
            System.out.println("ERROR::SHADER_PROGRAM::LINKING_FAILED " + glGetProgramInfoLog(shaderProgram));

        //Une fois l'étape du linking passé, nous n'avons plus besoin des shaders
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);*/

        Shader shaderProgram = new Shader("shaders/vertex.vs", "shaders/fragment.fs");

        //Données composant les données sur nos vertexes
        float[] verts = {
                // positions        // couleurs
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, // bas droite
                 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, // bas gauche
                 0.0f,  0.5f, 0.0f, 0.0f, 0.0f, 1.0f  // haut
        };

        float[] recVerts = {
                // positions        // couleurs       // texCoords
                 0.5f,  0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // haut droit
                 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bas droit
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bas gauche
                -0.5f,  0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f  // haut gauche
        };

        int[] indices = {
                0, 1, 3, //Premier triangle
                1, 2, 3  //Second triangle
        };

        //Données concernant notre texture (doit être entre 0 et 1)
        /*float[] texCoords = {
                0.0f, 0.0f,     // bas gauche
                1.0f, 0.0f,     // bas droite
                0.5f, 1.0f      // haut
        };*/

        //On génère un VBO
        int VBO = glGenBuffers();
        //On génère un VAO de cette manière
        int VAO = glGenVertexArrays();
        //On génère un EBO
        int EBO = glGenBuffers();

        // On bind notre VAO
        glBindVertexArray(VAO);

        //On bind notre buffer
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        //On copie les données de nos vertexes dans notre buffer
        glBufferData(GL_ARRAY_BUFFER, recVerts, GL_STATIC_DRAW);

        // On s'occupe de notre EBO ici
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //Nous renseignons OpenGL sur la manière dont il doit interprêter nos vertexes
        //Gestion de la position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        //Gestion de la couleur
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*Float.BYTES, 3*Float.BYTES);
        glEnableVertexAttribArray(1);

        //Gestion de la texture
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*Float.BYTES, 6*Float.BYTES);
        glEnableVertexAttribArray(2);

        //On unbind nos buffers
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        //--------------------------------------------------------------------------------------------------------------
        //On créer notre texture OpenGL
        int texture = glGenTextures();

        // On bind notre texture
        glBindTexture(GL_TEXTURE_2D, texture);

        //On choisi le mode d'afficheage de notre texture si coordonnées différentes de [0,1]
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //Dans le cas où on veut afficher une couleur unie si les coordonnées ne sont pas dans [0,1]
        /*float[] borderColor = {1.0f, 1.0f, 0.0f, 1.0f};
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);*/

        //On définit comment openGL doit scale down et scale up une image
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //On créer les buffer dont on a besoin pour charger notre image
        MemoryStack stack = stackPush();

        IntBuffer w = stack.mallocInt(2);
        IntBuffer h = stack.mallocInt(2);
        IntBuffer comp = stack.mallocInt(2);

        stbi_set_flip_vertically_on_load(true);

        //On charge notre image
        ByteBuffer data = stbi_load("res/container.jpg", w, h, comp, 0);

        //Maintenant que la texture est bind on peut la générer et créer ses mipmaps
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(0), h.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, data); // en théorie il faut tester si data n'est pas NULL
        glGenerateMipmap(GL_TEXTURE_2D);

        //On libère la mémoire de notre image
        stbi_image_free(data);

        //----|On créer une nouvelle texture|----//

        int texture2 = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture2);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        data = stbi_load("res/awesomeface.jpg", w, h, comp, 0);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(), h.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(data);

        //On explique à OpenGL quelle texture appartient à quelle sampler
        shaderProgram.use();
        shaderProgram.setInt("texture1", 0);
        shaderProgram.setInt("texture2", 1);

        //--------------------------------------------------------------------------------------------------------------

        // On garde notre fenêtre ouverte tant qu'on a pas terminé avec notre programme
        while(!glfwWindowShouldClose(window))
        {
            processInput(window);
            // On vide le buffer de couleur à chaque boucle de rendue
            glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);// NB : il faut clear avant de dessiner !

            // On va générer une couleur et la passer à notre shader
            //double time = glfwGetTime();
            //float color = ((float)Math.sin(time) / 2.0f) + 0.5f;
            //int vertexColorLocation = glGetUniformLocation(shaderProgram, "ourColor");

            //A présent, nous pouvons unitiliser notre programme de shader dans OpenGL
            shaderProgram.use();

            //On passe notre couleur à notre programme de shader
            //glUniform4f(vertexColorLocation, 0.0f, color, 0.0f, 1.0f);

            //On active la texture que l'on souhaite utiliser
            glActiveTexture(GL_TEXTURE0);
            //On utilise notre texture
            glBindTexture(GL_TEXTURE_2D, texture);

            //On active notre deuxième texture
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture2);
            
            glBindVertexArray(VAO);
            //Nous pouvons enfin dessiner notre forme primitive !
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        //On supprime nos ressources car on en a plus besoin
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);

        // On ferme correctement notre fenêtre
        glfwTerminate();
    }

    // Permet de gérer les entrées clavier
    private static void processInput(long window)
    {
        if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(window, true);
    }
}
