package utep.ilink.swimnlng.services.api;

import javax.servlet.FilterRegistration;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import java.util.EnumSet;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.federecio.dropwizard.swagger.*;
import utep.ilink.swimnlng.health.MongoHealthCheck;
import utep.ilink.swimnlng.health.RelationalHealthCheck;
import utep.ilink.swimnlng.services.controller.NarrativesController;

public class ServiceApplication extends Application<ServiceConfiguration> {

  public static void main(String[] args) throws Exception {
    new ServiceApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
    bootstrap.addBundle(
        new SwaggerBundle<ServiceConfiguration>() {
          @Override
          protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
              ServiceConfiguration configuration) {
            return configuration.swaggerBundleConfiguration;
          }
        });

    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
        bootstrap.getConfigurationSourceProvider(),
        new EnvironmentVariableSubstitutor(false)));

  }

  @Override
  public void run(ServiceConfiguration configuration, Environment environment) throws Exception {

    // Enable CORS headers
    final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS",
        (Class<? extends Filter>) CrossOriginFilter.class);
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,Authorization");
    cors.setInitParameter("allowedMethods", "POST,OPTIONS,HEAD");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    // Enable CORS for health checks
    FilterRegistration.Dynamic healthCors = environment.getAdminContext().getServletContext().addFilter(
        "CORS",
        (Class<? extends Filter>) CrossOriginFilter.class);
    healthCors.setInitParameter("allowedOrigins", "*");
    healthCors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    healthCors.setInitParameter("allowedMethods", "GET, OPTIONS, HEAD");
    healthCors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/ping");

    /****** Register Health Checks *******/

    // SWIM Mongo Connection Check
    environment.healthChecks().register("SWIM Database", new MongoHealthCheck(configuration.getSWIMDBUser(),
        configuration.getSWIMDBPassword(), configuration.getSWIMDBName(), configuration.getSWIMDBHost(),
        configuration.getSWIMDBPort(),
        configuration.getSWIMDBAuth()));

    // Narratives Mongo Connection Check
    environment.healthChecks().register("Narrative Database", new MongoHealthCheck(configuration.getNarrativedbuser(),
        configuration.getNarrativedbpassword(), configuration.getNarrativedbname(), configuration.getNarrativedbhost(),
        configuration.getNarrativedbport(),
        configuration.getNarrativedbauth()));

    environment.healthChecks().register("Auth Database", new RelationalHealthCheck(configuration.getAuthDBUser(),
        configuration.getAuthDBPassword(), configuration.getAuthDBName(), configuration.getAuthDBHost(),
        configuration.getAuthDBPort(),
        configuration.getAuthDBDriver()));

    environment.healthChecks().register("User Database", new RelationalHealthCheck(configuration.getUserDBUser(),
        configuration.getUserDBPassword(), configuration.getUserDBName(), configuration.getUserDBHost(),
        configuration.getUserDBPort(),
        configuration.getUserDBDriver()));

    /****** Register Service Controllers ******/

    // Narratives Controller
    environment.jersey().register(new NarrativesController(configuration));

  }
}
