package prosense.sassa.srdeft.userdetail.control;

import com.google.common.collect.Sets;

import prosense.sassa.srdeft.userdetail.entity.UserDetail;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;


@ApplicationScoped
public class UserDetailStore {
    @Inject
    EntityManager em;

    public Set<UserDetail> search(UserDetail userDetail) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserDetail> query = builder.createQuery(UserDetail.class);
        Root<UserDetail> root = query.from(query.getResultType());
        query.select(root);
        Stream<UserDetail> userDetailStream = Sets.newLinkedHashSet(em.createQuery(query).getResultList()).stream();
        return userDetailStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
