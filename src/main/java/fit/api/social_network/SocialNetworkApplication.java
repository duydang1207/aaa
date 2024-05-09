package fit.api.social_network;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.text.SimpleDateFormat;

@SpringBootApplication
@EnableJpaAuditing
public class SocialNetworkApplication {
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkApplication.class, args);
	}
	@Bean
	public ObjectMapper objectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
		objectMapper.setDateFormat(format);
		return objectMapper;
	}
}
