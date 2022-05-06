package com.tcs.edu.project_enum;

/**
 * Severity levels that can be used to decorate message strings.
 * {@link #MINOR}
 * {@link #REGULAR}
 * {@link #MAJOR}
 */
public enum Severity {
    MINOR ("()"),
    REGULAR ("(!)"),
    MAJOR ("(!!!)");

    /**
     * Message level
     */
    private final String level;

    /**
     * Constructor
     * @param level level value
     */
    Severity(String level) {
        this.level = level;
    }

    /**
     * @return level string value
     */
    public String getLevel() {
        return level;
    }
}
