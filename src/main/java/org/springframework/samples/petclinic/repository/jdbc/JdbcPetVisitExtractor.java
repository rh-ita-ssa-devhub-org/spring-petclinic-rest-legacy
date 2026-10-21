/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.NonNull;
import org.springframework.samples.petclinic.model.Visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link ResultSetExtractor} implementation that extracts a list of {@link JdbcPet}
 * objects with their associated {@link Visit} children from a joined {@link ResultSet}.
 *
 * <p>Replaces the former dependency on {@code OneToManyResultSetExtractor} from the
 * Spring Data JDBC extensions library, which is no longer available in the
 * Spring Boot 3.0.0 / Spring Data 3.x ecosystem.
 */
public class JdbcPetVisitExtractor implements ResultSetExtractor<List<JdbcPet>> {

    private final JdbcPetRowMapper petRowMapper = new JdbcPetRowMapper();
    private final JdbcVisitRowMapper visitRowMapper = new JdbcVisitRowMapper();

    /**
     * Iterates over every row in the {@link ResultSet}, groups rows by pet ID using a
     * {@link LinkedHashMap} to preserve insertion order, and attaches any associated
     * {@link Visit} records to the correct {@link JdbcPet}.
     *
     * @param rs the {@link ResultSet} to extract data from
     * @return a {@link List} of {@link JdbcPet} objects, each populated with its visits
     */
    @Override
    public List<JdbcPet> extractData(@NonNull ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, JdbcPet> map = new LinkedHashMap<>();
        int rowNum = 0;
        while (rs.next()) {
            Integer petId = mapPrimaryKey(rs);
            JdbcPet pet = map.get(petId);
            if (pet == null) {
                pet = petRowMapper.mapRow(rs, rowNum);
                map.put(petId, pet);
            }
            Integer visitId = mapForeignKey(rs);
            if (visitId != null) {
                addChild(pet, visitRowMapper.mapRow(rs, rowNum));
            }
            rowNum++;
        }
        return new ArrayList<>(map.values());
    }

    /**
     * Extracts the primary key (pet ID) from the current row of the {@link ResultSet}.
     *
     * @param rs the current {@link ResultSet} row
     * @return the pet ID
     */
    private Integer mapPrimaryKey(ResultSet rs) throws SQLException {
        return rs.getInt("pets_id");
    }

    /**
     * Extracts the foreign key (visit's pet_id) from the current row of the
     * {@link ResultSet}, returning {@code null} when no visit is present on this row.
     *
     * @param rs the current {@link ResultSet} row
     * @return the visit's pet_id, or {@code null} if the column is SQL NULL
     */
    private Integer mapForeignKey(ResultSet rs) throws SQLException {
        if (rs.getObject("visits_pet_id") == null) {
            return null;
        } else {
            return rs.getInt("visits_pet_id");
        }
    }

    /**
     * Adds a {@link Visit} child to the given {@link JdbcPet}.
     *
     * @param root  the parent {@link JdbcPet}
     * @param child the {@link Visit} to attach
     */
    private void addChild(JdbcPet root, Visit child) {
        root.addVisit(child);
    }
}
