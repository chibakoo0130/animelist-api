package animelistapi.config;

import org.dbunit.database.DatabaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.database.rider.core.configuration.DBUnitConfig;

@Configuration
public class DBUnintConfig {
	
	@Bean
	DBUnitConfig dbUnitConfig() {
		return DBUnitConfig.fromGlobalConfig()
                .addDBUnitProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
	}
}
