package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing operations for the list of MostImportantThings objects
 */
public class MostImportantThings {

    /**
     * Rotates the list by `k`, this is useful for when we want to
     * shift the list up or down because we've moved it to a different area
     * @param mits list of mits to rotate by k
     * @param k rotation value
     * @return rotated list of mits
     */
    @NonNull
    public static List<MostImportantThing> rotate(List<MostImportantThing> mits, int k) {
        // Build mapping from old to new sort orders using Math.floorMod to wrap around.
        var newMits = new ArrayList<MostImportantThing>();
        for (int i = 0; i < mits.size(); i++) {
            var thisMit = mits.get(i);
            var thatMit = mits.get(Math.floorMod(i + k, mits.size()));
            newMits.add(thisMit.withSortOrder(thatMit.sortOrder()));
        }
        return newMits;
    }
}
