package com.example.amesingflank.rubixcube;

/**
 * Created by lbj on 2015/11/28.
 */
public class IntersectionChecker {
    private static float dotProduct(final Point3DFloat p, final Point3DFloat p2) {
        return p.getX() * p2.getX() + p.getY() * p2.getY() + p.getZ() * p2.getZ();
    }

    public static Point3DFloat crossProduct(final Point3DFloat p, final Point3DFloat p2) {
        return new Point3DFloat(p.getY() * p2.getZ() - p.getZ() * p2.getY(),
                p.getZ() * p2.getX() - p.getX() * p2.getZ(),
                p.getX() * p2.getY() - p.getY() * p2.getX());
    }

    public static float[] getresults(final Point3DFloat startP, final Point3DFloat endP,
                                                           final Point3DFloat vertex1, final Point3DFloat vertex2, final Point3DFloat vertex3) {

        final Point3DFloat direction =
                new Point3DFloat(endP.getX() - startP.getX(), endP.getY() - startP.getY(), endP.getZ() - startP.getZ());

        //Calculate the vector that represents the first side of the triangle.
        final Point3DFloat edge1 =
                new Point3DFloat(vertex3.getX() - vertex2.getX(), vertex3.getY() - vertex2.getY(), vertex3.getZ() - vertex2.getZ());

        //Calculate the vector that represents the second side of the triangle.
        final Point3DFloat edge2 =
                new Point3DFloat(vertex1.getX() - vertex2.getX(), vertex1.getY() - vertex2.getY(), vertex1.getZ() - vertex2.getZ());

        //Calculate a vector which is perpendicular to the vector between point 0 and point 1,
        //and the direction vector for the ray.
        final Point3DFloat directionCrossEdge2 = crossProduct(direction, edge2);

        //Calculate the dot product of the above vector and the vector between point 0 and point 2.
        final float determinant = dotProduct(edge1, directionCrossEdge2);

        //If the ray is (almost) parallel to the plane,
        //then the ray does not intersect the plane the triangle lies in.
        if (determinant > -0.00001 && determinant < 0.00001) {
            return new float[]{0,0,0};
        }

        final float inverseDeterminant = 1 / determinant;

        //Calculate a vector between the starting point of our ray, and the first point of the triangle,
        //which is at UV(0,0)
        final Point3DFloat distanceVector =
                new Point3DFloat(startP.getX() - vertex2.getX(), startP.getY() - vertex2.getY(), startP.getZ() - vertex2.getZ());

        //Calculate the U coordinate of the intersection point.
        final float triangleU = inverseDeterminant * dotProduct(distanceVector, directionCrossEdge2);

        //Is the U coordinate outside the range of values inside the triangle?
        if (triangleU < 0 || triangleU > 1) {
            //The ray has intersected the plane outside the triangle.
            return new float[]{0,0,0};
        }

        final Point3DFloat distanceCrossEdge1 = crossProduct(distanceVector, edge1);

        //Calculate the V coordinate of the intersection point.
        final float triangleV = inverseDeterminant * dotProduct(direction, distanceCrossEdge1);

        //Is the V coordinate outside the range of values inside the triangle?
        //Does U+V exceed 1.0?
        if (triangleV < 0 || triangleU + triangleV > 1) {
            //The ray has intersected the plane outside the triangle.
            return new float[]{0,0,0};
        }


        //The ray intersects the triangle!

        float[] ans=new float[]{1f,triangleU,triangleV};
        return ans;
    }
}
