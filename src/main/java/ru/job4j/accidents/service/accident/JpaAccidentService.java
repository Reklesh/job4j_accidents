package ru.job4j.accidents.service.accident;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.accident.JpaAccidentRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class JpaAccidentService {

    private final JpaAccidentRepository jpaAccidentRepository;

    public void create(Accident accident) {
        jpaAccidentRepository.save(accident);
    }

    public List<Accident> getAll() {
        return (List<Accident>) jpaAccidentRepository.findAll();
    }
}
