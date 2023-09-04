package prosense.sassa.mqclient.beneficiary.control;

import org.slf4j.Logger;

import com.google.common.collect.Sets;

import prosense.sassa.mqclient.api.control.App;
import prosense.sassa.mqclient.api.entity.AppException;
import prosense.sassa.mqclient.api.control.Store;
import prosense.sassa.mqclient.beneficiary.entity.OutcomeError;
import prosense.sassa.mqclient.beneficiary.entity.OutcomeError_;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import javax.ws.rs.core.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.criteria.*;


@ApplicationScoped
public class OutcomeErrorStore extends Store {
    @Inject
    @App
    Logger logger;

    @Inject
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean exists(final Long id) {
        return em.find(OutcomeError.class, id) != null;
    }
	
    public OutcomeError create(final OutcomeError outcomeError) {
        em.persist(outcomeError);
        return outcomeError;
    }

    public OutcomeError read(final long id) {
        return Optional.ofNullable(em.find(OutcomeError.class, id)).orElseThrow(() -> AppException.builder()
                                                                                          .notFound404()
                                                                                          .message("outcomeError not found")
                                                                                          .build());
    }

    public OutcomeError update(final OutcomeError outcomeError) {
        em.merge(outcomeError);
        return Optional.of(outcomeError).orElseThrow(() -> AppException.builder()
                                                               .notFound404()
                                                               .message("outcomeError not found")
                                                               .build());
    }

    public void delete(long id) {
        if (!exists(id)) {
            throw AppException.builder()
                              .notFound404()
                              .message("outcomeError not found")
                              .build();
        }
        em.remove(em.getReference(OutcomeError.class, id));
    }

    public Set<OutcomeError> search(final MultivaluedMap<String, String> queryParameters) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<OutcomeError> query = builder.createQuery(OutcomeError.class);
        Root<OutcomeError> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
		Optional.ofNullable(queryParameters.getFirst(OutcomeErrorAgent.messageStatus)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(OutcomeError_.messageStatus)),n)));
        final Optional<String> fromOptional = Optional.ofNullable(queryParameters.getFirst("from"));
        final Optional<String> toOptional = Optional.ofNullable(queryParameters.getFirst("to"));
        if (fromOptional.isPresent() && toOptional.isPresent()) {
            try {
                ZonedDateTime from = ZonedDateTime.parse(fromOptional.get());
                ZonedDateTime to = ZonedDateTime.parse(toOptional.get());
                predicates.add(builder.between(root.get(OutcomeError_.created), from, to));
            } catch (DateTimeParseException ex) {
                throw AppException.builder()
                                  .badRequest400()
                                  .message("date invalid: " + ex.getMessage())
                                  .build();
            }
        }
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        Stream<OutcomeError> outcomeErrorStream = Sets.newLinkedHashSet(getEntityManager().createQuery(query).setMaxResults(50).getResultList()).stream();
        return outcomeErrorStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
