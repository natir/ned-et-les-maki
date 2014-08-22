/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.core.backend;

import java.util.ArrayList;
import java.util.Stack;

import javax.inject.Singleton;

import org.geekygoblin.nedetlesmaki.core.backend.Mouvement;

/**
 *
 * @author pierre
 */

@Singleton
public class MoveStory {

    private final Stack<ArrayList<Mouvement>> oldIndex;
    private ArrayList<Mouvement> remove;

    public MoveStory() {
        this.oldIndex = new Stack();
        this.remove = null;
    }

    public boolean addMouvement(ArrayList<Mouvement> vM) {
        return this.oldIndex.add(vM);
    }

    public void cleanStack() {
        this.oldIndex.clear();
    }

    public int sizeOfStack() {
        return this.oldIndex.size();
    }

    public ArrayList<Mouvement> getChangement() {
        if (!this.oldIndex.empty()) {
            ArrayList<Mouvement> o = this.oldIndex.peek();
            if (o != null) {
                return o;
            }
        }

        return null;
    }

    public ArrayList<Mouvement> pop() {
        if (this.oldIndex.isEmpty()) {
            return null;
        }

        return this.oldIndex.pop();
    }

    public ArrayList<Mouvement> peek() {
        if (this.oldIndex.isEmpty()) {
            return null;
        }

        return this.oldIndex.peek();
    }

    public void setRemove(ArrayList<Mouvement> set) {
        this.remove = set;
    }

    public ArrayList<Mouvement> getRemove() {
        return this.remove;
    }
}
