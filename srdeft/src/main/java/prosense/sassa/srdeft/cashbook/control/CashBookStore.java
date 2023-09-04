package prosense.sassa.srdeft.cashbook.control;

import prosense.sassa.srdeft.cashbook.entity.CashBook;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import javax.persistence.EntityManager;


@ApplicationScoped
public class CashBookStore {
    @Inject
    EntityManager em;
    
    public CashBook create(final CashBook cashBook) {
        em.persist(cashBook);
        return cashBook;
    }
}
