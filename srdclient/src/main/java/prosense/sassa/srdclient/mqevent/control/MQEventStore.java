package prosense.sassa.srdclient.mqevent.control;

import org.slf4j.Logger;

import com.google.common.collect.Sets;

import prosense.sassa.srdclient.api.control.App;
import prosense.sassa.srdclient.api.entity.AppException;
import prosense.sassa.srdclient.mqevent.entity.MQEvent;
import prosense.sassa.srdclient.mqevent.entity.MQEvent_;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import javax.ws.rs.core.MultivaluedMap;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import java.time.ZonedDateTime;

import java.time.format.DateTimeParseException;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;


@ApplicationScoped
public class MQEventStore {
    @Inject
    @App
    Logger logger;

    @Inject
    EntityManager em;

    public boolean exists(final Long id) {
        return em.find(MQEvent.class, id) != null;
    }

    public MQEvent create(final MQEvent mqevent) {
        em.persist(mqevent);
        return mqevent;
    }

    public MQEvent read(final long id) {
        return Optional.ofNullable(em.find(MQEvent.class, id)).orElseThrow(() -> AppException.builder()
                                                                                          .notFound404()
                                                                                          .message("mqevent not found")
                                                                                          .build());
    }

    public MQEvent update(final MQEvent mqevent) {
        em.merge(mqevent);
        return Optional.of(mqevent).orElseThrow(() -> AppException.builder()
                                                               .notFound404()
                                                               .message("mqevent not found")
                                                               .build());
    }

    public void delete(long id) {
        if (!exists(id)) {
            throw AppException.builder()
                              .notFound404()
                              .message("mqevent not found")
                              .build();
        }
        em.remove(em.getReference(MQEvent.class, id));
    }


    public Set<MQEvent> search(final MultivaluedMap<String, String> queryParameters) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MQEvent> query = builder.createQuery(MQEvent.class);
        Root<MQEvent> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
		Optional.ofNullable(queryParameters.getFirst(MQEventAgent.outcomeId)).ifPresent(n -> predicates.add(builder.equal(root.get(MQEvent_.outcomeId), n)));
		Optional.ofNullable(queryParameters.getFirst(MQEventAgent.messageId)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(MQEvent_.messageId)),n)));
		Optional.ofNullable(queryParameters.getFirst(MQEventAgent.correlation)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(MQEvent_.correlation)),n)));
        final Optional<String> fromOptional = Optional.ofNullable(queryParameters.getFirst("from"));
        final Optional<String> toOptional = Optional.ofNullable(queryParameters.getFirst("to"));
        if (fromOptional.isPresent() && toOptional.isPresent()) {
            try {
                ZonedDateTime from = ZonedDateTime.parse(fromOptional.get());
                ZonedDateTime to = ZonedDateTime.parse(toOptional.get());
                predicates.add(builder.between(root.get(MQEvent_.created), from, to));
            } catch (DateTimeParseException ex) {
                throw AppException.builder()
                                  .badRequest400()
                                  .message("date invalid: " + ex.getMessage())
                                  .build();
            }
        }
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        final Optional<String> sortOptional = Optional.ofNullable(queryParameters.getFirst("_sort"));
        if (sortOptional.isPresent()) {
            if ("-id".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(MQEvent_.id)));
            } else if ("id".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(MQEvent_.id)));
            } else if ("-created".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(MQEvent_.created)));
            } else if ("created".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(MQEvent_.created)));
            }
        }
        Stream<MQEvent> mqeventStream = null;
        final Optional<String> limitOptional = Optional.ofNullable(queryParameters.getFirst("_limit"));
        if (limitOptional.isPresent()) {
            try {
                mqeventStream = Sets.newLinkedHashSet(em.createQuery(query)
                                                                     .setMaxResults(Integer.parseInt(limitOptional.get()))
                                                                     .getResultList()).stream();
            } catch (IllegalArgumentException e) {
                mqeventStream = Sets.newLinkedHashSet(em.createQuery(query)
                                                                     .setMaxResults(50)
                                                                     .getResultList()).stream();
            }
        } else {
           mqeventStream = Sets.newLinkedHashSet(em.createQuery(query)
                                                                     .setMaxResults(50)
                                                                     .getResultList()).stream();
        }
        return mqeventStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
