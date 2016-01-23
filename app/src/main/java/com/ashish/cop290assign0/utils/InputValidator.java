package com.ashish.cop290assign0.utils;
import com.ashish.cop290assign0.config.Config;

/**
 * Utilities to check if input data is valid.
 * @author Arnav Kansal
 */
public class InputValidator {

    /**
     * Checks if the entered name is valid.
     * A name is considered to be valid if it is a string only of letters (from any language), spaces, . , ', - only.
     * source: <a href="http://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters">Stack Overflow answer</a>
     * @param inputName     the input name that needs to be tested
     * @return              <code>true</code> if the <code>inputName</code> is a valid name,
     *                      <code>false</code> otherwise.
     */
    public static boolean isValidName(String inputName){
        return inputName.matches(Config.REGEX_NAME);
    }

    /**
     * Checks if the entered entry number is structurally valid.
     * A structurally valid entry entry number is any string of format:
     *      2 0 \d \d  [a-zA-z] [a-zA-z] [a-zA-z\d] \d \d \d \d
     *          y1  y2 d1       d2       pc          i1 i2 i3 i4
     *
     * {y1,y2}          - Represent Entry Year (academic year of joining)
     * {d1,d2}          - Academic Unit Code (Department Code)
     * pc               - Program code. 1 for B.Tech, 5 for Dual, Z for Ph.D etc.
     * {i1,i2,i3,i4}    - Unique identification number for each student
     *
     * For example - Entry Number    : 2013EE10431
     *               Entry Year      : 13
     *               Department Code : EE
     *               Unique id       : 0431
     *
     * <b>Note</b> A structurally valid entry number need not necessarily be actually valid (i.e. associated to some student).
     *             But a structurally invalid entry number is definitely invalid.
     *
     * @param inputEntryNumber      the input entry number to be tested
     * @return                      <code>true</code> if the <code>inputEntryNumber</code> is structurally valid,
     *                              <code>false</code> otherwise.
     */
    public static boolean isValidEntryCodeStructure(String inputEntryNumber){
        return inputEntryNumber.matches(Config.REGEX_ENTRY_NUMBER);
    }
}