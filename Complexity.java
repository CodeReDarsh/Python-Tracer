/**
 * The <code>Complexity</code> class constructs and contains all the methods
 * required to manipulate <code>Complexity</code> type objects. It represents the Big-Oh complexity of some block of code.
 * @author CodeReDarsh
 * <br>email: adarshcp2077@gmail.com
 */
public class Complexity {
    private int nPower; //the power of the N base
    private int logPower;   //the power of the log N base

    /**
     * This is the default constructor of the <code>Complexity</code> class.
     * Creates a complexity object of O(1) by default.
     */
    public Complexity(){
        nPower = 0;
        logPower = 0;
    }

    /**
     * Creates a complexity object containing the powers of the N and log N bases
     * @param nPower
     *  The power of the N base
     * @param logPower
     * The power of the log N base
     */
    public Complexity(int nPower, int logPower){
        this.nPower = nPower;
        this.logPower = logPower;
    }

    /**
     * Returns the power of the N base for a complexity object
     * @return the power of the N base
     */
    public int getNPower() {
        return nPower;
    }

    /**
     * Sets the power of the N base in a complexity object
     * @param nPower
     *  the power of the N base in a complexity object
     */
    public void setNPower(int nPower) {
        this.nPower = nPower;
    }

    /**
     * Returns the power of the log(N) base for a complexity object
     * @return the power of the log(N) base
     */
    public int getLogPower() {
        return logPower;
    }

    /**
     * Sets the power of the log(N) base in a complexity object
     * @param logPower the power of the log(N) base
     */
    public void setLogPower(int logPower) {
        this.logPower = logPower;
    }

    /**
     * Returns a human-readable string representation of the complexity object in terms of Big O notation.
     * @return the big O time complexity of the code block
     */
    @Override
    public String toString() {
        String complexity = "";
        String nPowers = "";
        String logPowers = "";

        if (nPower != 1)
            nPowers = "^" + nPower;
        if (logPower != 1)
            logPowers = "^" + logPower;

        if (nPower >= 1){
            complexity = "O(n" + nPowers;
            if (logPower >= 1){
                complexity += " * (log n)" + logPowers + ").";
                return complexity;
            }
        }
        if (logPower >= 1)
            complexity = "O((log n)" + logPowers;

        if (nPower == 0 && logPower == 0)
            complexity = "O(1";

        complexity += ").";
        return complexity;
    }
}
