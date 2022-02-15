import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Lyna Jiang
 */
class ECHashStringSet<Array> implements StringSet {


    public ECHashStringSet(int bucket_size) {
        _size = 0;
        default_num_buckets = bucket_size;
        buckets = (LinkedList<String>[]) new LinkedList[default_num_buckets];
        for (int i = 0; i < default_num_buckets; i++) {
            buckets[i] = new LinkedList<String>();
        }

    }

    private int default_num_buckets;

    public ECHashStringSet() {
        this((int) (1/MIN_LOAD));


    }

    @Override
    public void put(String s) {
        _size += 1;
        if (s != null) {
            if (_size > (int) (buckets.length * MAX_LOAD)) {
                resize();
            }
            int hashcode = whichBucket(s);
            if (!buckets[hashcode].contains(s)) {
                buckets[hashcode].add(s);
            }

        }
        //if we've reached the load limit, we should resize
        //figure out which bucket to o to
        //get linked list at bucket[i]
        //add s to that linked list
    }

    @Override
    public boolean contains(String s) {
        //figure out which index i bucket S would be in
        //get the linked list at buckets[i]
        //find out if that linked list contains S
        int hashcode = whichBucket(s);
        for (int i = 0; i < buckets[hashcode].size(); i++) {
            if (buckets[hashcode].get(i).equals(s)) {
                return true;
            }
        }
        return false;
    }

    private int whichBucket(String s) {
        //returns which bucket i the string s should be in
        //call the default string hashcode, as in s.hasCode()
        //figure out a way to make that default hashcode wrap or fit

        return (s.hashCode() & 0x7fffffff) % buckets.length;


    }


    public void resize() {
        int newLoadCount = _size * (int) MAX_LOAD;
        ECHashStringSet newBuckets = new ECHashStringSet(newLoadCount);

        for (int i = 0; i < buckets.length; i++) {
            for (String s : buckets[i]) {
                newBuckets.put(s);
            }
        }

        buckets = newBuckets.buckets;

    }

    @Override
    public List<String> asList() {
        ArrayList allStrings = new ArrayList();
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (int j = 0; j < buckets[i].size(); j++) {
                    allStrings.add(buckets[i].get(j));
                }
            }

        }
        return allStrings;
    }



    private int _size;
    private static double MIN_LOAD = 0.2;
    private static double MAX_LOAD = 5;
    private LinkedList<String>[] buckets;

}
