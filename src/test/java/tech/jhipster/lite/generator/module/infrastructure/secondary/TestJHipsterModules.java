package tech.jhipster.lite.generator.module.infrastructure.secondary;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import tech.jhipster.lite.common.infrastructure.secondary.FileSystemProjectFilesReader;
import tech.jhipster.lite.error.domain.Assert;
import tech.jhipster.lite.generator.module.application.JHipsterModulesApplicationService;
import tech.jhipster.lite.generator.module.domain.JHipsterModule;
import tech.jhipster.lite.generator.module.domain.JHipsterModuleEvents;
import tech.jhipster.lite.generator.module.domain.JHipsterModuleSlug;
import tech.jhipster.lite.generator.module.domain.JHipsterModuleToApply;
import tech.jhipster.lite.generator.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.generator.npm.domain.NpmPackageName;
import tech.jhipster.lite.generator.npm.domain.NpmVersion;
import tech.jhipster.lite.generator.npm.domain.NpmVersions;

public final class TestJHipsterModules {

  private TestJHipsterModules() {}

  public static void apply(JHipsterModule module) {
    applyer().module(module).properties(JHipsterModuleProperties.defaultProperties(module.projectFolder())).slug("test-module").apply();
  }

  public static TestJHipsterModulesModuleApplyer applyer() {
    return new TestJHipsterModulesApplyer();
  }

  public static class TestJHipsterModulesApplyer
    implements
      TestJHipsterModulesModuleApplyer,
      TestJHipsterModulesPropertiesApplyer,
      TestJHipsterModulesSlugApplyer,
      TestJHipsterModulesFinalApplyer {

    private static final JHipsterModulesApplicationService modules = buildApplicationService();

    private JHipsterModule module;
    private JHipsterModuleProperties properties;
    private JHipsterModuleSlug slug;

    private TestJHipsterModulesApplyer() {}

    private static JHipsterModulesApplicationService buildApplicationService() {
      FileSystemProjectFilesReader filesReader = new FileSystemProjectFilesReader();

      FileSystemJHipsterModulesRepository modulesRepository = new FileSystemJHipsterModulesRepository(filesReader, npmVersions());

      return new JHipsterModulesApplicationService(
        modulesRepository,
        mock(JHipsterModuleEvents.class),
        new FileSystemCurrentJavaDependenciesVersionsRepository(filesReader),
        new FileSystemProjectJavaDependenciesRepository()
      );
    }

    private static NpmVersions npmVersions() {
      NpmVersions npmVersions = mock(NpmVersions.class);

      NpmVersion version = new NpmVersion("1.1.1");
      lenient().when(npmVersions.get(anyString(), any())).thenReturn(version);
      lenient().when(npmVersions.get(any(NpmPackageName.class), any())).thenReturn(version);

      return npmVersions;
    }

    @Override
    public TestJHipsterModulesPropertiesApplyer module(JHipsterModule module) {
      Assert.notNull("module", module);

      this.module = module;

      return this;
    }

    @Override
    public TestJHipsterModulesSlugApplyer properties(JHipsterModuleProperties properties) {
      Assert.notNull("properties", properties);

      this.properties = properties;

      return this;
    }

    @Override
    public TestJHipsterModulesFinalApplyer slug(JHipsterModuleSlug slug) {
      Assert.notNull("slug", slug);

      this.slug = slug;

      return this;
    }

    @Override
    public void apply() {
      modules.apply(new JHipsterModuleToApply(properties, slug, module));
    }
  }

  public interface TestJHipsterModulesModuleApplyer {
    public TestJHipsterModulesPropertiesApplyer module(JHipsterModule module);
  }

  public interface TestJHipsterModulesPropertiesApplyer {
    TestJHipsterModulesSlugApplyer properties(JHipsterModuleProperties properties);
  }

  public interface TestJHipsterModulesSlugApplyer {
    TestJHipsterModulesFinalApplyer slug(JHipsterModuleSlug slug);

    default TestJHipsterModulesFinalApplyer slug(String slug) {
      return slug(new JHipsterModuleSlug(slug));
    }
  }

  public interface TestJHipsterModulesFinalApplyer {
    void apply();
  }
}