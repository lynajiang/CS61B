
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author Lyna Jiang
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        set = new int[N + 1];
        for (int i = 1; i < set.length; i++) {
            set[i] = -1;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        if (set[v] < 0) {
            return v;
        }
        return find(set[v]);
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        if (samePartition(u, v)) {
            return find(u);
        }
        int rootOfU = find(u);
        int rootOfV = find(v);

        int bigger = (set[rootOfU] >= set[rootOfV])? rootOfV: rootOfU;
        int smaller = (set[rootOfU] >= set[rootOfV])? rootOfU: rootOfV;


        set[bigger] += set[smaller];
        set[smaller] = bigger;
        return bigger;


    }

    public int[] set;
}
