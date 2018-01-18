package maths;


/**
 * Author : Thomas
 * Date : 18/01/2018
 * Project : Java-OpenGL
 */

public class Vector4f
{
    private float x, y, z, w; // Une meilleure méthode consisterait à stocker les coordonnées dans un tableau

    //----|Constructors|----//
    public Vector4f()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public Vector4f(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    //----|Methods|----//

    //Opérations scalaires
    public Vector4f add(float s)
    {
        return new Vector4f(this.x + s, this.y + s, this.z + s, this.w + s);
    }

    public Vector4f sub(float s)
    {
        return new Vector4f(this.x - s, this.y - s, this.z - s, this.w - s);
    }

    public Vector4f mul(float s)
    {
        return new Vector4f(this.x * s, this.y * s, this.z * s, this.w * s);
    }

    public Vector4f div(float s)
    {
        if(s != 0)
            return new Vector4f(this.x / s, this.y / s, this.z / s, this.w / s);
        else
            return this; //On peut afficher un message d'erreur ici
    }

    //Opération entre vecteurs
    public Vector4f add(Vector4f o)
    {
        return new Vector4f(this.x + o.x, this.y + o.y, this.z + o.z, this.w + o.w);
    }

    public Vector4f sub(Vector4f o)
    {
        return new Vector4f(this.x - o.x, this.y - o.y, this.z - o.z, this.w - o.w);
    }

    public float dot(Vector4f o)
    {
        return this.x * o.x + this.y * o.y + this.z * o.z + this.w * o.w;
    }

    //Fonction basique sur les vecteurs
    public Vector4f negate()
    {
        return this.mul(-1);
    }

    public float squareLength()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float length()
    {
        return (float)Math.sqrt(this.squareLength());
    }

    public String toString()
    {
        return "Vector4f (" + this.x + ", " + this.y + ", " + this.z + ", " + this.w +")";
    }

    //----|Getters and Setters|----//
    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public float getZ(){return this.z;}
    public float getW(){return this.w;}

    public void setX(float x){this.x = x;}
    public void setY(float y){this.y = y;}
    public void setZ(float z){this.z = z;}
    public void setW(float w){this.w = w;}

    public float get(int index)
    {
        switch(index)
        {
            case 0: return getX();
            case 1: return getY();
            case 2: return getZ();
            case 3: return  getW();
            default: return -1;
        }
    }

    public void set(int index, float value)
    {
        switch(index)
        {
            case 0: setX(value);
            break;
            case 1: setY(value);
            break;
            case 2: setZ(value);
            break;
            case 3: setW(value);
            break;
            default: set(0, -1);
            break;
        }
    }
}
