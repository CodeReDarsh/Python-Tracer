/**
 * The <code>CodeBlock</code> class constructs and contains all the methods
 * required to manipulate <code>CodeBlock</code> type objects. It describes a nested block of code.
 * @author CodeReDarsh
 * <br>email: adarshcp2077@gmail.com
 */
public class CodeBlock {
    private Complexity blockComplexity; //the overall complexity of the code block. By default, it is set as O(1)
    private Complexity highestSubComplexity;    //the highest complexity of the statements contained within the code block.
                                                // By default, it is set as O(1)
    private String name;    //the indentation number of the code block, e.g. 1.1.2, 1.1, 3.2.2 etc.
                            // used to keep track of the nested structure
    private String loopVariable;    //only used for <code>while</code> blocks to keep track of the variable it is iterating over.
                                    // Set as null by default otherwise.

    /**
     * The default constructor of the <code>CodeBlock</code> class
     * Creates a code block with blockComplexity and highestSubComplexity as O(1) and name and loopVariable set to null
     */
    public CodeBlock (){
        blockComplexity = new Complexity();
        highestSubComplexity = new Complexity();
        name = null;
        loopVariable = null;
    }

    /**
     * Returns a reference to the block complexity of the code block
     * @return a reference to the block complexity object
     */
    public Complexity getBlockComplexity() {
        return blockComplexity;
    }

    /**
     * Sets the overall block complexity of a given code block to the one specified
     * @param blockComplexity the overall complexity of the code block
     */
    public void setBlockComplexity(Complexity blockComplexity) {
        this.blockComplexity = blockComplexity;
    }

    /**
     * Returns a reference to the highest complexity of the statements nested inside the code block
     * @return a reference to the highest complexity of the statements contained within the code block.
     */
    public Complexity getHighestSubComplexity() {
        return highestSubComplexity;
    }

    /**
     * Sets the overall complexity of the statements contained within the code block to the one specified
     * @param highestSubComplexity the highest complexity of the statements contained within the code block.
     */
    public void setHighestSubComplexity(Complexity highestSubComplexity) {
        this.highestSubComplexity = highestSubComplexity;
    }

    /**
     * Returns a string containing the indentation number of the code block, e.g. 1.1.2, 1.1, 3.2.2 etc. used to keep
     * track of the nested structure
     * @return the indentation number of the code block, e.g. 1.1.2, 1.1, 3.2.2 etc. used to keep track of the nested structure
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the indentation number of the code block to the one specified
     * @param name the indentation number of the code block, e.g. 1.1.2, 1.1, 3.2.2 etc. used to keep track of the nested structure
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string containing the variable a while block iterates over
     * @return the variable a <code>while</code> is iterating over
     */
    public String getLoopVariable() {
        return loopVariable;
    }

    /**
     * Sets the loop variable to the one specified
     * @param loopVariable A string containing the variable a <code>while</code> block iterates over
     */
    public void setLoopVariable(String loopVariable) {
        this.loopVariable = loopVariable;
    }
}
