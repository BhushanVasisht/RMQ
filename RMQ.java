/***
 * Class for a Hybrid implementation of the RMQ problem
 * Implements Hybrid-1 algorithm as described in the slides
 * @author Bhushan Vasisht BSV180000
 */

package bsv180000;

/***
 * Range Minimum Query Class
 * Implements efficient preprocessing and query for minimum element in a range
 * Implements the Hybrid-1 Algorithm <n, O(log(n))>
 */
public class RMQ {
    int[] elements;
    int numberOfBlocks;
    int blockSize;
    int[] blockMinima;
    int[][] dp;

    public RMQ(int [] input)
    {
        //level 1
        elements = input;
        preProcessArray(input);
    }

    /***
     * The Preprocessing work is done in this method.
     * Implementing preprocessing in O(nlog(n))
     * @param input - input array
     */
    public void preProcessArray(int[] input)
    {
        blockSize = (int)(Math.ceil(Math.log(input.length)));
        numberOfBlocks = input.length / blockSize;
        blockMinima = new int[numberOfBlocks];

        //level 2
        for(int i = 0; i < blockMinima.length; ++i)
        {
            int min = Integer.MAX_VALUE;
            for(int j = 0; j < blockSize; ++j)
            {
                min = Math.min(input[i*blockSize + j], min);
            }
            blockMinima[i] = min;
        }

        //top level
        int guessPower = 0;
        while(true)
        {
            int guess = (int)(Math.pow(2, guessPower));
            if(guess >= blockMinima.length) break;
            ++guessPower;
        }

        //fill sparse array with dp technique
        dp = new int[blockMinima.length][guessPower+1];

        for(int i = 0; i < dp.length; ++i)
            dp[i][0] = blockMinima[i];

        for(int j = 1; j < dp[0].length; ++j)
        {
            for(int i = 0; i < dp.length; ++i)
            {
                int min = Integer.MAX_VALUE;
                for(int k = (int)(Math.pow(2, j-1)) ; k < (int)(Math.pow(2, j)); ++k)
                {
                    if(i + k < blockMinima.length)
                        min = Math.min(min, blockMinima[i + k]);
                }

                dp[i][j] = Math.min(dp[i][j-1], min);
            }
        }
    }

    /**
     * Method to query the minimum element in a range
     * @param l - left most range
     * @param r - right most range
     * @return minimum element in the range
     */
    public int queryMin(int l, int r)
    {
        //should be done in log(n) time
        int k = 0;
        int range = r - l + 1;
        while(true)
        {
            int guess = (int)(Math.pow(2, k));
            if(guess > range)
            {
                --k;
                break;
            }
            ++k;
        }

        int index1 = (int)(Math.log(l + (int)(Math.pow(2,k)) - 1));
        int index2 = r - (int)(Math.pow(2,k)) + 1;

        //edge cases
        if(l >= dp.length) return Integer.MAX_VALUE;
        else if(r >= dp[0].length) return Integer.MAX_VALUE;
        else if(index1 >= dp[0].length) return Integer.MAX_VALUE;
        else if(index2 >= dp.length) return Integer.MAX_VALUE;

        //overlap interval 1
        int value1 = dp[l][index1];

        //overlap interval 2
        int value2 = dp[index2][(int)(Math.log(r) / Math.log(2))];

        return Math.min(value1, value2);
    }
}
