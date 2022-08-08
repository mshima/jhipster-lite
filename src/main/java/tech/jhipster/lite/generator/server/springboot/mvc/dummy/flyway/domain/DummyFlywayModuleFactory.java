package tech.jhipster.lite.generator.server.springboot.mvc.dummy.flyway.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import tech.jhipster.lite.error.domain.Assert;
import tech.jhipster.lite.module.domain.JHipsterDestination;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterSource;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

public class DummyFlywayModuleFactory {

  private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);

  private static final JHipsterSource SOURCE = from("server/springboot/mvc/dummy/flyway");
  private static final JHipsterDestination MIGRATION_DESTINATION = to("src/main/resources/db/migration/");

  private static final String NOT_POSTGRESQL_CHANGELOG = "00000000000_dummy_feature_schema.sql";
  private static final String POSTGRESQL_CHANGELOG = "00000000000_postgresql_dummy_feature_schema.sql";

  public JHipsterModule buildModule(JHipsterModuleProperties properties, Instant date) {
    Assert.notNull("properties", properties);
    Assert.notNull("date", date);

    String sourceFile = getSourceFile(properties);

    //@formatter:off
    return moduleBuilder(properties)
      .files()
        .add(SOURCE.file(sourceFile), MIGRATION_DESTINATION.append(dummyFlywayFilename(date)))
        .and()
      .build();
    //@formatter:on
  }

  private String getSourceFile(JHipsterModuleProperties properties) {
    if (properties.getBoolean("usePostgreSQLTypes")) {
      return POSTGRESQL_CHANGELOG;
    }

    return NOT_POSTGRESQL_CHANGELOG;
  }

  private String dummyFlywayFilename(Instant date) {
    return new StringBuilder()
      .append("V")
      .append(FILE_DATE_FORMAT.format(date.plusSeconds(1)))
      .append("__dummy_feature_schema.sql")
      .toString();
  }
}