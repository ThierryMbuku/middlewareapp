package prosense.sassa.mqclient.api.control;

import com.google.common.collect.Sets;

import java.time.ZonedDateTime;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public abstract class Store {

    public abstract EntityManager getEntityManager();

    public <E> HashMap<SingularAttribute<E, ?>, Object> newAttributeMap() {
        return new HashMap<>();
    }

    public <E, A> boolean exists(@NotNull final SingularAttribute<E, A> attribute, @NotNull final A attributeValue) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(attribute.getDeclaringType().getJavaType());
        criteriaQuery.select(builder.count(root)).where(builder.equal(root.get(attribute), attributeValue));
        return getEntityManager().createQuery(criteriaQuery).getSingleResult() > 0;
    }

    @Log
    public <E> boolean exists(@NotNull @Size(min=1) final Map<SingularAttribute<E, ?>, ?> attributeMap) {
        final Optional<SingularAttribute<E, ?>> firstOptional = attributeMap.keySet().stream().findFirst();
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(firstOptional.get().getDeclaringType().getJavaType());
        final Predicate[] predicates = attributeMap.entrySet().stream().map(e -> builder.equal(root.get(e.getKey()), e.getValue())).toArray(Predicate[]::new);
        criteriaQuery.select(builder.count(root)).where(builder.and(predicates));
        return getEntityManager().createQuery(criteriaQuery).getSingleResult() > 0;
    }

    @Log
    public <E> List<E> readListBy(@NotNull @Size(min=1) final Map<SingularAttribute<E, ?>, ?> attributeMap) {
        final Optional<SingularAttribute<E, ?>> firstOptional = attributeMap.keySet().stream().findFirst();
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(firstOptional.get().getDeclaringType().getJavaType());
        Root<E> root = criteriaQuery.from(criteriaQuery.getResultType());
        final Predicate[] predicates = attributeMap.entrySet().stream().map(e -> builder.equal(root.get(e.getKey()), e.getValue())).toArray(Predicate[]::new);
        criteriaQuery.select(root).where(builder.and(predicates));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }
    
    @Log
        public <E> List<E> readDateFilterListBy(final Map<SingularAttribute<E, ?>, ?> attributeMap, SingularAttribute<E, ?> dateSingularAttribute, ZonedDateTime from, ZonedDateTime to) {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = null;
            if(attributeMap.isEmpty()) {
                criteriaQuery = builder.createQuery(dateSingularAttribute.getDeclaringType().getJavaType());
            }
            else
            {
            	final Optional<SingularAttribute<E, ?>> firstOptional = attributeMap.keySet().stream().findFirst();
            	criteriaQuery = builder.createQuery(firstOptional.get().getDeclaringType().getJavaType());
            }
            Root<E> root = criteriaQuery.from(criteriaQuery.getResultType());  
            if(!attributeMap.isEmpty()) {
            	final Predicate[] predicates = attributeMap.entrySet().stream().map(e -> builder.equal(root.get(e.getKey()), e.getValue())).toArray(Predicate[]::new);
            	if (from != null && to != null && dateSingularAttribute != null) {
                	criteriaQuery.select(root).where(builder.and(builder.and(builder.between(root.get(dateSingularAttribute.getName()), from, to))), builder.and(predicates));
            	} else {
            		criteriaQuery.select(root).where(builder.and(predicates));
                }
            } else {
            	criteriaQuery.select(root).where(builder.between(root.get(dateSingularAttribute.getName()), from, to));
            }
            return getEntityManager().createQuery(criteriaQuery).getResultList();
        }

    @Log
    public <E, A> List<E> readListBy(@NotNull final SingularAttribute<E, A> attribute, @NotNull final A attributeValue) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(attribute.getDeclaringType().getJavaType());
        Root<E> root = criteriaQuery.from(criteriaQuery.getResultType());
        criteriaQuery.select(root).where(builder.equal(root.get(attribute), attributeValue));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    @Log
    public <E, A> List<E> readListBy(@NotNull final SingularAttribute<E, A> attribute, @NotNull @Size(min=1) final Collection<A> attributeValueCollection) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(attribute.getDeclaringType().getJavaType());
        Root<E> root = criteriaQuery.from(criteriaQuery.getResultType());
        criteriaQuery.select(root).where(root.get(attribute).in(attributeValueCollection));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    @Log
    public <E> Set<E> readSetBy(@NotNull @Size(min=1) final Map<SingularAttribute<E, ?>, ?> attributeMap) {
        return Sets.newHashSet(readListBy(attributeMap));
    }
    
    @Log
    public <E> Set<E> readDateFilterSetBy(final Map<SingularAttribute<E, ?>, ?> attributeMap, SingularAttribute<E, ?> dateSingularAttribute, ZonedDateTime from, ZonedDateTime to) {
        return Sets.newHashSet(readDateFilterListBy(attributeMap, dateSingularAttribute, from, to));
    }

    @Log
    public <E, A> Set<E> readSetBy(@NotNull final SingularAttribute<E, A> attribute, @NotNull final A attributeValue) {
        return Sets.newHashSet(readListBy(attribute, attributeValue));
    }

    @Log
    public <E, A> Set<E> readSetBy(@NotNull final SingularAttribute<E, A> attribute, @NotNull @Size(min=1) final Collection<A> attributeValueCollection) {
        return Sets.newHashSet(readListBy(attribute, attributeValueCollection));
    }

    @Log
    public <E> void create(@NotNull @Size(min=1) final Collection<E> entities) {
        final EntityManager em = getEntityManager();
        AtomicInteger c = new AtomicInteger();
        entities.forEach(e -> {
            em.persist(e);
            if (c.incrementAndGet() % 20 == 0) {
                em.flush();
                em.clear();
            }
        });
    }

    @Log
    public <E, A> int deleteBy(@NotNull final SingularAttribute<E, A> attribute, @NotNull final A attributeValue) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<E> criteriaDelete = builder.createCriteriaDelete(attribute.getDeclaringType().getJavaType());
        Root<E> root = criteriaDelete.from(attribute.getDeclaringType().getJavaType());
        criteriaDelete.where(builder.equal(root.get(attribute), attributeValue));
        return getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    @Log
    public <E, A> int deleteBy(@NotNull final SingularAttribute<E, A> attribute, @NotNull @Size(min=1) Collection<A> attributeValueCollection) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<E> criteriaDelete = builder.createCriteriaDelete(attribute.getDeclaringType().getJavaType());
        Root<E> root = criteriaDelete.from(attribute.getDeclaringType().getJavaType());
        criteriaDelete.where(root.get(attribute).in(attributeValueCollection));
        return getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    @Log
    public <E> int deleteBy(@NotNull @Size(min=1) final Map<SingularAttribute<E, ?>, ?> attributeMap) {
        final Optional<SingularAttribute<E, ?>> firstOptional = attributeMap.keySet().stream().findFirst();
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<E> criteriaDelete = builder.createCriteriaDelete(firstOptional.get().getDeclaringType().getJavaType());
        Root<E> root = criteriaDelete.from(firstOptional.get().getDeclaringType().getJavaType());
        final Predicate[] predicates = attributeMap.entrySet().stream().map(e -> root.get(e.getKey()).in(e.getValue())).toArray(Predicate[]::new);
        criteriaDelete.where(builder.and(predicates));
        return getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    @Log
    public <E, I, A> int updateBy(@NotNull final SingularAttribute<E, I> id, @NotNull final I idValue, @NotNull final SingularAttribute<E, A> attribute, @NotNull final A attributeValue) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<E> criteriaUpdate = builder.createCriteriaUpdate(attribute.getDeclaringType().getJavaType());
        Root<E> root = criteriaUpdate.from(attribute.getDeclaringType().getJavaType());
        criteriaUpdate.set(root.get(attribute), attributeValue);
        criteriaUpdate.where(builder.equal(root.get(id), idValue));
        return getEntityManager().createQuery(criteriaUpdate).executeUpdate();
    }

    @Log
    public <E, I> int updateBy(@NotNull final SingularAttribute<E, I> id, @NotNull final I idValue, @NotNull @Size(min=1) final Map<SingularAttribute<E, ?>, ?> attributeMap) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<E> criteriaUpdate = builder.createCriteriaUpdate(id.getDeclaringType().getJavaType());
        Root<E> root = criteriaUpdate.from(id.getDeclaringType().getJavaType());
        //noinspection unchecked
        attributeMap.forEach((a, v) -> criteriaUpdate.set(root.get((SingularAttribute<E, Object>) a), v));
        criteriaUpdate.where(builder.equal(root.get(id), idValue));
        return getEntityManager().createQuery(criteriaUpdate).executeUpdate();
    }
}
