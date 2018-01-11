package GettingStarted;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Author : Thomas
 * Date : 09/01/2018
 * Project : Java-OpenGL
 */

public class Main
{
    public static void main(String[] args)
    {

         String vShader = "#version 330 core\n" +
                        "layout (location = 0) in vec3 aPos;\n" +
                        "void main()\n" +
                        "{\n" +
                            "gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
                        "}";

         String fShader = "#version 330 core\n" +
                        "out vec4 FragColor;\n" +
                        "void main()\n" +
                        "{\n" +
                            "FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                        "}";

        // Code minimal pour initialiser GLFW
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // Nécessaire pour initialiser sous MacOS

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

        //On génère notre vertex shader
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
        glDeleteShader(fragmentShader);

        //Données composant les données sur nos vertexes
        float[] verts = {
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f
        };

        //On génère un VBO
        int VBO = glGenBuffers();
        //On génère un VAO de cette manière
        int VAO = glGenVertexArrays();

        // On bind notre VAO
        glBindVertexArray(VAO);

        //On bind notre buffer
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        //On copie les données de nos vertexes dans notre buffer
        glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);

        //Nous renseignons OpenGL sur la manière dont il doit interprêter nos vertexes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        // On garde notre fenêtre ouverte tant qu'on a pas terminé avec notre programme
        while(!glfwWindowShouldClose(window))
        {
            processInput(window);
            // On vide le buffer de couleur à chaque boucle de rendue
            glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);// NB : il faut clear avant de dessiner !

            //A présent, nous pouvons unitiliser notre programme de shader dans OpenGL
            glUseProgram(shaderProgram);
            glBindVertexArray(VAO);
            //Nous pouvons enfin dessiner notre forme primitive !
            glDrawArrays(GL_TRIANGLES, 0, 3);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        //On supprime nos ressources car on en a plus besoin
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);

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
