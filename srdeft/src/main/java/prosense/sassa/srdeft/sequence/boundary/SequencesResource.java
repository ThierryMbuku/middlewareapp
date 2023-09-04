package prosense.sassa.srdeft.sequence.boundary;

import prosense.sassa.srdeft.sequence.entity.Sequence;
import prosense.sassa.srdeft.sequence.control.SequenceStore;

import javax.ejb.Stateless;

import javax.inject.Inject;

import java.util.Set;


@Stateless
public class SequencesResource {
    @Inject
    SequenceStore sequenceStore;

    public Sequence read(long id) {
        return sequenceStore.read(id);
    }

    public Sequence read(Sequence sequence) {
        return sequenceStore.search(sequence).stream().findFirst().get();
    }

    public Sequence update(Sequence sequence) {
        return sequenceStore.update(sequence);
    }

    public Set<Sequence> searchAll() {
        return sequenceStore.search(Sequence.builder().build());
    }
}
