/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package nsu.ui.mvc;

import javax.validation.Valid;

import nsu.ui.Category;
import nsu.ui.Transaction;
import nsu.ui.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.*;

/**
 * @author Rob Winch
 */
@Controller
@RequestMapping("/")
public class TransactionController {
	private final TransactionRepository transactionRepository;
	private Long id;

	@Autowired
	public TransactionController(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Transaction transaction) throws SQLException {
		id = transaction.getId();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("transactions/edit");
		mav.addObject("categories", this.transactionRepository.findCategories());
		mav.addObject("transaction", this.transactionRepository.findTransaction(transaction.getId()));
		return mav;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public ModelAndView edit(@Valid Transaction transaction, BindingResult result,
							 RedirectAttributes redirect) throws SQLException {
		if (result.hasErrors()) {
			ArrayList<Category> categories = this.transactionRepository.findCategories();
			ModelAndView mav = new ModelAndView();
			mav.setViewName("transactions/edit");
			mav.addObject("categories", categories);
			return mav;
		}

		transaction.setId(id);
		this.transactionRepository.updateTransaction(transaction);
		return new ModelAndView("redirect:/transactions");
	}


	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@Valid Transaction transaction, BindingResult result,
							 RedirectAttributes redirect) throws SQLException {
		if (result.hasErrors()) {
			return new ModelAndView("transactions/edit", "formErrors", result.getAllErrors());
		}
		this.transactionRepository.deleteTransaction(id);
		return new ModelAndView("redirect:/transactions");
	}


	@RequestMapping("/transactions")
	public ModelAndView list() throws SQLException {
		HashMap<String, HashMap<String, ArrayList<Transaction>>> dateCategoryMap = this.transactionRepository.grouping();
		ArrayList<Integer> daySums = this.transactionRepository.getDaySums();
		HashMap<String, HashMap<String, Integer>> categorySums = this.transactionRepository.getCategorySums();
		ArrayList<String> dates = this.transactionRepository.getDates();

		ModelAndView mav = new ModelAndView();


		mav.setViewName("transactions/list");
		mav.addObject("daySums", daySums);
		mav.addObject("dateCategoryMap", dateCategoryMap);
		mav.addObject("categorySums", categorySums);
		mav.addObject("dates", dates);

		return mav;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createTransactionForm(@ModelAttribute Transaction transaction) throws SQLException {
		ArrayList<Category> categories = this.transactionRepository.findCategories();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("transactions/create");
		mav.addObject("categories", categories);
		return mav;
	}



	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createTransaction(@Valid Transaction transaction, BindingResult result,
			RedirectAttributes redirect) throws SQLException {

		if (result.hasErrors()) {
			ArrayList<Category> categories = this.transactionRepository.findCategories();
			ModelAndView mav = new ModelAndView();
			mav.setViewName("transactions/create");
			mav.addObject("categories", categories);
			mav.addObject("formErrors", result.getAllErrors());
			return mav;
		}

		this.transactionRepository.saveTransaction(transaction);

		return new ModelAndView("redirect:/transactions");

	}

	@RequestMapping("foo")
	public String foo() {
		throw new RuntimeException("Expected exception in controller");
	}

}
