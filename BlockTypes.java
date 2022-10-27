/**
 * The <code>BlockTypes</code> enum is used to store some constant keywords
 * @author CodeReDarsh
 * <br>email: adarshcp2077@gmail.com
 */
public enum BlockTypes {

    DEF("def"),
    FOR("for"),
    WHILE("while"),
    IF("if"),
    ELIF("elif"),
    ELSE("else");

    final String str;

    /**
     * A helper method used to return the constant enum values in string form
     * @param str
     *  The string representation of the constant values
     */
    BlockTypes(String str) {
        this.str = str;
    }
}
