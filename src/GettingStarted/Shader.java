package GettingStarted;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Author : Thomas
 * Date : 11/01/2018
 * Project : Java-OpenGL
 */

public class Shader {
    private int ID;

    public Shader(String vertPath, String fragPath)
    {
        try{
            //On récupère nos shaders sur le disque (méthode pour lire de petits fichiers)
            Path vPath = Paths.get(vertPath);
            Path fPath = Paths.get(fragPath);

            String vSource = "", fSource = "";

            for(String line:Files.readAllLines(vPath, StandardCharsets.UTF_8))
            {
                vSource += line + "\n";
            }

            for(String line:Files.readAllLines(fPath, StandardCharsets.UTF_8))
            {
                fSource += line + "\n";
            }


            //On peut maintenant compiler nos shaders
            int vertexShader, fragmentShader;
            int success;

            // Création de notre vertex shader
            vertexShader = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertexShader, vSource);
            glCompileShader(vertexShader);

            success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
            if(success != GL_TRUE)
                System.out.println("ERROR::VERTEX_SHADER::COMPILATION_FAILED " + glGetShaderInfoLog(vertexShader));

            // Création de notre frament shader
            fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragmentShader, fSource);
            glCompileShader(fragmentShader);

            success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
            if(success != GL_TRUE)
                System.out.println("ERROR::FRAGMENT_SHADER::COMPILATION_FAILED " + glGetShaderInfoLog(fragmentShader));

            //On s'occupe désormait de créer notre programme de shader
            ID = glCreateProgram();
            glAttachShader(ID, vertexShader);
            glAttachShader(ID, fragmentShader);
            glLinkProgram(ID);

            success = glGetProgrami(ID, GL_LINK_STATUS);
            if(success != GL_TRUE)
                System.out.println("ERROR::SHADER_PROGRAM::LINKING_FAILED " + glGetProgramInfoLog(ID));

            //On peut supprimer nos shaders
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);

        }catch(IOException e){
            System.out.println("ERROR::SHADER::FILES_NOT_CORRECTLY_READ " + e.toString());
        }
    }

    public void use()
    {
        glUseProgram(ID);
    }

    public void setInt(String name, int value)
    {
        glUniform1i(glGetUniformLocation(ID, name), value);
    }

    public void setFloat(String name, int value)
    {
        glUniform1f(glGetUniformLocation(ID, name), value);
    }
}
