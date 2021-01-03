package kimera.really.works.vamprism.common.util;

public final class MathUtils
{
    public static final float PI = (float) Math.PI;

    public static final double deg2Rad(double degrees)
    {
        return (degrees / 360) * (Math.PI * 2);
    }

    public static final float deg2Rad(float degrees)
    {
        return (degrees / 360) * (PI * 2);
    }

    public static final double rad2Deg(double radian)
    {
        return (radian / (Math.PI * 2)) * 360;
    }

    public static final float rad2Deg(float radian)
    {
        return (radian / (PI * 2)) * 360;
    }
}
