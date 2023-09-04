package prosense.sassa.srdeft.outcome.srd.control;

import com.google.common.collect.Sets;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.api.control.Property;
import prosense.sassa.srdeft.outcome.srd.entity.Outcome;
import prosense.sassa.srdeft.outcome.srd.entity.Outcome_;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;


@ApplicationScoped
public class OutcomeStore {
    @Inject
    @Property
    EntityManager emSRD;

    public Outcome read(final long id) {
        return Optional.ofNullable(emSRD.find(Outcome.class, id)).orElseThrow(() -> AppException.builder().notFound404().message("outcome not found").build());
    }

    public Outcome update(final Outcome outcome) {
        emSRD.merge(outcome);
        return Optional.of(outcome).orElseThrow(() -> AppException.builder().notFound404().message("outcome not found").build());
    }

    public Set<Outcome> search(Outcome outcome) {
        CriteriaBuilder builder = emSRD.getCriteriaBuilder();
        CriteriaQuery<Outcome> query = builder.createQuery(Outcome.class);
        Root<Outcome> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
        predicates.add(builder.equal(builder.lower(root.get(Outcome_.period)), outcome.getPeriod().toLowerCase()));
        predicates.add(builder.equal(builder.lower(root.get(Outcome_.outcome)), outcome.getOutcome().toLowerCase()));
        predicates.add(builder.equal(root.get(Outcome_.payDay), outcome.getPayDay()));
        predicates.add(builder.equal(root.get(Outcome_.payMonth), outcome.getPayMonth()));
        predicates.add(builder.equal(root.get(Outcome_.payYear), outcome.getPayYear()));
        predicates.add(builder.equal(root.get(Outcome_.paySeq), outcome.getPaySeq()));
        predicates.add(root.get(Outcome_.processed).isNull());
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        query.orderBy(builder.asc(root.get(Outcome_.id)));
        Stream<Outcome> outcomeStream = Sets.newLinkedHashSet(emSRD.createQuery(query).getResultList()).stream();
        return outcomeStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
