package com.final_project;

public class SearchParameter {
    public boolean startChar;
    String prefix; // example: start
    String value; // example: 2014-01-15


    public SearchParameter(String prefix, String value) {
        this.prefix = prefix;
        this.value = value;
        this.startChar = false;
    }

    public String getString() {
        if (startChar) {
            return prefix;
        }
        return prefix + "=" + value;
    }

    public String getPrefix() {
        return prefix;
    }
}
