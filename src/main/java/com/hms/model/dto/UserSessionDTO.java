package com.hms.model.dto;

import com.hms.model.enumtype.UserRole;
import lombok.Data;

@Data
public class UserSessionDTO {
    private Long accountId;
    private String username;
    private UserRole role;
    private String displayName; // Tên thật của BS hoặc BN
    private Long referenceId;   // ID của Doctor hoặc Patient tương ứng
}