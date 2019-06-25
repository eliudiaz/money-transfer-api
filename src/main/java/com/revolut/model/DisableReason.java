package com.revolut.model;

public enum DisableReason {
    USER_SUSPENDED("User has suspended this account"),
    BLOCKED("Account blocked due to suspicious behavior"),
    HOLD("Account account is on-hold due to a review");

    public final String label;

    private DisableReason(String label){
        this.label=label;
    }
}
