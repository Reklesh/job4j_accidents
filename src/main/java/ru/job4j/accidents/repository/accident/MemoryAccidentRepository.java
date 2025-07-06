package ru.job4j.accidents.repository.accident;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MemoryAccidentRepository implements AccidentRepository {

    private final AtomicInteger nextId = new AtomicInteger();
    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private MemoryAccidentRepository() {
        save(new Accident(0, "ДТП", "Столкновение легковых авто", "ул. Ленина, 10"));
        save(new Accident(0, "Наезд", "Наезд на пешехода", "ул. Пушкина, 5"));
        save(new Accident(0, "Парковка", "ДТП на парковке", "ул. Советская, 20"));
        save(new Accident(0, "Выезд", "Выезд через две сплошные", "М-4, 15 км"));
        save(new Accident(0, "Разворот", "Разворот в неположенном месте", "ул. Мира, 3"));
        save(new Accident(0, "ДТП", "Автобус столкнулся с авто", "ул. Красная, 25"));
    }

    @Override
    public Accident save(Accident accident) {
        return accidents.computeIfAbsent(nextId.incrementAndGet(), k -> {
            accident.setId(k);
            return accident;
        });
    }

    @Override
    public boolean deleteById(int id) {
        return Objects.nonNull(accidents.remove(id));
    }

    @Override
    public boolean update(Accident accident) {
        return Objects.nonNull(accidents.replace(accident.getId(), accident));
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(accidents.get(id));
    }

    @Override
    public Collection<Accident> findAll() {
        return accidents.values();
    }
}
