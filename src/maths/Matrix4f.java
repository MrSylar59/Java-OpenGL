package maths;

/**
 * Author : Thomas
 * Date : 18/01/2018
 * Project : Java-OpenGL
 */

public class Matrix4f
{
    private float[] matrix;

    //----|Constructor|----//
    public Matrix4f()
    {
        matrix = new float[4*4];

        for(int i = 0; i < matrix.length; i++)
        {
            matrix[i] = 0;
        }
    }

    //----|Methods|----//

    //Opérations scalaires
    public Matrix4f add(float s)
    {
        for(int i = 0; i < matrix.length; i++)
            matrix[i] += s;
        return this;
    }

    public Matrix4f sub(float s)
    {
        for(int i = 0; i < matrix.length; i++)
            matrix[i] -= s;
        return this;
    }

    public Matrix4f mul(float s)
    {
        for(int i = 0; i < matrix.length; i++)
            matrix[i] *= s;
        return this;
    }

    public Matrix4f div(float s)
    {
        if (s != 0) //Encore une fois, il est possible de renvoyer un message d'erreur
        {
            for(int i = 0; i < matrix.length; i++)
                matrix[i] /= s;
        }
        return this;
    }

    //Opérations entre matrices
    public Matrix4f add(Matrix4f o)
    {
        for(int i = 0; i < matrix.length; i++)
            matrix[i] += o.matrix[i];
        return this;
    }

    public Matrix4f sub(Matrix4f o)
    {
        for(int i = 0; i < matrix.length; i++)
            matrix[i] -= o.matrix[i];
        return this;
    }

    public Matrix4f mul(Matrix4f o)
    {
        Matrix4f result = new Matrix4f().identity();
        for(int row = 0; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                float sum = 0.0f;
                for(int e = 0; e < 4; e++)
                {
                    sum += matrix[col + e * 4] * o.matrix[e + row * 4];
                }
                result.matrix[col + row * 4] = sum;
            }
        }
        return result;
    }

    //Opérations entre matrice et vecteur
    public Vector4f mul(Vector4f v)
    {
        Vector4f result = new Vector4f();
        for(int col = 0; col < 4; col++)
        {
            float sum = 0;
            for(int row = 0; row < 4; row++)
            {
                sum += matrix[col + row * 4] * v.get(row);
            }
            result.set(col, sum);
        }
        return result;
    }

    //Fonctions basiques sur les matrices
    public Matrix4f identity()
    {
        matrix[0 + 0 * 4] = 1;
        matrix[1 + 1 * 4] = 1;
        matrix[2 + 2 * 4] = 1;
        matrix[3 + 3 * 4] = 1;
        return this;
    }

    public Matrix4f scale(float x, float y, float z)
    {
        Matrix4f result = new Matrix4f().identity();
        result.matrix[0 + 0 * 4] = x;
        result.matrix[1 + 1 * 4] = y;
        result.matrix[2 + 2 * 4] = z;
        return this.mul(result);
    }

    public Matrix4f translate(float x, float y, float z)
    {
        Matrix4f result = new Matrix4f().identity();
        result.matrix[0 + 3 * 4] = x;
        result.matrix[1 + 3 * 4] = y;
        result.matrix[2 + 3 * 4] = z;
        return this.mul(result);
    }

    //Ce type de rotation effectue une rotation d'Euler : risque de perte d'un degrès de liberté
    public Matrix4f rotate(float ang, Vector4f axis)
    {
        float rang = (float)Math.toRadians(ang);
        float cos = (float)Math.cos(rang);
        float sin = (float)Math.sin(rang);
        float omc = 1.0f - cos;

        float x = axis.getX();
        float y = axis.getY();
        float z = axis.getZ();

        Matrix4f result = new Matrix4f().identity();

        result.matrix[0 + 0 * 4] = cos + x*x*omc;
        result.matrix[0 + 1 * 4] = x*y*omc - z*sin;
        result.matrix[0 + 2 * 4] = x*z*omc + y*sin;

        result.matrix[1 + 0 * 4] = x*y*omc + z*sin;
        result.matrix[1 + 1 * 4] = cos + y*y*omc;
        result.matrix[1 + 2 * 4] = y*z*omc - x*sin;

        result.matrix[2 + 0 * 4] = z*x*omc + y*sin;
        result.matrix[2 + 1 * 4] = z*y*omc + x*sin;
        result.matrix[2 + 2 * 4] = cos + z*z*omc;

        /*System.out.println(this);
        System.out.println(result);
        System.out.println(this.mul(result));*/

        return this.mul(result);
    }

    public float[] toArray()
    {
        return matrix;
    }

    public String toString()
    {
        String str = "Matrix4f [";

        for(int raw = 0; raw < 4; raw++)
        {
            str += "[";
            for(int col = 0; col < 4; col++)
            {
                str += matrix[raw + col * 4];
                if(col < 3)
                        str += ", ";
            }
            str += "]";
        }
        str += "]";

        return str;
    }

    //----|Getters and Setters|----//
    public void set(int row, int col, float value)
    {
        matrix[row + col * 4] = value;
    }

    public float get(int row, int col)
    {
        return matrix[row + col * 4];
    }
}
