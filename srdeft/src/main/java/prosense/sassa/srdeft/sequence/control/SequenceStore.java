package prosense.sassa.srdeft.sequence.control;

import com.google.common.collect.Sets;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.sequence.entity.Sequence;
import prosense.sassa.srdeft.sequence.entity.Sequence_;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import java.util.Optional;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;


@ApplicationScoped
public class SequenceStore {
    @Inject
    EntityManager em;

    public Sequence read(final long id) {
        return Optional.ofNullable(em.find(Sequence.class, id)).orElseThrow(() -> AppException.builder().notFound404().message("sequence not found").build());
    }

    public Sequence update(final Sequence sequence) {
        em.merge(sequence);
        return Optional.of(sequence).orElseThrow(() -> AppException.builder().notFound404().message("sequence not found").build());
    }

    public Set<Sequence> search(Sequence sequence) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Sequence> query = builder.createQuery(Sequence.class);
        Root<Sequence> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
        if(sequence.getName() != null)
	        predicates.add(builder.equal(builder.lower(root.get(Sequence_.name)), sequence.getName().toLowerCase()));
        if(sequence.getSubService() != null)
            predicates.add(builder.equal(builder.lower(root.get(Sequence_.subService)), sequence.getSubService().toLowerCase()));
        if(sequence.getRegion() != null)
            predicates.add(builder.equal(builder.lower(root.get(Sequence_.region)), sequence.getRegion().toLowerCase()));
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        Stream<Sequence> sequenceStream = Sets.newLinkedHashSet(em.createQuery(query).getResultList()).stream();
        return sequenceStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
