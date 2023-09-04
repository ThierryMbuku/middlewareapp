package prosense.sassa.srdeft.cashbook.boundary;

import prosense.sassa.srdeft.cashbook.entity.CashBook;
import prosense.sassa.srdeft.cashbook.control.CashBookStore;

import javax.ejb.Stateless;

import javax.inject.Inject;


@Stateless
public class CashBooksResource {
    @Inject
    CashBookStore cashBookStore;

    public CashBook create(CashBook cashBook) {
        return cashBookStore.create(cashBook);
    }
}
