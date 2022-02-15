public class HW0 {

    public static void main(String[] args) {

    }
    public static int max(int[] a){
        int maximum = a[0];

        for(int i = 1; i < a.length; i++) {

            if (maximum < a[i]) {
                maximum = a[i];
            }
        }
        return maximum;

    }

    public static boolean threeSum(int[] a) {
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
            for (int j = 0; j < a.length; j++) {
                sum += a[j];
                for (int k = 0; k < a.length; k++) {
                    sum += a[k];
                    if (sum == 0) {
                        return true;
                    }

                    sum -= a[k];

                }
                sum -= a[j];


            }
            sum = 0;
        }
        return false;
    }


    public static boolean threeSumDistinct(int[] a) {
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
            for (int j = 1; j < a.length; j++) {
                sum += a[j];
                for (int k = 2; k < a.length; k++) {
                    sum += a[k];
                    if (sum == 0) {
                        return true;
                    }

                    sum -= a[k];

                }
                sum -= a[j];


            }
            sum = 0;
        }
        return false;
    }
}

