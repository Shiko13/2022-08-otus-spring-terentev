package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.spring.dto.CommentDto;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MutableAclService mutableAclService;
    @Override
    public CommentDto setPermission(CommentDto commentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(commentDto.getClass(), commentDto.getId());

        String adminPrincipal = "admin";
        Sid admin = new PrincipalSid(adminPrincipal);
        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.setOwner(owner);
        CumulativePermission cumulativePermissionForAdmin = new CumulativePermission();
        cumulativePermissionForAdmin.set(BasePermission.ADMINISTRATION);
        cumulativePermissionForAdmin.set(BasePermission.CREATE);
        cumulativePermissionForAdmin.set(BasePermission.DELETE);
        cumulativePermissionForAdmin.set(BasePermission.READ);
        cumulativePermissionForAdmin.set(BasePermission.WRITE);
        acl.insertAce(acl.getEntries().size(), cumulativePermissionForAdmin, admin, true);

        if (!adminPrincipal.equals(authentication.getName())) {
            CumulativePermission cumulativePermissionForOwner = new CumulativePermission();
            cumulativePermissionForOwner.set(BasePermission.CREATE);
            cumulativePermissionForOwner.set(BasePermission.DELETE);
            cumulativePermissionForOwner.set(BasePermission.READ);
            cumulativePermissionForOwner.set(BasePermission.WRITE);
            acl.insertAce(acl.getEntries().size(), cumulativePermissionForOwner, owner, true);
        }
        mutableAclService.updateAcl(acl);

        return commentDto;
    }
}
