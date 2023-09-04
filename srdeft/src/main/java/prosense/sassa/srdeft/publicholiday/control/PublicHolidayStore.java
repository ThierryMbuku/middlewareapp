package prosense.sassa.srdeft.publicholiday.control;

import com.google.common.collect.Sets;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.publicholiday.entity.PublicHoliday;
import prosense.sassa.srdeft.publicholiday.entity.PublicHoliday_;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import javax.ws.rs.core.MultivaluedMap;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import java.util.*;
import java.util.stream.Collectors;

import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;


@ApplicationScoped
public class PublicHolidayStore {
    @Inject
    EntityManager em;
    
    public boolean exists(final Long id) {
        return em.find(PublicHoliday.class, id) != null;
    }

    public PublicHoliday create(final PublicHoliday publicHoliday) {
        em.persist(publicHoliday);
        return publicHoliday;
    }

    public PublicHoliday read(final long id) {
        return Optional.ofNullable(em.find(PublicHoliday.class, id)).orElseThrow(() -> AppException.builder().notFound404().message("publicHoliday not found").build());
    }

    public PublicHoliday update(final PublicHoliday publicHoliday) {
        em.merge(publicHoliday);
        return Optional.of(publicHoliday).orElseThrow(() -> AppException.builder().notFound404().message("publicHoliday not found").build());
    }

    public void delete(long id) {
        if (!exists(id)) {
            throw AppException.builder().notFound404().message("publicHoliday not found").build();
        }
        em.remove(em.getReference(PublicHoliday.class, id));
    }

    public Set<PublicHoliday> search(final MultivaluedMap<String, String> queryParameters) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PublicHoliday> query = builder.createQuery(PublicHoliday.class);
        Root<PublicHoliday> root = query.from(query.getResultType());
        Set<Predicate> predicates = new HashSet<>();
        final Optional<String> dateOptional = Optional.ofNullable(queryParameters.getFirst("date"));
        if (dateOptional.isPresent()) {
            try {
                ZonedDateTime date = LocalDate.parse(dateOptional.get(), PublicHolidayAgent.dateFormatter).atStartOfDay(ZoneId.systemDefault());
                predicates.add(builder.equal(root.get(PublicHoliday_.date), date));
            } catch (DateTimeParseException ex) {
                throw AppException.builder().badRequest400().message("date invalid: " + ex.getMessage()).build();
            }
        }
        final Optional<String> gteqDateOptional = Optional.ofNullable(queryParameters.getFirst("gteqDate"));
        if (gteqDateOptional.isPresent()) {
            try {
                ZonedDateTime date = ZonedDateTime.parse(gteqDateOptional.get()).minusDays(1);
                predicates.add(builder.greaterThanOrEqualTo(root.get(PublicHoliday_.date), date));
            } catch (DateTimeParseException ex) {
                throw AppException.builder().badRequest400().message("date invalid: " + ex.getMessage()).build();
            }
        }
        Optional.ofNullable(queryParameters.getFirst(PublicHolidayAgent.name)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(PublicHoliday_.name)),n)));
        Optional.ofNullable(queryParameters.getFirst(PublicHolidayAgent.description)).map(String::toLowerCase).ifPresent(n -> predicates.add(builder.like(builder.lower(root.get(PublicHoliday_.description)),n)));
        final Optional<String> fromOptional = Optional.ofNullable(queryParameters.getFirst("from"));
        final Optional<String> toOptional = Optional.ofNullable(queryParameters.getFirst("to"));
        if (fromOptional.isPresent() && toOptional.isPresent()) {
            try {
                ZonedDateTime from = ZonedDateTime.parse(fromOptional.get());
                ZonedDateTime to = ZonedDateTime.parse(toOptional.get());
                predicates.add(builder.between(root.get(PublicHoliday_.created), from, to));
            } catch (DateTimeParseException ex) {
                throw AppException.builder().badRequest400().message("date invalid: " + ex.getMessage()).build();
            }
        }
        query.select(root).where(builder.and(predicates.stream().toArray(Predicate[]::new)));
        final Optional<String> sortOptional = Optional.ofNullable(queryParameters.getFirst("_sort"));
        if (sortOptional.isPresent()) {
            if ("-date".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(PublicHoliday_.date)));
            } else if ("date".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(PublicHoliday_.date)));
            } else if ("-created".equals(sortOptional.get())) {
                query.orderBy(builder.desc(root.get(PublicHoliday_.created)));
            } else if ("created".equals(sortOptional.get())) {
                query.orderBy(builder.asc(root.get(PublicHoliday_.created)));
            }
        }
        Stream<PublicHoliday> publicHolidayStream = null;
        final Optional<String> limitOptional = Optional.ofNullable(queryParameters.getFirst("_limit"));
        if (limitOptional.isPresent()) {
            try {
                publicHolidayStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(Integer.parseInt(limitOptional.get())).getResultList()).stream();
            } catch (IllegalArgumentException e) {
                publicHolidayStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(10).getResultList()).stream();
            }
        } else {
           publicHolidayStream = Sets.newLinkedHashSet(em.createQuery(query).setMaxResults(10).getResultList()).stream();
        }
        return publicHolidayStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
