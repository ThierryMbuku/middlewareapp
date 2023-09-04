package prosense.sassa.srdeft.covjob.control;

import com.google.common.collect.Sets;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.covjob.entity.CovJob_;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import javax.ws.rs.core.MultivaluedMap;

import java.time.ZonedDateTime;

import java.time.format.DateTimeParseException;

import java.util.*;
import java.util.stream.Collectors;

import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;


@ApplicationScoped
public class CovJobStore {
    @Inject
    EntityManager em;
    
    public boolean exists(final Long id) {
        return em.find(CovJob.class, id) != null;
    }

    public CovJob create(final CovJob covJob) {
        em.persist(covJob);
        return covJob;
    }

    public CovJob read(final long id) {
        return Optional.ofNullable(em.find(CovJob.class, id)).orElseThrow(() -> AppException.builder().notFound404().message("covJob not found").build());
    }

    public CovJob update(final CovJob covJob) {
        em.merge(covJob);
        return Optional.of(covJob).orElseThrow(() -> AppException.builder().notFound404().message("covJob not found").build());
    }

    public void delete(long id) {
        if (!exists(id)) {
            throw AppException.builder().notFound404().message("covJob not found").build();
        }
        em.remove(em.getReference(CovJob.class, id));
    }

    public Set<CovJob> search(final MultivaluedMap<String, String> queryParameters) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CovJob> query = builder.createQuery(CovJob.class);
        Root<CovJob> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.verificationPeriod)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(CovJob_.verificationPeriod)),n)));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.period)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(CovJob_.period)),n)));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.payDay)).ifPresent(n -> predicates.add(builder.equal(root.get(CovJob_.payDay), n)));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.subService)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(CovJob_.subService)),n)));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.type)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(CovJob_.type)),n)));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.status)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(CovJob_.status)),n)));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.signStatus)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(CovJob_.signStatus)),n)));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.test)).ifPresent(n -> predicates.add(builder.equal(root.get(CovJob_.test),Boolean.valueOf(n))));
        Optional.ofNullable(queryParameters.getFirst(CovJobAgent.batchCovJob)).ifPresent(n -> predicates.add(builder.equal(root.get(CovJob_.batchCovJob), n)));
        final Optional<String> fromOptional = Optional.ofNullable(queryParameters.getFirst("from"));
        final Optional<String> toOptional = Optional.ofNullable(queryParameters.getFirst("to"));
        if (fromOptional.isPresent() && toOptional.isPresent()) {
            try {
                ZonedDateTime from = ZonedDateTime.parse(fromOptional.get());
                ZonedDateTime to = ZonedDateTime.parse(toOptional.get());
                predicates.add(builder.between(root.get(CovJob_.created), from, to));
            } catch (DateTimeParseException ex) {
                throw AppException.builder().badRequest400().message("date invalid: " + ex.getMessage()).build();
            }
        }
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        final Optional<String> sortOptional = Optional.ofNullable(queryParameters.getFirst("_sort"));
        if (sortOptional.isPresent()) {
            if ("-id".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(CovJob_.id)));
            } else if ("id".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(CovJob_.id)));
            } else if ("-created".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(CovJob_.created)));
            } else if ("created".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(CovJob_.created)));
            }
        }
        Stream<CovJob> covJobStream = null;
        final Optional<String> limitOptional = Optional.ofNullable(queryParameters.getFirst("_limit"));
        if (limitOptional.isPresent()) {
            try {
                covJobStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(Integer.parseInt(limitOptional.get())).getResultList()).stream();
            } catch (IllegalArgumentException e) {
                covJobStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(50).getResultList()).stream();
            }
        } else {
           covJobStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(50).getResultList()).stream();
        }
        return covJobStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
