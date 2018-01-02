package com.digitalexperts.bookyachts.models;

/**
 * Created by CodianSoft on 13/12/2017.
 */

public class StateModel {
    private String stateName;
    private String stateId;

    public StateModel(String stateName, String stateId)
    {
        setStateName(stateName);
        setStateId(stateId);
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
