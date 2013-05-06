/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.FacilityType;
import org.openlmis.core.domain.GeographicLevel;
import org.openlmis.core.domain.GeographicZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;

@NoArgsConstructor
@Component
public class FacilityDAO {

  @Autowired
  NamedParameterJdbcTemplate template;

  final String insertGeoZoneQuery = "INSERT INTO geographic_zones VALUES(DEFAULT, :code, :name, :level.id)";
  final String insertGeoLevelQuery = "INSERT INTO geographic_levels VALUES(:levelNumber, :code, :name, :levelNumber)";
  final String insertFacilityTypeQuery = "INSERT INTO facility_types VALUES(DEFAULT, :code, :name, :description, " +
      ":levelId, :nominalMaxMonth, :nominalEop, :displayOrder, :active)";

  final String insertFacilityQuery = format("INSERT INTO facilities VALUES(" +
      "DEFAULT, :code, :name, :description, :gln, :mainPhone, :fax, :address1, :address2, " +
      " :geographicZone.id, :facilityType.id, :catchmentPopulation, :latitude, :longitude, :altitude, " +
      " :operatedBy.id, :coldStorageGrossCapacity, :coldStorageNetCapacity, :suppliesOthers, :sdp, :online, :satellite," +
      " :satelliteParentId, :hasElectricity, :hasElectronicScc, :hasElectronicDar, :active, :goLiveDate, :goDownDate, " +
      " :comment, :dataReportable, :modifiedBy, :modifiedDate, %s, :modifiedDate)", randomNumeric(5));

  final static Logger logger = LoggerFactory.getLogger(FacilityDAO.class);

  public long insertFacility(Facility facility) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertFacilityQuery, new BeanPropertySqlParameterSource(facility), keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    facility.setId(id);

    logger.info("facility id {}", id);

    return id;
  }

  public long insertFacilityType(FacilityType facilityType) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertFacilityTypeQuery, new BeanPropertySqlParameterSource(facilityType), keyHolder, new String[]{"id"});

    long autoGeneratedKey = keyHolder.getKey().longValue();
    facilityType.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public long insertGeoLevel(GeographicLevel geoLevel) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertGeoLevelQuery, new BeanPropertySqlParameterSource(geoLevel), keyHolder, new String[]{"id"});

    long autoGeneratedKey = keyHolder.getKey().longValue();
    geoLevel.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public long insertGeoZone(GeographicZone geoZone) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertGeoZoneQuery, new BeanPropertySqlParameterSource(geoZone), keyHolder, new String[]{"id"});

    long autoGeneratedKey = keyHolder.getKey().longValue();
    geoZone.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public GeographicZone getDistrictZone(int offset) {
    String sql = "SELECT id FROM geographic_zones WHERE levelId = 3 LIMIT 1 OFFSET :offset";

    SqlParameterSource namedParameters = new MapSqlParameterSource("offset", offset);

    long id = template.queryForLong(sql, namedParameters);
    GeographicZone zone = new GeographicZone();
    zone.setId(id);
    return zone;


  }
}
