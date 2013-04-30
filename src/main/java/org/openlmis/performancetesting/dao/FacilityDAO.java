/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.FacilityType;
import org.openlmis.core.domain.GeographicLevel;
import org.openlmis.core.domain.GeographicZone;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import static java.lang.String.format;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;

public class FacilityDAO {
  NamedParameterJdbcTemplate template;

  final String insertGeoZoneQuery = "INSERT INTO geographic_zones VALUES(DEFAULT, :code, :name, :level.id)";
  final String insertGeoLevelQuery = "INSERT INTO geographic_levels VALUES(DEFAULT, :code, :name, :levelNumber)";
  final String insertFacilityTypeQuery = "INSERT INTO facility_types VALUES(DEFAULT, :code, :name, :description, " +
      ":levelId, :nominalMaxMonth, :nominalEop, :displayOrder, :active)";

  final String insertFacilityQuery = format("INSERT INTO facilities VALUES(" +
      "DEFAULT, :code, :name, :description, :gln, :mainPhone, :fax, :address1, :address2, " +
      " :geographicZone.id, :facilityType.id, :catchmentPopulation, :latitude, :longitude, :altitude, " +
      " :operatedBy.id, :coldStorageGrossCapacity, :coldStorageNetCapacity, :suppliesOthers, :sdp, :online, :satellite," +
      " :satelliteParentId, :hasElectricity, :hasElectronicScc, :hasElectronicDar, :active, :goLiveDate, :goDownDate, " +
      " :comment, :dataReportable, :modifiedBy, :modifiedDate, %s, :modifiedDate)", randomNumeric(5));

  public FacilityDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertFacility(Facility facility) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertFacilityQuery, new BeanPropertySqlParameterSource(facility), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    facility.setId(id);
    return id;
  }

  public int insertFacilityType(FacilityType facilityType) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertFacilityTypeQuery, new BeanPropertySqlParameterSource(facilityType), keyHolder, new String[]{"id"});

    int autoGeneratedKey = keyHolder.getKey().intValue();
    facilityType.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public int insertGeoLevel(GeographicLevel geoLevel) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertGeoLevelQuery, new BeanPropertySqlParameterSource(geoLevel), keyHolder, new String[]{"id"});

    int autoGeneratedKey = keyHolder.getKey().intValue();
    geoLevel.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public int insertGeoZone(GeographicZone geoZone) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertGeoZoneQuery, new BeanPropertySqlParameterSource(geoZone), keyHolder, new String[]{"id"});

    int autoGeneratedKey = keyHolder.getKey().intValue();
    geoZone.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public GeographicZone getZone(int offset) {
    String sql = "SELECT id FROM geographic_zones LIMIT 1 OFFSET :offset";

    SqlParameterSource namedParameters = new MapSqlParameterSource("offset", offset);

    int id = template.queryForInt(sql, namedParameters);
    GeographicZone zone = new GeographicZone();
    zone.setId(id);
    return zone;


  }
}
