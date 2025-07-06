package ru.job4j.accidents.repository.accident;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Primary
public class HbmAccidentRepository implements AccidentRepository {

    private final SessionFactory sf;

    @Override
    public Accident save(Accident accident) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(accident);
            tx.commit();
            return accident;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            int updatedRows = session.createQuery(
                            "DELETE FROM Accident WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
            return updatedRows > 0;
        }
    }

    @Override
    public boolean update(Accident accident) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            int updatedRows = session.createQuery(
                            "UPDATE Accident SET name = :name, text = :text, address = :address WHERE id = :id")
                    .setParameter("name", accident.getName())
                    .setParameter("text", accident.getText())
                    .setParameter("address", accident.getAddress())
                    .setParameter("id", accident.getId())
                    .executeUpdate();
            tx.commit();
            return updatedRows > 0;
        }
    }

    @Override
    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            Accident accident = session.get(Accident.class, id);
            return Optional.ofNullable(accident);
        }
    }

    @Override
    public Collection<Accident> findAll() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Accident", Accident.class)
                    .list();
        }
    }
}
