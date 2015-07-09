/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * The Software is provided "as is", without warranty of any kind, express or
 * implied, including but not limited to the warranties of merchantability,
 * fitness for a particular purpose and noninfringement. In no event shall the
 * authors or copyright holders X be liable for any claim, damages or other
 * liability, whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other dealings in the
 * Software.
 */
package org.geekygoblin.nedetlesmaki.core.systems;

import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Path;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import pythagoras.f.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geekygoblin.nedetlesmaki.core.backend.Position;

/**
 * @author potatoesmaster
 */
public class AStar {
    public static final int DISTANCE_BETWEEN_NODES = 1;

    private final LevelIndex index;

    private final Vector start;
    private final Vector goal;

    /**
     * The set of nodes left to be evaluated, along with their cost.
     */
    private Map<Vector, Double> open;

    /**
     * The set of nodes already evaluated, with their cost.
     */
    private Map<Vector, Double> closed;

    /**
     * The map of navigated nodes.<br>
     * Maps a node to his preceding node on the path.
     */
    private Map<Vector, Vector> cameFrom;

    private Path path;

    public AStar(LevelIndex index, Vector start, Vector goal) {
        this.index = index;
        this.start = start;
        this.goal = goal;
    }

    /**
     * Return the path, calculating it if needed.
     *
     * @return a path from the start to the goal, or null if a path can not be found
     */
    public Path getPath() {
        if (path == null) {
            calculatePath();
        }

        return path;
    }

    /**
     * Calculate the path from the starting node to the goal.
     */
    public void calculatePath() {
        closed = new HashMap<Vector, Double>();
        open = new HashMap<Vector, Double>();
        cameFrom = new HashMap<Vector, Vector>();

        // The set of tentative nodes to be evaluated, initially containing the start node
        // with a cost of zero
        open.put(start, .0);

        while (!open.isEmpty()) {
            Vector current = getNextBetterNode();

            if (posEquals(current, goal)) {
                // we are arrived at the goal, a path is found
                reconstructPath();
                return;
            }

            Double currentCost = open.get(current);
            open.remove(current);
            closed.put(current, currentCost);

            for (Vector neighbor : neighborNodes(current)) {
                double costSoFar = currentCost + DISTANCE_BETWEEN_NODES;

                // skip this neighbor if we have already seen his node with a lower cost (a better path to this node exists)
                if ((open.containsKey(neighbor) && open.get(neighbor) <= costSoFar) || (closed.containsKey(neighbor)) && closed.get(neighbor) <= costSoFar) {
                    continue;
                }

                closed.remove(neighbor);
                open.put(neighbor, costSoFar);
                cameFrom.put(neighbor, current);
            }
        }

        path = null;
    }

    /**
     * Return the guessed most promising node.
     *
     * @return the node which have not yet been evaluated with the lowest estimated total cost
     */
    private Vector getNextBetterNode() {
        // the result cannot be null, as 'open' should not be empty
        Vector better = null;

        double min = Double.MAX_VALUE;
        for (Map.Entry<Vector, Double> e : open.entrySet()) {
            final Double costSoFar = e.getValue();
            final Vector node = e.getKey();
            if (costSoFar + costEstimate(node) < min) {
                better = node;
                min = costSoFar + costEstimate(node);
            }
        }

        return better;
    }

    protected void reconstructPath() {
        Vector currentNode = goal;
        path = new Path();

        Vector previousNode;
        while ((previousNode = cameFrom.get(currentNode)) != null) {
            path.insertStep(currentNode);
            currentNode = previousNode;
        }
    }

    /**
     * @return an estimation of the distance between the given node and the goal
     */
    protected double costEstimate(Vector from) {
        return dist(goal.x, from.x) + dist(goal.y, from.y);
    }

    protected double dist(double a, double b) {
        return (a > b) ? a - b : b - a;
    }

    /**
     * @return the list of the node neighbors
     */
    private List<Vector> neighborNodes(Vector node) {
        List<Vector> neighbors = new ArrayList<Vector>();

        int x = Math.round(node.x);
        int y = Math.round(node.y);

        Vector up = getNode(x, y + 1);
        Vector down = getNode(x, y - 1);
        Vector left = getNode(x - 1, y);
        Vector right = getNode(x + 1, y);

        if (up != null) {
            neighbors.add(up);
        }

        if (down != null) {
            neighbors.add(down);
        }

        if (left != null) {
            neighbors.add(left);
        }

        if (right != null) {
            neighbors.add(right);
        }

        return neighbors;
    }

    private Vector getNode(int x, int y) {
        // if the cell is present in the level and is unoccupied
        if (index.positionIsVoid(new Position(y, x)))
        {
            return new Vector(x, y);
        }

        return null;
    }

    // needed because we use Vector as Vector2
    private boolean posEquals(Vector a, Vector b) {
        return a.x == b.x && a.y == b.y;
    }
}
