package prosense.sassa.srdeft.userdetail.boundary;

import prosense.sassa.srdeft.userdetail.entity.UserDetail;
import prosense.sassa.srdeft.userdetail.control.UserDetailStore;

import javax.ejb.Stateless;

import javax.inject.Inject;

import java.util.Set;


@Stateless
public class UserDetailsResource {
    @Inject
    UserDetailStore userDetailStore;

    public Set<UserDetail> searchAll() {
        return userDetailStore.search(UserDetail.builder().build());
    }
}

