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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dave Syer
 */
public class InMemoryMessageRepository implements TransactionRepository, CategoryRepository {

	private static AtomicLong transactionCounter = new AtomicLong();
	private static AtomicLong categoryCounter = new AtomicLong();

	private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<Long, Transaction>();
	private final ConcurrentMap<Long, Category> categories = new ConcurrentHashMap<Long, Category>();

	@Override
	public Iterable<Transaction> findAll() {
		return this.transactions.values();
	}


	@Override
	public Transaction saveTransaction(Transaction transaction) {
		Long id = transaction.getId();
		if (id == null) {
			id = transactionCounter.incrementAndGet();
			transaction.setId(id);
		}
		this.transactions.put(id, transaction);
		System.out.println(this.transactions);
		return transaction;
	}

	@Override
	public Transaction findTransaction(Long id) {
		return this.transactions.get(id);
	}

	@Override
	public Category saveCategory(Category category) {
		Long id = category.getId();
		if (id == null) {
			id = categoryCounter.incrementAndGet();
			category.setId(id);
		}

		this.categories.put(id, category);
		System.out.println(this.categories);
		return category;
	}
}
