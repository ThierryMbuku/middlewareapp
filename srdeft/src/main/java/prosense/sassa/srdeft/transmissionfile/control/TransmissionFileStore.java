package prosense.sassa.srdeft.transmissionfile.control;

import org.slf4j.Logger;

import com.google.common.collect.Sets;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile_;

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
public class TransmissionFileStore {
    @Inject
    @App
    Logger logger;

    @Inject
    EntityManager em;
    
    @Inject
    prosense.sassa.srdeft.file.control.TransmissionFileStore transmissionFileStore;
    
    public TransmissionFile create(TransmissionFile transmissionFile) {
    	try {
	    	transmissionFile = transmissionFileStore.generate(transmissionFile);
	    } catch (Exception e) {
            logger.error("exception", e);
	    	throw AppException.builder().internalServerError500().message(e.getMessage()).build();
	    }
        em.persist(transmissionFile);
        return transmissionFile;
    }

    public TransmissionFile read(final long id) {
        return Optional.ofNullable(em.find(TransmissionFile.class, id)).orElseThrow(() -> AppException.builder().notFound404().message("transmissionFile not found").build());
    }

    public Set<TransmissionFile> search(final MultivaluedMap<String, String> queryParameters) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TransmissionFile> query = builder.createQuery(TransmissionFile.class);
        Root<TransmissionFile> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
        Optional.ofNullable(queryParameters.getFirst(TransmissionFileAgent.fileName)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.equal(builder.lower(root.get(TransmissionFile_.fileName)),n)));
        Optional.ofNullable(queryParameters.getFirst(TransmissionFileAgent.service)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.equal(builder.lower(root.get(TransmissionFile_.service)),n)));
        Optional.ofNullable(queryParameters.getFirst(TransmissionFileAgent.subService)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.equal(builder.lower(root.get(TransmissionFile_.subService)),n)));
        Optional.ofNullable(queryParameters.getFirst(TransmissionFileAgent.type)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.equal(builder.lower(root.get(TransmissionFile_.type)),n)));
        Optional.ofNullable(queryParameters.getFirst(TransmissionFileAgent.ackStatus)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.equal(builder.lower(root.get(TransmissionFile_.ackStatus)),n)));
        Optional.ofNullable(queryParameters.getFirst(TransmissionFileAgent.test)).ifPresent(n -> predicates.add(builder.equal(root.get(TransmissionFile_.test),Boolean.valueOf(n))));
        final Optional<String> fromOptional = Optional.ofNullable(queryParameters.getFirst("from"));
        final Optional<String> toOptional = Optional.ofNullable(queryParameters.getFirst("to"));
        if (fromOptional.isPresent() && toOptional.isPresent()) {
            try {
                ZonedDateTime from = ZonedDateTime.parse(fromOptional.get());
                ZonedDateTime to = ZonedDateTime.parse(toOptional.get());
                predicates.add(builder.between(root.get(TransmissionFile_.created), from, to));
            } catch (DateTimeParseException ex) {
                throw AppException.builder().badRequest400().message("date invalid: " + ex.getMessage()).build();
            }
        }
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        final Optional<String> sortOptional = Optional.ofNullable(queryParameters.getFirst("_sort"));
        if (sortOptional.isPresent()) {
            if ("-id".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(TransmissionFile_.id)));
            } else if ("id".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(TransmissionFile_.id)));
            } else if ("-created".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(TransmissionFile_.created)));
            } else if ("created".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(TransmissionFile_.created)));
            }
        }
        Stream<TransmissionFile> transmissionFileStream = null;
        final Optional<String> limitOptional = Optional.ofNullable(queryParameters.getFirst("_limit"));
        if (limitOptional.isPresent()) {
            try {
                transmissionFileStream = Sets.newLinkedHashSet(em.createQuery(query)
                                                                     .setMaxResults(Integer.parseInt(limitOptional.get())).getResultList()).stream();
            } catch (IllegalArgumentException e) {
                transmissionFileStream = Sets.newLinkedHashSet(em.createQuery(query)
                                                                     .setMaxResults(50).getResultList()).stream();
            }
        } else {
           transmissionFileStream = Sets.newLinkedHashSet(em.createQuery(query)
                                                                     .setMaxResults(50).getResultList()).stream();
        }
        return transmissionFileStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
