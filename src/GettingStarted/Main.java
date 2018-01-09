package GettingStarted;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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

        // On garde notre fenêtre ouverte tant qu'on a pas terminé avec notre programme
        while(!glfwWindowShouldClose(window))
        {
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // On ferme correctement notre fenêtre
        glfwTerminate();
    }
}
