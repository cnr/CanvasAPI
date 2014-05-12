package com.eueln.canvasapi.impl.util;

import com.eueln.canvasapi.impl.map.MapCanvasGraphics;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/*
 * Credit for the linear algebra bits goes to ase34
 */
public class MathUtil {
    public static double determinant(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[0][1] * matrix[1][2] * matrix[2][0] + matrix[0][2] * matrix[1][0] * matrix[2][1] - matrix[0][2] * matrix[1][1] * matrix[2][0] - matrix[0][1] * matrix[1][0] * matrix[2][2] - matrix[0][0] * matrix[1][2] * matrix[2][1];
    }

    public static Vector getIntersection(Vector linePoint, Vector lineDirection, Vector planeOrigin, Vector planeXDirection, Vector planeYDirection) {
        double[][] coefficients = {
                {lineDirection.getX(), -planeXDirection.getX(), -planeYDirection.getX()},
                {lineDirection.getY(), -planeXDirection.getY(), -planeYDirection.getY()},
                {lineDirection.getZ(), -planeXDirection.getZ(), -planeYDirection.getZ()}
        };

        double[] solutions = {
                planeOrigin.getX() - linePoint.getX(),
                planeOrigin.getY() - linePoint.getY(),
                planeOrigin.getZ() - linePoint.getZ()
        };

        double[][] dMatrix = {
                {solutions[0], coefficients[0][1], coefficients[0][2]},
                {solutions[1], coefficients[1][1], coefficients[1][2]},
                {solutions[2], coefficients[2][1], coefficients[2][2]}
        };
        double[][] uMatrix = {
                {coefficients[0][0], solutions[0], coefficients[0][2]},
                {coefficients[1][0], solutions[1], coefficients[1][2]},
                {coefficients[2][0], solutions[2], coefficients[2][2]}
        };
        double[][] vMatrix = {
                {coefficients[0][0], coefficients[0][1], solutions[0]},
                {coefficients[1][0], coefficients[1][1], solutions[1]},
                {coefficients[2][0], coefficients[2][1], solutions[2]}
        };

        double det = determinant(coefficients);

        double d = determinant(dMatrix) / det;
        double u = determinant(uMatrix) / det;
        double v = determinant(vMatrix) / det;

        return new Vector(u, v, 0);
    }

    public static Vector getTouchedPoint(Player player, MapCanvasGraphics graphics) {
        Vector planeOrigin = getOrigin(graphics.getLocation(), graphics.getBlockFace());
        Vector planeXDirection = getXDirection(graphics.getPositiveXFace());
        Vector planeYDirection = new Vector(0, -1, 0);

        return getIntersection(player.getEyeLocation().toVector(), player.getLocation().getDirection(), planeOrigin, planeXDirection, planeYDirection);
    }

    private static Vector getOrigin(Location loc, BlockFace face) {
        return loc.add(0, 1, 0).add(getOffset(face)).toVector();
    }

    private static Vector getOffset(BlockFace face) {
        switch (face) {
            case WEST:
                return new Vector(0, 0, 0);

            case SOUTH:
                return new Vector(0, 0, 1);

            case EAST:
                return new Vector(1, 0, 1);

            case NORTH:
                return new Vector(1, 0, 0);

            default:
                throw new IllegalArgumentException();
        }
    }

    private static Vector getXDirection(BlockFace face) {
        return new Vector(face.getModX(), 0, face.getModZ());
    }
}
