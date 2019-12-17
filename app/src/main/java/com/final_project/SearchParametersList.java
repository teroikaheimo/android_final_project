package com.final_project;

import java.util.ArrayList;

public class SearchParametersList {
    private final String startOfLine = "?";
    private final String and = "&";
    private ArrayList<SearchParameter> searchParameters;

    public SearchParametersList() {
        this.searchParameters = new ArrayList<>();
        SearchParameter sp = new SearchParameter(startOfLine, "");
        sp.startChar = true;
        searchParameters.add(sp);
    }

    public String getSearchParameterString() {
        String searchString = "";
        boolean andIsNeeded = false;

        for (int i = 0; i < searchParameters.size(); i++) {
            SearchParameter sp = searchParameters.get(i);
            if (sp.startChar || sp.value.length() > 0) {
                if (andIsNeeded) {
                    searchString += and;
                }
                searchString += sp.getString();
                if (!sp.startChar) {
                    andIsNeeded = true;
                }
            }
        }
        return searchString;
    }

    public boolean updateSearchParameter(String prefix, String value) {
        boolean updated = false;
        for (SearchParameter sp : searchParameters) {
            if (sp.getPrefix().equals(prefix)) {
                int index = searchParameters.indexOf(sp);
                searchParameters.set(index, new SearchParameter(prefix, value));
                updated = true;
                break;
            }
        }
        return updated;
    }


    public void addSearchParameter(SearchParameter sp) {
        searchParameters.add(sp);
    }

}
