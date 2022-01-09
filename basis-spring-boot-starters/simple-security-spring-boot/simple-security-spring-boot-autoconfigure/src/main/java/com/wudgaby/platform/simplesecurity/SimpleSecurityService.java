package com.wudgaby.platform.simplesecurity;

import java.io.Serializable;
import java.util.List;

public interface SimpleSecurityService {
    List<String> getUserPermList(Serializable userId);
    List<String> getUserInfo();

    boolean hasPermission(List<String> requiredPermList);
    boolean hasRole(List<String> requiredRoleList);
}
