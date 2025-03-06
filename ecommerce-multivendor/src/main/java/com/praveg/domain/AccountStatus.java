package com.praveg.domain;

public enum AccountStatus {

    PENDING_VERIFICATION,   // Account is created but not verified
    ACTIVE,                 // Account is active and good standing
    SUSPENDED,              // Account is temporary suspended, possible due to violations
    DEACTIVATED,            // Account is deactivated, user have choose to deactivated it
    BANNED,                 // Account is permanently banned due to serve violations
    CLOSED                  // Account is permanently closed , possible at user request
}
