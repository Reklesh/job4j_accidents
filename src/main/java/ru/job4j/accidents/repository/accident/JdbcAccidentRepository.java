package ru.job4j.accidents.repository.accident;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcAccidentRepository implements AccidentRepository {

    private final JdbcTemplate jdbc;

    @Override
    public Accident save(Accident accident) {
        jdbc.update("insert into accidents (name) values (?)", accident.getName());
        return accident;
    }

    @Override
    public boolean deleteById(int id) {
        return jdbc.update("delete from accidents where id = ?", id) > 0;
    }

    @Override
    public boolean update(Accident accident) {
        return jdbc.update(
                "update accidents set name = ? where id = ?",
                accident.getName(),
                accident.getId()
        ) > 0;
    }

    @Override
    public Optional<Accident> findById(int id) {
        return jdbc.query(
                "select id, name from accidents where id = ?",
                ps -> ps.setInt(1, id),
                rs -> {
                    if (rs.next()) {
                        Accident accident = new Accident();
                        accident.setId(rs.getInt("id"));
                        accident.setName(rs.getString("name"));
                        return Optional.of(accident);
                    }
                    return Optional.empty();
                }
        );
    }

    @Override
    public Collection<Accident> findAll() {
        return jdbc.query("select id, name from accidents",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    return accident;
                });
    }
}
