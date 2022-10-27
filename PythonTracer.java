/**
 * The <code>PythonTracer</code> class contains the main driver method for the program along with some helper methods.
 * @author CodeReDarsh
 * <br>email: adarshcp2077@gmail.com
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;

public class PythonTracer {
    static final int SPACE_COUNT = 4;   //used to determine the indentation of each statement
    private static int stackSize = 0;   //counter used to keep track of stack size
    private static boolean runCondition = true; //The variable in control of terminating the program

    /**
     * The driver method of the program
     * @param args
     *  The arguments to be passed into the main method
     */
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        while(runCondition){
            Complexity finalComplexity = null;
            System.out.println("Please enter a file name (or 'quit' to quit):");
            String filename = stdin.nextLine();
            try {
                finalComplexity = traceFile(filename);
            } catch (FileNotFoundException ex){
                System.out.println("Invalid file name, File could not be found...");
                continue;
            } catch (IOException e) {
                System.out.println("Error, occurred while reading file, stopping execution...");
            }
            if (finalComplexity != null)
                System.out.println("\nOverall complexity of " + filename + ": " + finalComplexity);
            System.out.println();
        }

        System.out.println("Program terminating successfully...");
        stdin.close();
    }

    /**
     * This method opens the indicated file, trace through the code of the Python function contained within the file, and
     * output the details of the trace and the overall complexity to the console.
     * @param filename Name or absolute path of the file to be opened
     * @return
     *  The overall complexity of the python function
     * @throws IOException
     *  When the entered filename is invalid or when the file could not be found
     */
    public static Complexity traceFile(String filename) throws IOException {

        //Initializing stack to an empty stack of Codeblocks
        Stack<CodeBlock> stack = new Stack<>();

        if((filename.equals("quit"))) {
            runCondition = false;
            return null;
        }

        FileInputStream fis = new FileInputStream(filename);    //Opening file using filename
        InputStreamReader inStream = new InputStreamReader(fis);
        BufferedReader reader = new BufferedReader(inStream);
        String line;    //line contains the code from a line of the file in the form of a string
        ArrayList<Integer> intArr = new ArrayList<>();  //ArrayList storing the number of code blocks currently made at a particular indent
        intArr.add(0);

        while((line = reader.readLine()) != null){
            if(!line.isBlank() && !line.trim().startsWith("#")){
                int indents = line.indexOf(line.trim()) / SPACE_COUNT;  //stores number of indents in line
                while(indents < stackSize){ //loop to find the total sub complexity of outermost code block
                    CodeBlock oldTop = blockComplexityTotal(stack);
                    if (indents != 0){
                        if (complexityIsGreater(oldTop.getBlockComplexity(), stack.peek().getHighestSubComplexity())){
                            CodeBlock stackTop = stack.pop();
                            stackTop.setHighestSubComplexity(oldTop.getBlockComplexity());
                            System.out.println("\nLeaving block " + oldTop.getName() + ", updating block " +
                                    stackTop.getName() + ":");
                            System.out.println("\tBlock " + stackTop.getName() + ":\tBlock complexity: " +
                                    stackTop.getBlockComplexity() + " \tHighest sub complexity: "
                                    + stackTop.getHighestSubComplexity());
                            stack.push(stackTop);
                        }
                    } else {
                        reader.close();
                        CodeBlock stackTop = blockComplexityTotal(stack);
                        System.out.println("\nLeaving block " + oldTop.getName() + ", nothing to update.");
                        System.out.println("\tBlock " + stackTop.getName() + ":\tBlock complexity: " +
                                stackTop.getBlockComplexity() + " \tHighest sub complexity: "
                                + stackTop.getHighestSubComplexity());
                        return stackTop.getBlockComplexity();
                    }
                }
                String keyword = "";
                for (BlockTypes b:
                     BlockTypes.values()) {
                    if (line.trim().startsWith(b.str + " ")){
                        keyword = b.str;
                        break;
                    }
                }

                if (!keyword.isEmpty() && line.trim().contains(keyword)){
                    codeBlockCreator(stack, line, intArr, indents, keyword);
                } else {
                    CodeBlock currentStackTop = stack.peek();
                    boolean isWhileBlock = currentStackTop.getLoopVariable() != null;
                    boolean lineUpdatesLoopVar = line.trim().contains(currentStackTop.getLoopVariable() + " -=")
                    || line.trim().contains(currentStackTop.getLoopVariable() + " /=");
                    if (isWhileBlock && lineUpdatesLoopVar){
                        CodeBlock whileBlock = stack.pop();
                        if (line.trim().contains(" -=")){
                            whileBlock.setBlockComplexity(new Complexity(1, 0));
                        } else
                            whileBlock.setBlockComplexity(new Complexity(0, 1));
                        System.out.println("\nFound update statement, updating block " + whileBlock.getName() + ": ");
                        System.out.println("\tBlock " + whileBlock.getName() + ":\tBlock complexity: " + whileBlock.getBlockComplexity()
                                + " \tHighest sub complexity: " + whileBlock.getHighestSubComplexity());
                        stack.push(whileBlock);
                    }
                }
            }
        }

        while (stackSize > 1){
            CodeBlock oldTop = blockComplexityTotal(stack);
            if (complexityIsGreater(oldTop.getBlockComplexity(), stack.peek().getHighestSubComplexity())){  //checks if oldTop's complexity is higher than sub complexity of block from stack
                CodeBlock stackTop = stack.pop();
                stackTop.setHighestSubComplexity(oldTop.getBlockComplexity());
                stack.push(stackTop);
            }
        }
        CodeBlock last = stack.pop();
        stackSize--;
        System.out.println("\nLeaving block " + last.getName());
        return last.getHighestSubComplexity();
    }

    /**
     * A helper method to traceFile method used to create <code>CodeBlock</code> objects of certain types of codeblocks
     * @param stack
     *  The stack used to store the code block objects representing their nested structure
     * @param line
     *  The line which the program is currently reading data from
     * @param intArr
     *  The integer array list used to help store and update the names of the indent numbers of the code blocks
     * @param indents
     *  The number of indents in the current line
     * @param keyword
     *  The specific keyword used to create a code block in the python function can be one of the following:
     *  "def", "for", "while", "if", "elif", "else".
     */
    private static void codeBlockCreator(Stack<CodeBlock> stack, String line, ArrayList<Integer> intArr, int indents, String keyword) {
        if (keyword.equals(BlockTypes.FOR.str) && line.trim().endsWith(":")){   // creates code block for "for" loop
            Complexity newBlockComplexity;
            if (line.trim().endsWith("log_N:"))
                newBlockComplexity = new Complexity(0, 1);
            else
                newBlockComplexity = new Complexity(1, 0);
            CodeBlock newCodeBlock = new CodeBlock();
            newCodeBlock.setBlockComplexity(newBlockComplexity);
            named(stack, intArr, indents, newCodeBlock, keyword);
        }
        else if (keyword.equals(BlockTypes.WHILE.str) && line.trim().endsWith(":")) {   //creates code block for "while" loop
            String loopVariable;
            loopVariable = line.substring(line.indexOf(" >") - 1, line.indexOf(" >"));
            CodeBlock newCodeBlock = new CodeBlock();
            newCodeBlock.setLoopVariable(loopVariable);
            named(stack, intArr, indents, newCodeBlock, keyword);
        } else { // creates code block for everything else
            CodeBlock newCodeBlock = new CodeBlock();
            named(stack, intArr, indents, newCodeBlock, keyword);
        }
    }

    /**
     * A helper method used to calculate the overall complexity of a code block from its block complexity and highest
     * sub complexity
     * @param stack
     *  The stack used to store the code block objects representing their nested structure
     * @return The code block object with the total complexity of the code block
     */
    private static CodeBlock blockComplexityTotal(Stack<CodeBlock> stack) {
        CodeBlock block = stack.pop();
        stackSize--;
        int blockSubNPower = block.getHighestSubComplexity().getNPower();
        int blockSubLogPower = block.getHighestSubComplexity().getLogPower();
        int blockNPower = block.getBlockComplexity().getNPower();
        int blockLogPower = block.getBlockComplexity().getLogPower();
        block.setBlockComplexity(new Complexity(blockSubNPower + blockNPower,
                blockSubLogPower + blockLogPower));
        return block;
    }

    /**
     * A helper method used to name the code block's indentation numbers
     * @param stack
     *  The stack used to store the code block objects representing their nested structure
     * @param intArr
     *  The integer array list used to help store and update the names of the indent numbers of the code blocks
     * @param indents
     *  The number of indents in the current line
     * @param newCodeBlock
     *  The code block who name is to be set
     * @param keyword
     *  The specific keyword used to create a code block in the python function can be one of the following:
     *  "def", "for", "while", "if", "elif", "else".
     */
    private static void named(Stack<CodeBlock> stack, ArrayList<Integer> intArr, int indents, CodeBlock newCodeBlock,
                              String keyword) {
        if (intArr.size() <= indents)
            intArr.add(0);
        if(stackSize != 0){
            String stackTopName = stack.peek().getName();
            newCodeBlock.setName(stackTopName + "." + (intArr.get(indents) + 1));
            intArr.set(indents, intArr.get(indents) + 1);
        } else {
            newCodeBlock.setName("" + (intArr.get(indents) + 1));
            intArr.set(indents, intArr.get(indents) + 1);
        }
        System.out.println("\nEntering block " + newCodeBlock.getName() + " '" + keyword + "':");
        System.out.println("\tBlock " + newCodeBlock.getName() + ":\tBlock complexity: " + newCodeBlock.getBlockComplexity()
        + "\t Highest sub complexity: " + newCodeBlock.getHighestSubComplexity());
        stack.push(newCodeBlock);
        stackSize++;
    }

    /**
     * A helper method used to compare the complexities of 2 code blocks
     * @param mainBlock
     *  The block complexity of the popped code block
     * @param stackBlockHighestComplexity
     *  The highest sub complexity of the code block at the top of the stack
     * @return A boolean value indicating whether the complexity is greater or not
     */
    private static boolean complexityIsGreater(Complexity mainBlock, Complexity stackBlockHighestComplexity){
        if (mainBlock.getNPower() > stackBlockHighestComplexity.getNPower())
            return true;
        if (mainBlock.getNPower() == stackBlockHighestComplexity.getNPower()){
            return mainBlock.getLogPower() > stackBlockHighestComplexity.getLogPower();
        }
        return false;
    }
}


