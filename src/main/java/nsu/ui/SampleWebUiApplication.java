/*
 * Copyright 2012-2013 the original author or authors.
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

package nsu.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.sql.SQLException;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SampleWebUiApplication {

	@Bean
	public TransactionRepository transactionRepository() throws SQLException, ClassNotFoundException {
		return new MySqlRepository();
	}

	@Bean
	public CategoryRepository categoryRepository() throws SQLException, ClassNotFoundException {
		return new MySqlRepository();
	}

	@Bean
	public Converter<String, Transaction> messageConverter() {
		return new Converter<String, Transaction>() {
			@Override
			public Transaction convert(String id) {
				try {
					return transactionRepository().findTransaction(Long.valueOf(id));
				} catch (SQLException | ClassNotFoundException throwables) {
					throwables.printStackTrace();
				}
				return null;
			}
		};
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleWebUiApplication.class, args);
	}

}
