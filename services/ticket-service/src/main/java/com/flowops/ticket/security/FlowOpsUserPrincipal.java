package com.flowops.ticket.security;

import java.util.List;
import java.util.UUID;

public record FlowOpsUserPrincipal(UUID userId, String username, List<String> roles) {
}
