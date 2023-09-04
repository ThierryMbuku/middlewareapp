package prosense.sassa.srdeft.batchcovjob.control;

import com.google.common.collect.Sets;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.batchcovjob.entity.BatchCovJob;
import prosense.sassa.srdeft.batchcovjob.entity.BatchCovJob_;

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
public class BatchCovJobStore {
    @Inject
    EntityManager em;
    
    public boolean exists(final Long id) {
        return em.find(BatchCovJob.class, id) != null;
    }

    public BatchCovJob create(final BatchCovJob batchCovJob) {
        em.persist(batchCovJob);
        return batchCovJob;
    }

    public BatchCovJob read(final long id) {
        return Optional.ofNullable(em.find(BatchCovJob.class, id)).orElseThrow(() -> AppException.builder().notFound404().message("batchCovJob not found").build());
    }

    public BatchCovJob update(final BatchCovJob batchCovJob) {
        em.merge(batchCovJob);
        return Optional.of(batchCovJob).orElseThrow(() -> AppException.builder().notFound404().message("batchCovJob not found").build());
    }

    public void delete(long id) {
        if (!exists(id)) {
            throw AppException.builder().notFound404().message("batchCovJob not found").build();
        }
        em.remove(em.getReference(BatchCovJob.class, id));
    }

    public Set<BatchCovJob> search(final MultivaluedMap<String, String> queryParameters) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BatchCovJob> query = builder.createQuery(BatchCovJob.class);
        Root<BatchCovJob> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
        final Optional<String> processingDateOptional = Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.processingDate));
        if (processingDateOptional.isPresent()) {
            try {
                ZonedDateTime processingDate = ZonedDateTime.parse(processingDateOptional.get());
                predicates.add(builder.equal(root.get(BatchCovJob_.processingDate), processingDate));
            } catch (DateTimeParseException ex) {
                throw AppException.builder().badRequest400().message("date invalid: " + ex.getMessage()).build();
            }
        }
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.verificationPeriod)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(BatchCovJob_.verificationPeriod)),n)));
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.period)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(BatchCovJob_.period)),n)));
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.payDay)).ifPresent(n -> predicates.add(builder.equal(root.get(BatchCovJob_.payDay), n)));
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.subService)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(BatchCovJob_.subService)),n)));
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.type)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(BatchCovJob_.type)),n)));
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.status)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(BatchCovJob_.status)),n)));
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.signStatus)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(BatchCovJob_.signStatus)),n)));
        Optional.ofNullable(queryParameters.getFirst(BatchCovJobAgent.test)).ifPresent(n -> predicates.add(builder.equal(root.get(BatchCovJob_.test),Boolean.valueOf(n))));
        final Optional<String> fromOptional = Optional.ofNullable(queryParameters.getFirst("from"));
        final Optional<String> toOptional = Optional.ofNullable(queryParameters.getFirst("to"));
        if (fromOptional.isPresent() && toOptional.isPresent()) {
            try {
                ZonedDateTime from = ZonedDateTime.parse(fromOptional.get());
                ZonedDateTime to = ZonedDateTime.parse(toOptional.get());
                predicates.add(builder.between(root.get(BatchCovJob_.created), from, to));
            } catch (DateTimeParseException ex) {
                throw AppException.builder().badRequest400().message("date invalid: " + ex.getMessage()).build();
            }
        }
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        final Optional<String> sortOptional = Optional.ofNullable(queryParameters.getFirst("_sort"));
        if (sortOptional.isPresent()) {
            if ("-id".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(BatchCovJob_.id)));
            } else if ("id".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(BatchCovJob_.id)));
            } else if ("-created".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(BatchCovJob_.created)));
            } else if ("created".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(BatchCovJob_.created)));
            }
        }
        Stream<BatchCovJob> batchCovJobStream = null;
        final Optional<String> limitOptional = Optional.ofNullable(queryParameters.getFirst("_limit"));
        if (limitOptional.isPresent()) {
            try {
                batchCovJobStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(Integer.parseInt(limitOptional.get())).getResultList()).stream();
            } catch (IllegalArgumentException e) {
                batchCovJobStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(50).getResultList()).stream();
            }
        } else {
           batchCovJobStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(50).getResultList()).stream();
        }
        return batchCovJobStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
