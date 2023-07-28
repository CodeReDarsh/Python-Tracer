# Python Tracer

A simple code tracer program which takes the name of a Python file containing a single function and outputs the Big-Oh 
order of complexity of that function. Supports evaluation of nested code blocks too.

## Input Code Restrictions

For the sake of simplicity some restrictions on the type of code to be evaluated have been made as following:

- Since Big-Oh notation can get quite messy (e.g. `log(n^(1/2) / n!)`), the possible orders have been restricted to powers
of two base types: `n` and `log_n`.


- Support for common language paradigms and keywords is limited to `while`, `if`, `else`, `else if`, `def` and `for`.


- Once a new block is declared (via a new def, for, while, if, else, or elif), all statements contained within the new 
block must be indented by exactly 4 whitespace characters, whether it be a space (" ") or a tab ("\t"). 
Once this new block ends, the following statements must have the exact same indentation as the previous statements in 
the block.


- All `for` blocks should be of the following variety, where * represents some variable name:


    for * in N               for * in log_N
                        or
        ....                         .... 

-  All `while` blocks will loop from n to 1 over some variable. The update statement should be located within the block 
and can be either of the following:

```
* = n                   * = n
while (* < 1)           while (* > 1)
                   or
     ....                   ....
     
     * -= 1                 * /= 2        
```
   

## Trace Algorithm

Pseudocode of the algorithm used for the traceFile method is given below:

    Initialize stack to an empty stack of CodeBlocks.
    Open file using filename.
    while file has lines
         line = next line in file.
        if line is not empty and line does not start with '#'
             indents = number of spaces in line / SPACES_COUNT.
            while indents is less than size of stack
                if indents is 0
                     Close file and return the total complexity of stack.top.
                else
                     oldTop = stack.pop()
                     oldTopComplexity = total complexity of oldTop
                    if oldTopComplexity is higher order than stack.top's highest sub-complexity
                         stack.top's highest sub-complexity = oldTopComplexity
            if line contains a keyword
                 keyword = keyword in line.
                if keyword is "for"
                    Determine the complexity at end of line ("N:" or "log_N:")
                    Create new O(n) or O(log(n)) CodeBlock and push onto stack.
                else if keyword is "while"
                     loopVariable = variable being updated (first token after "while").
                    Create new O(1) CodeBlock with loopVariable and push onto stack.
                else 
                    Create new O(1) CodeBlock and push onto the stack.
            else if stack.top is a "while" block and line updates stack.top's loopVariable 
                Update the blockComplexity of stack.top.
        else 
            Ignore line.
    while size of stack > 1
        oldTop = stack.pop()
        oldTopComplexity = total complexity of oldTop
        if oldTopComplexity is higher order than stack.top's highest sub-complexity
             stack.top's highest sub-complexity = oldTopComplexity
    Return stack.pop(). 
